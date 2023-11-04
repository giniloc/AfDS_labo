import java.util.Stack;


public class Vehicle {
    private int ID;
    private String name;
    private final int capacity;
    private int x;
    private int y;
    private int startTime;
    private int endTime;
    private int loadingDuration;

    private boolean isBusy;
    private Stack <Box> boxes;

    public Vehicle(int id, String n, int capacity,int loadingDuration, int xc, int yc) {
        this.ID = id;
        this.name = n;
        this.capacity = capacity;
        this.x = xc;
        this.y = yc;
        this.startTime=0;
        this.endTime=0;
        this.loadingDuration=loadingDuration;
        this.isBusy=false;
        this.boxes = new Stack<>();

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getID() {
        return ID;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }
    public void addBox(Box box) {
        boxes.push(box);
        System.out.println("Added box " + box.getBoxID() + " to vehicle " + this.name);
    }
    public void removeBox(Box box) {
        boxes.remove(box);
        System.out.println("Removed box " + box.getBoxID() + " from vehicle " + this.name);
    }

    public int getLoadingDuration() {
        return loadingDuration;
    }

    public Stack<Box> getBoxes() {
        return boxes;
    }

    public int getStartTime() {
        return startTime;
    }
}