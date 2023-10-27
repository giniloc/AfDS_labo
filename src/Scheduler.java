import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
public class Scheduler {
    private List<BoxStack> neededStacks = new ArrayList<>();
    private int loadingDuration;
    private final int vehicleSpeed;
    private final int stackCapacity;
    private List<BoxStack> boxStacks;
    private Buffer buffer;
    private List<Vehicle> vehicles;
    private Stack<TransportRequest> requests;

    public Scheduler(int lo, int vesp, int stcap, List<BoxStack> bs, Buffer bu, List<Vehicle> ve, Stack<TransportRequest> re) {
        this.loadingDuration = lo;
        this.vehicleSpeed = vesp;
        this.stackCapacity = stcap;
        this.boxStacks = bs;
        this.buffer = bu;
        this.vehicles = ve;
        this.requests = re;
    }

    public void scheduleRequests() {
        while (!requests.isEmpty()) {
            TransportRequest request = requests.peek();

            if (request == null) {
                break; // No more requests, exit the loop.
            }

            Vehicle vehicle = findAvailableVehicle(request);

            if (vehicle == null) {
                System.out.println("No available vehicles");
                break; // No available vehicles, exit the loop.
            }

            for (String location : request.getPickupLocations()) {
                BoxStack stack = findStackByName(location);
                if (location.equals("BufferPoint")) {
                    neededStacks.add(buffer);
                } else if (stack == null) {
                    System.out.println("Stack not found");
                    continue;
                }
                neededStacks.add(stack);
            }

            for (String location : request.getDeliveryLocations()) {
                BoxStack stack = findStackByName(location);
                if (location.equals("BufferPoint")) {
                    neededStacks.add(buffer);
                }
                neededStacks.add(stack);
            }

            processRequest(request, vehicle);
            vehicle.setBusy(false);
            requests.pop(); // Remove the completed request from the queue.
            neededStacks.clear();
        }
    }

    private void processRequest(TransportRequest request, Vehicle vehicle) {
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
