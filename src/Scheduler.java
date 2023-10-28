import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
                if (location.equals("BufferPoint")) {
                    neededStacks.add(buffer);
                }
            }

            processRequest(request, vehicle);
            vehicle.setBusy(false);
            requests.pop(); // Remove the completed request from the queue.
            neededStacks.clear();
        }
    }


    public void processRequest(TransportRequest request, Vehicle vehicle) {
        int startX = vehicle.getX();
        int startY = vehicle.getY();

        BoxStack van = neededStacks.get(0);
        BoxStack naar = neededStacks.get(1);

        int boxPosition = van.calculateBoxPosition(request.getBoxID());
        if (vehicle.getCapacity() >= boxPosition && stackCapacity >= boxPosition) {
            // PICKUP action
            List<Box> removedBoxes = van.removeBox(van.getBox(boxPosition));
            for (Box box : removedBoxes) {
                if (box.getBoxID().equals(request.getBoxID())) {
                    naar.addBox(box);
                    removedBoxes.remove(box);
                    logAction(vehicle, "Picked up", box.getBoxID());
                    break;
                }
            }
            for (Box box : removedBoxes) {
                van.addBox(box);
            }

            // Update the vehicle's new position
            vehicle.setX(van.getX());
            vehicle.setY(van.getY());
        } else {
            // PLACE action
            BoxStack closestFreeStack = getClosestFreeStack(vehicle);
            closestFreeStack.addBox(request.getBoxID());
            logAction(vehicle, "Placed", request.getBoxID());

            // Update the vehicle's new position
            vehicle.setX(closestFreeStack.getX());
            vehicle.setY(closestFreeStack.getY());
        }

        int endX = vehicle.getX();
        int endY = vehicle.getY();

        // Display the information
        System.out.println("StartX: " + startX);
        System.out.println("StartY: " + startY);
        System.out.println("EndX: " + endX);
        System.out.println("EndY: " + endY);
    }

    private void logAction(Vehicle vehicle, String action, String boxID) {
        System.out.println("Vehicle " + vehicle.getID() + " " + action + " box " + boxID);
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

    public TransportRequest getNextRequestForVehicle(Vehicle vehicle) {
        if (!requests.isEmpty()) {
            return requests.pop();
        }
        return null;
    }
    public BoxStack getClosestFreeStack(Vehicle vehicle) {
        int minDistance = Integer.MAX_VALUE;
        BoxStack closestStack = null;

        for (BoxStack stack : boxStacks) {
            if (!stack.isInUse()) {
                int distance = calculateDistance(vehicle, stack);
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
    private int calculateDistance(Vehicle vehicle, BoxStack stack) {
        int x1 = vehicle.getX();
        int y1 = vehicle.getY();
        int x2 = stack.getX();
        int y2 = stack.getY();

        return (int) Math.ceil(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }
    public enum action {
        PICKUP, PLACE
    }
}

