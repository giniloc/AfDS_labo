public class VehicleWorker extends Thread {
    private Vehicle vehicle;
    private Scheduler scheduler;

    public VehicleWorker(Vehicle vehicle, Scheduler scheduler) {
        this.vehicle = vehicle;
        this.scheduler = scheduler;
    }


    @Override
    public void run() {
        while (true) {
            TransportRequest request = scheduler.getNextRequestForVehicle(vehicle);
            if (request == null) {
                // No more requests to process, exit the loop
                break;
            }

            // Process the request
            // Call methods in the Vehicle class to handle the request
        }
    }
}
