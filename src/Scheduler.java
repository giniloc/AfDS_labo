import java.util.*;

public class Scheduler {

    private final Vehicle[] vehicles;
    private final Queue<Request> requests;
    private final int loadingDuration;
    private final Map<String, BoxStack> boxStacks;
    private final List<Task> tasksInOrder = new ArrayList<>(1024);

    public Scheduler(Vehicle[] vehicles, Queue<Request> requests, int loadingDuration, Map<String, BoxStack> boxStacks) {
        this.vehicles = vehicles;
        this.requests = requests;
        this.loadingDuration = loadingDuration;
        this.boxStacks = boxStacks;
    }

    public void start() {
        while (!requests.isEmpty()) {
            for (Vehicle vehicle : vehicles) {
                processRequest(vehicle);
            }
        }

        printTasks();
    }

    private void processRequest(Vehicle vehicle) {
        Request request = requests.poll();
        assert request != null;
        BoxStack pickupStack = request.getPickupLocation();
        BoxStack deliveryStack = request.getDeliveryLocation();
        String targetBox = request.getBoxID();
        int currentTime = vehicle.getCurrentTime();

        List<Task> undoRelocation_pickup = new ArrayList<>(64);
        List<Task> undoRelocation_deliver = new ArrayList<>(64);

        if (!pickupStack.getName().equals("BufferPoint")) {
            String currentBox = "";
            int boxCounter = 0;

            while (!currentBox.equals(targetBox)) {
                List<Task> relocation_deliver = new ArrayList<>(16);
                boxCounter++;
                int driveTime_toPickup = vehicle.getDriveTime(pickupStack);
                int driveTime_toRelocate = 0;

                for (int i = 0; i < vehicle.getCapacity(); i++) {
                    currentBox = pickupStack.pop();
                    if (currentBox.equals(targetBox)) break;

                    BoxStack relocationStack = getRelocationStack(pickupStack, deliveryStack, boxCounter);
                    Task newTask = new Task(loadingDuration + driveTime_toPickup, TaskType.PU, currentBox, vehicle, pickupStack);
                    currentTime = schedule(newTask, currentTime);

                    if (i == 0) {
                        vehicle.driveTo(pickupStack);
                        driveTime_toRelocate = vehicle.getDriveTime(relocationStack);
                        vehicle.driveTo(relocationStack);
                    }

                    undoRelocation_pickup.add(new Task(loadingDuration + driveTime_toRelocate, TaskType.PL, currentBox, vehicle, pickupStack));

                    if (i == 0) vehicle.driveTo(pickupStack);
                    relocation_deliver.add(new Task(loadingDuration + driveTime_toRelocate, TaskType.PL, currentBox, vehicle, relocationStack));

                    if (i == 0) vehicle.driveTo(deliveryStack);
                    undoRelocation_deliver.add(new Task(loadingDuration + driveTime_toRelocate, TaskType.PU, currentBox, vehicle, relocationStack));

                    vehicle.driveTo(relocationStack);
                    driveTime_toPickup = 0;
                    driveTime_toRelocate = 0;
                }

                for (Task task : relocation_deliver) currentTime = schedule(task, currentTime);
            }
        }

        int driveTime = vehicle.getDriveTime(pickupStack);
        Task pickupTask = new Task(loadingDuration + driveTime, TaskType.PU, targetBox, vehicle, pickupStack);
        currentTime = schedule(pickupTask, currentTime);
        vehicle.driveTo(pickupStack);

        if (!pickupStack.getName().equals("BufferPoint")) {
            deliveryStack.push(targetBox);
        }

        driveTime = vehicle.getDriveTime(deliveryStack);
        Task deliverTask = new Task(loadingDuration + driveTime, TaskType.PL, targetBox, vehicle, deliveryStack);

        currentTime = schedule(deliverTask, currentTime);

        if (!undoRelocation_pickup.isEmpty()) {
            vehicle.driveTo(pickupStack);
        } else {
            vehicle.driveTo(deliveryStack);
        }

        for (Task task : undoRelocation_deliver) currentTime = schedule(task, currentTime);
        for (Task task : undoRelocation_pickup) currentTime = schedule(task, currentTime);

        vehicle.setCurrentTime(currentTime);
    }

    private BoxStack getRelocationStack(BoxStack pickupStack, BoxStack deliveryStack, int boxCounter) {
        int minDistance = Integer.MAX_VALUE;
        BoxStack ret = null;

        for (String key : boxStacks.keySet()) {
            BoxStack boxStack = boxStacks.get(key);

            if (boxStack == pickupStack || boxStack == deliveryStack) {
                continue;
            }

            int remainingPlaces = boxStack.getRemainingPlaces();
            if (remainingPlaces >= boxCounter) {
                int distance = calculateDistance(pickupStack, boxStack);
                if (distance < minDistance) {
                    minDistance = distance;
                    ret = boxStack;
                }
            }
        }
        return ret;
    }

    private int schedule(Task task, int startTime) {
        BoxStack stack = task.getStack();
        int endTimeStack = stack.getEndTime();
        int maxEndTime = Math.max(endTimeStack, startTime);
        task.setStartTime(maxEndTime);
        stack.schedule(task);

        if (tasksInOrder.isEmpty()) tasksInOrder.add(task);
        else {
            for (int i = tasksInOrder.size() - 1; i >= 0; i--) {
                if (tasksInOrder.get(i).getStartTime() < task.getStartTime()) {
                    tasksInOrder.add(i + 1, task);
                    break;
                }
            }
        }

        return maxEndTime + task.getDuration();
    }

    private int calculateDistance(BoxStack stack1, BoxStack stack2) {
        int x1 = stack1.getX();
        int y1 = stack1.getY();
        int x2 = stack2.getX();
        int y2 = stack2.getY();

        return (int) Math.ceil(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }

    private void printTasks() {
        for (Task task : tasksInOrder) task.print();
    }
}
