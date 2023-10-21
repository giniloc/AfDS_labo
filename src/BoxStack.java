import java.util.ArrayList;
import java.util.List;

public class BoxStack {
    private int ID;
    private String name;
    private int x;
    private int y;
    private List<Box> boxes;

    private boolean inUse;


    public BoxStack(int ID, String name, int x, int y) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.boxes = new ArrayList<>();
        this.inUse = false;
    }
    public boolean isInUse() {
        return inUse;
    }
    // Add a method to add boxes to the list
    public void addBox(String boxID) {
        boxes.add(new Box(boxID));
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
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

    public List<Box> getBoxes() {
        return boxes;
    }


    public void removeBox(Box box) {
        boxes.remove(box);
    }
}
