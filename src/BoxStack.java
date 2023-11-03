import java.util.*;

public class BoxStack {
    private int ID;
    private String name;
    private int x;
    private int y;
    private Stack<String> boxes = new Stack<>();
    private Queue<Vehicle> vehicleQueue = new LinkedList<>();

    public BoxStack(int ID, String name, int x, int y) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void AddToQueue(Vehicle vehicle){
        vehicleQueue.add(vehicle);
    }

    public boolean IsFirstInQueue(Vehicle vehicle){
        return vehicleQueue.peek() == vehicle;
    }

    public void RemoveFirstInQueue(){
        vehicleQueue.poll();
    }

    public int GetInverseIndexOf(String box){
        return GlobalData.getStackCapacity() - boxes.indexOf(box);
    }

    public void addBox(String id){
        boxes.push(id);
    }

    public int AmountInQueue(){
        return vehicleQueue.size();
    }
}
