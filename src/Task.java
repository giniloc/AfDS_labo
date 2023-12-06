import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Task {
    private int startTime = 0;
    private int duration;

    private TaskType type;
    private String box;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    private Vehicle vehicle;
    private BoxStack endStack;

    public Task(TaskType type, String box, Vehicle vehicle, BoxStack startStack, BoxStack endStack) {
        this.type = type;
        this.box = box;
        this.vehicle = vehicle;
        this.startX = startStack.getX();
        this.startY = startStack.getY();
        this.endX = endStack.getX();
        this.endY = endStack.getY();
        this.endStack = endStack;

        duration = calculateDriveTime(startX, startY, endX, endY) + Vehicle.getLoadingDuration();
    }

    public Task(TaskType type, String box, Vehicle vehicle, int x0, int y0, BoxStack endStack) {
        this.type = type;
        this.box = box;
        this.vehicle = vehicle;
        this.startX = x0;
        this.startY = y0;
        this.endX = endStack.getX();
        this.endY = endStack.getY();
        this.endStack = endStack;

        duration = calculateDriveTime(startX, startY, endX, endY) + Vehicle.getLoadingDuration();
    }

    public Task(TaskType type, String box, Vehicle vehicle, BoxStack stack) {
        this.type = type;
        this.box = box;
        this.vehicle = vehicle;
        this.startX = stack.getX();
        this.startY = stack.getY();
        this.endX = stack.getX();
        this.endY = stack.getY();
        this.endStack = stack;

        duration = Vehicle.getLoadingDuration();
    }

    private int calculateDriveTime(int x0, int y0, int x1, int y1){
        return Math.abs((x0 - x1) + (y0 - y1)) / Vehicle.getVehicleSpeed();
    }

    public String print(String inputName) {
        return vehicle.getName() + ";" +
                startX + ";" +
                startY + ";" + startTime + ";" +
                endX + ";" +
                endY + ";" +
                (startTime + duration) + ";" +
                box + ";" +
                type;
    }

    //Getters & Setters
    public int getEndTime(){
        return startTime+duration;
    }

    public int getDuration(){
        return duration;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public BoxStack getEndStack() {
        return endStack;
    }
}

enum TaskType{
    PU,
    PL
}
