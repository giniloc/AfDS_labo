import java.util.List;
import java.util.Stack;

public class Scheduler {

    private int loadingDuration;
    private int vehicleSpeed;
    private int stackCapacity;

    private BoxStack[] boxStacks;
    private Buffer buffer;
    private Vehicle[] vehicles;
    private Stack<TransportRequest> requests;
    public Scheduler(int lo, int vesp, int stcap, BoxStack[] bs, Buffer bu, Vehicle[] ve, Stack<TransportRequest> re){
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
            TransportRequest request = requests.pop();
            Vehicle vehicle = findAvailableVehicle(request);

            if (vehicle == null) {
                continue;
            }
            for (String location : request.getPickupLocations()) {
                BoxStack stack = null;
                for (BoxStack boxStack : boxStacks) {
                    if (boxStack.getName().equals(location)) {
                        stack = boxStack;
                        break;
                    }
                }
                if (stack == null) {
                    System.out.println("Stack not found");
                    continue;
                }
                if (stack.isInUse()) {
                    System.out.println("Stack is in use");
                    continue;
                }
                if (stack.getBoxes().size() > stackCapacity) {
                    System.out.println("Stack is too big");
                    continue;
                }
                stack.setInUse(true);
                vehicle.addBoxStack(stack, stack.getBoxes());
            }
        }
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
}
