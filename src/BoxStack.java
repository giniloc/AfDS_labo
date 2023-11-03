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

    //VehicleQueue methods
    public void AddToQueue(Vehicle vehicle){
        vehicleQueue.add(vehicle);
    }

    public boolean IsFirstInQueue(Vehicle vehicle){
        return vehicleQueue.peek() == vehicle;
    }

    public void RemoveFirstInQueue(){
        vehicleQueue.poll();
    }

    public int AmountInQueue(){
        return vehicleQueue.size();
    }

    //BoxStack methods
    public int GetDepth(String box){
        return boxes.size() - boxes.indexOf(box);
    }

    public void addBox(String id){
        boxes.push(id);
    }

    public String Pop(){
        return boxes.pop();
    }

    public void Push(String box){
        boxes.push(box);
    }

    public int GetStackSize(){
        return boxes.size();
    }

    public String GetFromStack(int index){
        return boxes.get(index);
    }

    //getters & setters
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
