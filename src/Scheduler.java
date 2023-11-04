import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class Scheduler {
    private List<BoxStack> neededStacks = new ArrayList<>();
    private final int vehicleSpeed;
    private final int stackCapacity;
    private List<BoxStack> boxStacks;
    private Buffer buffer;
    private List<Vehicle> vehicles;
    private Stack<TransportRequest> requests;

    public Scheduler(int lo, int vesp, int stcap, List<BoxStack> bs, Buffer bu, List<Vehicle> ve, Stack<TransportRequest> re) {
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
            Vehicle vehicle = findAvailableVehicle();

            if (vehicle == null) {
                System.out.println("No available vehicles");
                break; // No available vehicles, exit the loop.
            }

            for (String location : request.getPickupLocations()) {
                BoxStack stack = findStackByName(location);
                if (location.equals("BufferPoint")) {
                    neededStacks.add(buffer);
                }
                else if (stack == null) {
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
        if (van.getName().equals("BufferPoint")) {
        }
        else van.setInUse(true);
        BoxStack naar = neededStacks.get(1);
        if (naar.getName().equals("BufferPoint")){}
        else naar.setInUse(true);

        int boxPosition = van.calculateBoxPosition(request.getBoxID());
        if (vehicle.getCapacity() >= boxPosition && stackCapacity >= boxPosition) {// mogelijk om boxes in 1 keer te verplaatsen
            van.removeBox(van.getBox(boxPosition), vehicle);
            addTravelTime(vehicle, van);

            for (Box box : vehicle.getBoxes()) {
                if (box.getBoxID().equals(request.getBoxID())) {
                    addTravelTime(vehicle, naar);
                    naar.addBox(box);
                    vehicle.removeBox(box);
                    break;
                }
            }
            addTravelTime(vehicle, van);
            while (!vehicle.getBoxes().empty()) {
                Box box = vehicle.getBoxes().pop();
                van.addBox(box);
                vehicle.removeBox(box);
            }
        } else {
            BoxStack closestFreeStack = getClosestFreeStack(vehicle, boxPosition);

            closestFreeStack.addBox(request.getBoxID());

            // Update the vehicle's new position
            vehicle.setX(closestFreeStack.getX());
            vehicle.setY(closestFreeStack.getY());
        }
        van.setInUse(false);
        naar.setInUse(false);

        int endX = vehicle.getX();
        int endY = vehicle.getY();

        // Display the information
        System.out.println("StartX: " + startX);
        System.out.println("StartY: " + startY);
        System.out.println("EndX: " + endX);
        System.out.println("EndY: " + endY);
        System.out.println("Start time: " + vehicle.getStartTime());
        System.out.println("End time: " + vehicle.getEndTime());
        vehicle.setStartTime(vehicle.getEndTime());
    }

    private BoxStack findStackByName(String location) {
        for (BoxStack stack : boxStacks) {
            if (stack.getName().equals(location)) {
                return stack;
            }
        }
        return null;
    }


    private Vehicle findAvailableVehicle() {
        for (Vehicle vehicle : vehicles) {
            if (!vehicle.isBusy()) {
                vehicle.setBusy(true);
                return vehicle;
            }
        }
        System.out.println("No available vehicles");
        return null;
    }



    public TransportRequest getNextRequestForVehicle(Vehicle vehicle) {
        if (!requests.isEmpty()) {
            return requests.pop();
        }
        return null;
    }
    private void addTravelTime(Vehicle vehicle, BoxStack stack) {
        int x1 = vehicle.getX();
        int y1 = vehicle.getY();
        int x2 = stack.getX();
        int y2 = stack.getY();

        // Calculate the Euclidean distance between the current location and the stack
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // Calculate the travel time using the vehicle's speed
        int travelTime = (int) Math.ceil(distance / vehicleSpeed);
        vehicle.setEndTime(vehicle.getEndTime() + travelTime);
        vehicle.setX(stack.getX());
        vehicle.setY(stack.getY());
    }
    public BoxStack getClosestFreeStack(Vehicle vehicle, int neededCapacity) {
        int minDistance = Integer.MAX_VALUE;
        BoxStack closestStack = null;

        for (BoxStack stack : boxStacks) {
            if (!stack.isInUse() && stack.getCapacity() >= neededCapacity) {
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

}