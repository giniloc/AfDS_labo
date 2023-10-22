public class VehicleWorker implements Runnable {
    private final Vehicle vehicle;
    private final Scheduler scheduler;

    public VehicleWorker(Vehicle vehicle, Scheduler scheduler) {
        this.vehicle = vehicle;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while (true) {
            TransportRequest request = scheduler.getNextRequestForVehicle(vehicle);

            if (request == null) {
                // No more requests for this vehicle, exit the thread
                break;
            }

            // Handle the request with the vehicle
            // Implement the logic to load boxes, rearrange, etc.
        }
    }
}
