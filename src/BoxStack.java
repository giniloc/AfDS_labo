import java.util.*;

public class BoxStack {
    private final int ID;
    private final String name;
    private final int x;
    private final int y;
    private Stack<String> boxes = new Stack<>();
    private List<Task> schedule = new ArrayList<>(64);

    private static int stackCapacity;

    private static Vehicle vehicle;

    public BoxStack(int ID, String name, int x, int y) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.vehicle = null;
    }

    public void addBox(String box){
        boxes.add(box);
    }

    //Methods on boxes stack
    public String pop(){
        return boxes.pop();
    }

    public void push(String box){
        boxes.push(box);
    }

    //Methods on schedule
    public int getEndTime(){
        if (schedule.isEmpty()) return 0;
        else return schedule.get(schedule.size()-1).getEndTime();
    }

    public void schedule(Task task){
        schedule.add(task);
    }

    //Getters & Setters
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static void setStackCapacity(int stackCapacity) {
        BoxStack.stackCapacity = stackCapacity;
    }

    public static int getStackCapacity() {
        return stackCapacity;
    }

    public int getRemainingPlaces() {
        return stackCapacity-boxes.size();
    }

    public static Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
