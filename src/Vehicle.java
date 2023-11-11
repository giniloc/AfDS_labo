public class Vehicle {
    private final int ID;
    private final String name;
    private final int capacity;

    private int x;
    private int y;
    private int currentTime;

    private static int vehicleSpeed;

    public Vehicle(int id, String n, int capacity, int xc, int yc) {
        this.ID = id;
        this.name = n;
        this.capacity = capacity;
        this.x = xc;
        this.y = yc;
    }

    public int getDriveTime(BoxStack stack){
        return Math.abs((stack.getX() - x) + (stack.getY() - y)) / vehicleSpeed;
    }

    public void driveTo(BoxStack stack){
        x = stack.getX();
        y = stack.getY();
    }

    //Getters & Setters
    public int getCurrentTime() {
        return currentTime;
    }

    public static void setVehicleSpeed(int vehicleSpeed) {
        Vehicle.vehicleSpeed = vehicleSpeed;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}