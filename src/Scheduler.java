import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Scheduler {

    private final Vehicle[] vehicles;
    private final Map<String, BoxStack> boxStacks;
    private final Map<String, Buffer> buffers;
    private final Map<String, BoxStack> stacksNBuffers;
    private final LinkedList<BoxStack> boxStacksNotUsed = new LinkedList<>();
    private final LinkedList<Buffer> buffersNotUsed = new LinkedList<>();
    private final List<Task> tasksInOrder = new ArrayList<>(1024);
    private String inputFileName;

    public Scheduler(Vehicle[] vehicles, Map<String, BoxStack> boxStacks, Map<String, Buffer> buffers, String inputFileName) {
        this.vehicles = vehicles;
        this.boxStacks = boxStacks;
        this.buffers = buffers;
        stacksNBuffers = new HashMap<>();
        stacksNBuffers.putAll(boxStacks);
        stacksNBuffers.putAll(buffers);
        this.inputFileName = inputFileName;
    }

    public void preProcess(){
        for (String key : boxStacks.keySet()){
            if (boxStacks.get(key).hasRequests()){
                boxStacksNotUsed.add(boxStacks.get(key));
            }
        }
        for (String key : buffers.keySet()){
            if (buffers.get(key).hasRequests()){
                buffersNotUsed.add(buffers.get(key));
            }
        }
    }

    public void Start(){
        handleStacks();
        handleBuffers();
        printTasks(inputFileName);
    }

    private void handleStacks(){
        //stacks -> buffer
        while(!boxStacksNotUsed.isEmpty()){
            BoxStack stack = boxStacksNotUsed.pop();
            if (stack == null) break;
            Vehicle vehicle = getBestVehicle(stack);

            int lastPickupIndex = stack.getLastPickupIndex();
            int amountRelocationBoxes = stack.getAmountRelocationBoxes();
            List<BoxStack> relocationStacks = getRelocationStacks(amountRelocationBoxes, stack, vehicle);
            int iterations = stack.boxesSize() - lastPickupIndex;
            int vehicleCapacity = vehicle.getCapacity();
            int currentTime = vehicle.getCurrentTime();

            int currentRelocationStack = 0;
            int amountRelocations = 0;
            Map<String, List<Box>> boxesPerDeliveryLocation = new HashMap<>();

            for (int i = 0; i < iterations; i++){
                Box newBox = stack.pop();

                //pickup
                Task pickupTask;
                if (vehicleCapacity == vehicle.getCapacity()) pickupTask = new Task(TaskType.PU, newBox.getName(), vehicle, vehicle.getX(), vehicle.getY(), stack);
                else pickupTask = new Task(TaskType.PU, newBox.getName(), vehicle, stack);
                currentTime = schedule(pickupTask, currentTime);
                vehicleCapacity--;

                //place
                if (newBox.getDeliveryStack() != null){
                    BoxStack deliveryStack = newBox.getDeliveryStack();
                    if (!boxesPerDeliveryLocation.containsKey(deliveryStack.getName())){
                        boxesPerDeliveryLocation.put(deliveryStack.getName(), new ArrayList<>());
                    }
                    boxesPerDeliveryLocation.get(deliveryStack.getName()).add(newBox);
                }
                else{
                    amountRelocations++;
                    BoxStack deliveryStack = relocationStacks.get(currentRelocationStack);
                    if (!boxesPerDeliveryLocation.containsKey(deliveryStack.getName())){
                        boxesPerDeliveryLocation.put(deliveryStack.getName(), new ArrayList<>());
                    }
                    boxesPerDeliveryLocation.get(deliveryStack.getName()).add(newBox);

                    if (amountRelocations >= deliveryStack.getPlaceLeft()){
                        currentRelocationStack++;
                        amountRelocations = 0;
                    }
                }

                if (vehicleCapacity == 0 || i == iterations-1){
                    String previousKey = null;
                    int startX = stack.getX();
                    int startY = stack.getY();
                    for (String key : boxesPerDeliveryLocation.keySet()){
                        if (previousKey != null){
                            BoxStack previousStack = stacksNBuffers.get(previousKey);
                            startX = previousStack.getX();
                            startY = previousStack.getY();
                        }
                        previousKey = key;
                        BoxStack thisStack = stacksNBuffers.get(key);
                        for (Box box : boxesPerDeliveryLocation.get(key)){
                            Task newTask = new Task(TaskType.PL, box.getName(), vehicle, startX, startY, thisStack);
                            currentTime = schedule(newTask, currentTime);
                            thisStack.push(box);
                            startX = thisStack.getX();
                            startY = thisStack.getY();
                        }
                    }
                    vehicle.driveTo(stacksNBuffers.get(previousKey));

                    //reset
                    vehicleCapacity = vehicle.getCapacity();
                    boxesPerDeliveryLocation.clear();
                }
            }
            vehicle.setCurrentTime(currentTime);
        }
    }

    //TODO: zou kunnen da ge moet relocaten
    private void handleBuffers(){
        //buffers -> stacks
        while(!buffersNotUsed.isEmpty()) {
            Buffer buffer = buffersNotUsed.getFirst();
            Vehicle vehicle = getBestVehicle(buffer);
            String currentDeliveryLocation_name = buffer.getPickupKey();
            if (currentDeliveryLocation_name.isEmpty()) continue;
            LinkedList<Box> currentBoxes = buffer.getDeliveryBoxes(currentDeliveryLocation_name);
            boolean newBuffer = false;

            Map<String, List<Box>> boxesPerDeliveryLocation = new HashMap<>();
            int vehicleCapacity = vehicle.getCapacity();

            for (int i = 0; i < vehicleCapacity; i++){
                while (currentBoxes.isEmpty()){
                    buffer.removeKey(currentDeliveryLocation_name);
                    currentDeliveryLocation_name = buffer.getPickupKey();
                    if (currentDeliveryLocation_name.isEmpty()){
                        newBuffer = true;
                        break;
                    }
                    else{
                        currentBoxes = buffer.getDeliveryBoxes(currentDeliveryLocation_name);
                    }
                }
                if (newBuffer){
                    newBuffer = false;
                    buffersNotUsed.remove(0);
                    break;
                }


                //pickup
                Box box = currentBoxes.poll();
                Task pickupTask = new Task(TaskType.PU, box.getName(), vehicle, vehicle.getX(), vehicle.getY(), buffer);
                vehicle.driveTo(buffer);
                vehicle.setCurrentTime(schedule(pickupTask, vehicle.getCurrentTime()));

                //place
                if (!boxesPerDeliveryLocation.containsKey(currentDeliveryLocation_name)){
                    boxesPerDeliveryLocation.put(currentDeliveryLocation_name, new ArrayList<>());
                }
                boxesPerDeliveryLocation.get(currentDeliveryLocation_name).add(box);
            }

            //relocating: ...

            if (!boxesPerDeliveryLocation.isEmpty()){
                String previousKey = null;
                int startX = buffer.getX();
                int startY = buffer.getY();
                for (String key : boxesPerDeliveryLocation.keySet()){
                    if (previousKey != null){
                        BoxStack previousBuffer = stacksNBuffers.get(previousKey);
                        startX = previousBuffer.getX();
                        startY = previousBuffer.getY();
                    }
                    previousKey = key;
                    BoxStack thisBuffer = stacksNBuffers.get(key);
                    for (Box box : boxesPerDeliveryLocation.get(key)){
                        Task newTask = new Task(TaskType.PL, box.getName(), vehicle, startX, startY, thisBuffer);
                        vehicle.setCurrentTime(schedule(newTask, vehicle.getCurrentTime()));
                        thisBuffer.push(box);
                        startX = thisBuffer.getX();
                        startY = thisBuffer.getY();
                    }
                }
                vehicle.driveTo(stacksNBuffers.get(previousKey));

                //reset
                boxesPerDeliveryLocation.clear();
            }
        }
    }

    //TODO: veranderen naar getBestStack voor een vehicle. Daarvoor gaat ge wss datatype van stacks en buffers moeten aanpassen.
    private Vehicle getBestVehicle(BoxStack stack){
        Vehicle bestVehicle = null;
        float lowestCost = Integer.MAX_VALUE;
        for (Vehicle vehicle : vehicles){
            float cost = 0;
            cost += vehicle.getDriveTime(stack);
            cost += Math.abs(vehicle.getCurrentTime() - stack.getEndTime());
            if (cost < lowestCost){
                lowestCost = cost;
                bestVehicle = vehicle;
            }
        }
        return bestVehicle;
    }

    private List<BoxStack> getRelocationStacks(int totalAmount, BoxStack myStack,Vehicle vehicle){
        List<BoxStack> relocationStacks = new ArrayList<>(4);
        int currentAmount = 0;

        while(totalAmount > currentAmount){
            int lowestCost = Integer.MAX_VALUE;
            BoxStack bestStack = null;
            for (String key : boxStacks.keySet()){
                BoxStack stack = boxStacks.get(key);
                if (stack.equals(myStack) || relocationStacks.contains(stack)){
                    continue;
                }
                int cost = 0;
                cost += Math.abs(stack.getEndTime() - vehicle.getCurrentTime());
                cost += vehicle.getDriveTime(stack);
                cost -= stack.getPlaceLeft() * 5;
                if (cost < lowestCost){
                    lowestCost = cost;
                    bestStack = stack;
                }
            }
            assert bestStack != null;
            currentAmount += bestStack.getPlaceLeft();
            relocationStacks.add(bestStack);
        }

        return relocationStacks;
    }

    private int schedule(Task task, int startTime) {
        BoxStack stack = task.getEndStack();
        int endTimeStack = stack.getEndTime();
        int maxEndTime = Math.max(endTimeStack, startTime);
        task.setStartTime(maxEndTime);
        stack.setEndTime(maxEndTime);

        if (tasksInOrder.isEmpty()) tasksInOrder.add(task);
        else {
            for (int i = tasksInOrder.size() - 1; i >= 0; i--) {
                if (tasksInOrder.get(i).getStartTime() <= task.getStartTime()) {
                    tasksInOrder.add(i + 1, task);
                    return maxEndTime + task.getDuration();
                }
            }
            tasksInOrder.add(0, task);
        }

        return maxEndTime + task.getDuration();
    }

    private void printTasks(String inputFileName) {
        for (Task task : tasksInOrder) task.print(inputFileName);
    }

}
