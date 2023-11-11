import java.util.*;

public class Scheduler {

    private final Vehicle[] vehicles;
    private final Queue<Request> requests; //TODO: idk if this can be final
    private final int loadingDuration;
    private final Map<String, BoxStack> boxStacks;
    private List<Task> tasksInOrder = new ArrayList<>(1024);

    public Scheduler(Vehicle[] vehicles, Queue<Request> requests, int loadingDuration, Map<String, BoxStack> boxStacks) {
        this.vehicles = vehicles;
        this.requests = requests;
        this.loadingDuration = loadingDuration;
        this.boxStacks = boxStacks;
    }

    //TODO: relocation only to 1 stack possible now
    public void Start(){
        while(!requests.isEmpty()){
            for (Vehicle vehicle : vehicles){
                Request request = requests.poll();
                assert request != null;
                BoxStack pickupStack = request.getPickupLocation();
                BoxStack deliveryStack = request.getDeliveryLocation();
                String targetBox = request.getBoxID();
                int currentTime = vehicle.getCurrentTime();

                //Create relocation & undo-relocation tasks -> add relocation tasks
                List<Task> undoRelocation_pickup = new ArrayList<>(64);
                List<Task> undoRelocation_deliver = new ArrayList<>(64);
                if (!pickupStack.getName().equals("BufferPoint")){
                    String currentBox = "";
                    while (!currentBox.equals(targetBox)){
                        List<Task> relocation_deliver = new ArrayList<>(16);
                        int driveTime_toPickup = vehicle.getDriveTime(pickupStack);
                        int driveTime_toRelocate = 0;
                        for (int i = 0; i < vehicle.getCapacity(); i++){
                            currentBox = pickupStack.pop();
                            if (currentBox.equals(targetBox)) break;

                            BoxStack relocationStack = getRelocationStack(pickupStack, deliveryStack);

                            //Pickup & undo
                            Task newTask = new Task(loadingDuration+driveTime_toPickup, TaskType.PU, currentBox, vehicle, pickupStack);
                            currentTime = schedule(newTask, currentTime);
                            if (i == 0){
                                vehicle.driveTo(pickupStack);
                                driveTime_toRelocate = vehicle.getDriveTime(relocationStack);
                                vehicle.driveTo(relocationStack);
                            }
                            undoRelocation_pickup.add(new Task(loadingDuration+driveTime_toRelocate, TaskType.PL, currentBox, vehicle, pickupStack));

                            //Deliver & undo
                            if (i == 0) vehicle.driveTo(pickupStack);
                            relocation_deliver.add(new Task(loadingDuration+driveTime_toRelocate, TaskType.PL, currentBox, vehicle, relocationStack));
                            if (i == 0) vehicle.driveTo(deliveryStack);
                            undoRelocation_deliver.add(new Task(loadingDuration+driveTime_toRelocate, TaskType.PU, currentBox, vehicle, relocationStack));

                            vehicle.driveTo(relocationStack);
                            driveTime_toPickup = 0;
                            driveTime_toRelocate = 0;
                        }
                        for (Task task : relocation_deliver) currentTime = schedule(task, currentTime);

                    }
                }

                //Create pickup target box task -> add
                int driveTime = vehicle.getDriveTime(pickupStack);
                Task pickup_task = new Task(loadingDuration+driveTime, TaskType.PU, targetBox, vehicle, pickupStack);
                currentTime = schedule(pickup_task, currentTime);
                vehicle.driveTo(pickupStack);

                //Create deliver target box task -> add
                if (!pickupStack.getName().equals("BufferPoint")) deliveryStack.push(targetBox);
                driveTime = vehicle.getDriveTime(deliveryStack);
                Task deliver_task = new Task(loadingDuration+driveTime, TaskType.PL, targetBox, vehicle, deliveryStack);
                currentTime = schedule(deliver_task, currentTime);
                if (!undoRelocation_pickup.isEmpty()){
                    vehicle.driveTo(pickupStack);
                }
                else vehicle.driveTo(deliveryStack);

                for (Task task : undoRelocation_deliver) currentTime = schedule(task, currentTime);
                for (Task task : undoRelocation_pickup) currentTime = schedule(task, currentTime);

                vehicle.setCurrentTime(currentTime);
            }
        }
    }

    private BoxStack getRelocationStack(BoxStack pickupStack, BoxStack deliveryStack){
        int placeLeft_best = 0;
        BoxStack ret = null;
        for (String key : boxStacks.keySet()){
            BoxStack boxStack = boxStacks.get(key);
            if (boxStack == pickupStack || boxStack == deliveryStack) break;
            int placeLeft = boxStack.getPlaceLeft();
            if (placeLeft > placeLeft_best){
                placeLeft_best = placeLeft;
                ret = boxStack;
                if (placeLeft == BoxStack.getStackCapacity()) break;
            }
        }
        return ret;
    }

    private int schedule(Task task, int startTime){
        //returns current time of vehicle
        BoxStack stack = task.getStack();
        int endTimeStack = stack.getEndTime();
        //System.out.println(endTimeStack + ", " + startTime);
        int maxEndTime = Math.max(endTimeStack, startTime);
        task.setStartTime(maxEndTime);
        stack.schedule(task);
        task.print();
        return maxEndTime+task.getDuration();
    }
}
