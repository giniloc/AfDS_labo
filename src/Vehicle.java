import java.util.List;

public class Vehicle {
    private int ID;
    private String name;
    private int capacity;
    private int x;
    private int y;
    private int startTime;
    private int endTime;

    private boolean isBusy;

    public Vehicle(int id, String n, int capacity, int xc, int yc) {
        this.ID = id;
        this.name = n;
        this.capacity = capacity;
        this.x = xc;
        this.y = yc;
        this.startTime=0;
        this.endTime=0;
        this.isBusy=false;
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

    public void addBoxStack(BoxStack stack, List<Box> boxes) {
        System.out.println("Adding boxes from stack: " + stack.getName() + " to vehicle: " + this.name);

        for (Box box : boxes) {
            System.out.println("Adding box with ID: " + box.getBoxID() + " to vehicle: " + this.name);
            // You can print any other details of the box here.
        }
    }

    public void addBox(Box box) {
        System.out.println("Adding box with ID: " + box.getBoxID() + " to vehicle: " + this.name);
        // You can print any other details of the box here.
    }
}