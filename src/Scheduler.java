import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
public class Scheduler {
    private ReentrantLock[] stackLocks;

    public static List<BoxStack> neededStacks = new ArrayList<>();
    private int loadingDuration;
    private final int vehicleSpeed;
    private final int stackCapacity; //dit is de capacity van de stack, niet van vehicle

    private List<BoxStack> boxStacks;
    private Buffer buffer;
    private List<Vehicle> vehicles;
    private Stack<TransportRequest> requests;

    private final ExecutorService executorService;

    public Scheduler(int lo, int vesp, int stcap, List<BoxStack> bs, Buffer bu, List<Vehicle> ve, Stack<TransportRequest> re, ExecutorService executorService) {
        this.loadingDuration = lo;
        this.vehicleSpeed = vesp;
        this.stackCapacity = stcap;
        this.boxStacks = bs;
        this.buffer = bu;
        this.vehicles = ve;
        this.requests = re;
        this.executorService = executorService;
        stackLocks = new ReentrantLock[boxStacks.size()];
        for (int i = 0; i < stackLocks.length; i++) {
            stackLocks[i] = new ReentrantLock();
        }
    }

    public void scheduleRequestsWithExecutorService() {
        ExecutorService executorService = Executors.newFixedThreadPool(vehicles.size());

        while (!requests.isEmpty()) {
            final TransportRequest request = requests.pop();

            executorService.execute(() -> {
                Vehicle vehicle = findAvailableVehicle(request);

                if (vehicle == null) {
                    System.out.println("No available vehicles");
                    return;
                }

                // Create a list to store the BoxStacks needed for this request

                for (String location : request.getPickupLocations()) {
                    BoxStack stack = findStackByName(location);
                    if (location.equals("BufferPoint")){
                        neededStacks.add(buffer);
                    }
                    else if (stack == null) {
                        System.out.println("Stack not found");
                        continue;
                    }

                    neededStacks.add(stack);
                }
                for (String location : request.getDeliveryLocations()) {
                    BoxStack stack = findStackByName(location);
                    if (location.equals("BufferPoint")){
                            neededStacks.add(buffer);
                        }
                    neededStacks.add(stack);
                }
                processRequest(request, vehicle);

                // Release the vehicle when done
                vehicle.setBusy(false);
            });
        }

        // Shutdown the executor when all tasks are complete
        executorService.shutdown();
    }

    private synchronized void processRequest(TransportRequest request, Vehicle vehicle) {
        BoxStack van = neededStacks.get(0);
        BoxStack naar = neededStacks.get(1);

        int boxPosition = van.calculateBoxPosition(request.getBoxID());
        if (vehicle.getCapacity() >= boxPosition && naar.getCapacity() >= boxPosition){ //mogelijk om alles in 1 keer te doen
            List<Box> removedBoxes= van.removeBox(van.getBox(boxPosition));
            for(Box box : removedBoxes){
                if (box.getBoxID().equals(request.getBoxID())){
                    naar.addBox(box);
                }
                else{
                    van.addBox(box);
                }
            }
        }
        else {// relocate dozen
            BoxStack closestFreeStack = getClosestFreeStack();
            closestFreeStack.addBox(request.getBoxID());

        }



        }



    private BoxStack findStackByName(String location) {
        for (BoxStack stack : boxStacks) {
            if (stack.getName().equals(location)) {
                return stack;
            }

        }
        return null;
    }


    private Vehicle findAvailableVehicle(TransportRequest request) {
        for (Vehicle vehicle : vehicles) {
            if (!vehicle.isBusy()) {
                vehicle.setBusy(true);
                return vehicle;
            }
        }
        System.out.println("No available vehicles");
        return null;
    }

    private int calculateTravelTime(Vehicle vehicle, List<String> pickupLocations) { //deze gingen we nog krijgen van de prof
        int travelTime = 0;
        int x = vehicle.getX();
        int y = vehicle.getY();

        for (String location : pickupLocations) {
            int x2 = Integer.parseInt(location.split(",")[0]);
            int y2 = Integer.parseInt(location.split(",")[1]);

            travelTime += (int) Math.ceil(Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2)) / vehicleSpeed);
            x = x2;
            y = y2;
        }
        return travelTime;
    }

    public synchronized TransportRequest getNextRequestForVehicle(Vehicle vehicle) {
        if (!requests.isEmpty()) {
            return requests.pop();
        }
        return null;
    }
    public BoxStack getClosestFreeStack(){
        int minDistance = Integer.MAX_VALUE;
        BoxStack closestStack = null;
        for (BoxStack stack : boxStacks) {
            if (!stack.isInUse()) {
                int distance = (int) Math.ceil(Math.sqrt(Math.pow(stack.getX() - buffer.getX(), 2) + Math.pow(stack.getY() - buffer.getY(), 2)));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestStack = stack;
                }
            }
        }
        if (closestStack != null) {
            closestStack.setInUse(true);
        }
        return closestStack;
    }
}
