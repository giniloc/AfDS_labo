import java.util.*;

public class Buffer extends BoxStack {
    private int ID;
    private String name;
    private int x;
    private int y;

    private Map<String, LinkedList<Box>> pickupBoxesPerStack = new HashMap<>();
    private boolean hasRequests = false;

    public Buffer(int ID, String name, int x, int y) {
        super(ID, name, x, y);
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y; 
    }

    public String getPickupKey(){
        Iterator<String> iter = pickupBoxesPerStack.keySet().iterator();
        if (iter.hasNext()){
            return iter.next();
        }
        else{
            return "";
        }
    }

    // Getters and setters for the class attributes
    public int getX() {
        return x;
    }

    @Override
    public int getEndTime(){
        return 0;
    }

    public int getY() {
        return y;
    }

    public void addPickupBox(String stackName, Box box){
        hasRequests = true;
        if (!pickupBoxesPerStack.containsKey(stackName)){
            pickupBoxesPerStack.put(stackName, new LinkedList<Box>());
        }
        pickupBoxesPerStack.get(stackName).add(box);
    }

    public void addStack(String name){
        pickupBoxesPerStack.put(name, new LinkedList<>());
    }

    public LinkedList<Box> getDeliveryBoxes(String key){
        return pickupBoxesPerStack.get(key);
    }

    public void removeKey(String key){
        pickupBoxesPerStack.remove(key);
    }

    public boolean hasRequests() {
        return hasRequests;
    }
}
