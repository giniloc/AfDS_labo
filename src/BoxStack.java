import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BoxStack {
    private int ID;
    private String name;
    private int x;
    private int y;
    private Stack<Box> boxes;

    private boolean inUse;
    private int capacity;


    public BoxStack(int ID, String name, int x, int y, int capacity) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.boxes = new Stack<>();
        this.inUse = false;
        this.capacity = capacity;
    }
    public void addBox(Box box) {
        boxes.push(box);
        System.out.println("Added box " + box.getBoxID() + " to stack " + this.name);
    }
    public void addBox(String boxID) {
        boxes.push(new Box(boxID));
        System.out.println("Added box " + boxID + " to stack " + this.name);
    }

    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void removeBox(Box box, Vehicle vehicle) {
        while (box != boxes.peek()){
            Box removedBox =boxes.pop();
            vehicle.addBox(removedBox);
            vehicle.setEndTime(vehicle.getEndTime() + vehicle.getLoadingDuration());
            System.out.println("Removed box " + removedBox.getBoxID() + " from stack " + this.name);
        }
        boxes.pop();
        vehicle.addBox(box);
        vehicle.setEndTime(vehicle.getEndTime() + vehicle.getLoadingDuration());
        System.out.println("Removed box " + box.getBoxID() + " from stack " + this.name);
    }
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean isInUse() {
        return inUse;
    }

    public int getIndex() {
        return 1;
    }

    public Box getBox(int index) {
        return boxes.get(index);
    }
    public int calculateBoxPosition(String box) {
        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).getBoxID().equals(box)){
                System.out.println("Box found in " + this.name+ " at position " + i);
                return i; // Return the position of the box in the stack
            }
        }
        return -1; // Return -1 if the box is not found in the stack
    }



    public int getCapacity() {
        return capacity;
    }
}
