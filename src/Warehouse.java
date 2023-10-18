import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private List<Vehicle> vehicles;
    private List<BoxStack> stacks;
    private List<TransportRequest> requests;

    public Warehouse(List<Vehicle> vehicles, List<BoxStack> stacks) {
        this.vehicles = vehicles;
        this.stacks = stacks;
        this.requests = new ArrayList<>();
    }

    public void addRequest(TransportRequest request) {
        requests.add(request);
    }

    public void assignRequestsToVehicles() {
        // logic to assign requests to vehicles
    }

    public void handleRequests() {
        // logic to handle each request
    }

}
