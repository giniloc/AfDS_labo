import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vehicle {
    private final int ID;
    private final String name;
    private final int capacity;

    private int x;
    private int y;
    private int currentTime;

    private static int vehicleSpeed;
    private static int loadingDuration;

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

    public int getDriveTime(BoxStack stack0, BoxStack stack1) {
        return Math.abs((stack1.getX() - stack0.getX()) + (stack1.getY() - stack0.getX())) / vehicleSpeed;
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

    public static int getVehicleSpeed() {
        return vehicleSpeed;
    }

    public static int getLoadingDuration() {
        return loadingDuration;
    }

    public static void setLoadingDuration(int loadingDuration) {
        Vehicle.loadingDuration = loadingDuration;
    }
}