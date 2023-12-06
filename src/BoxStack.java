import java.util.*;

public class BoxStack {
    private int ID;
    private final String name;
    private final int x;
    private final int y;
    private Stack<Box> boxes = new Stack<>();
    private int endTime = 0;

    private static int stackCapacity;

    private int lastPickupIndex = Integer.MAX_VALUE;
    private int amountPickupBoxes = 0;
    private boolean hasRequests = false;
    private int amountDeliveryBoxes = 0; //=boxes die van eender welke buffer naar hier moeten worden gebracht

    public BoxStack(int ID, String name, int x, int y) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    //Methods on boxes stack
    public Box pop(){
        Box box = boxes.pop();
        System.out.println(name + ": placeLeft: " + getPlaceLeft() + ", box: " + box.getName() + ", pop");
        return box;
    }

    public void push(Box box){
        boxes.push(box);
        System.out.println(name + ": placeLeft: " + getPlaceLeft() + ", box: " + box.getName() + ", push");
    }

    //Methods on pickupBoxes
    public void addPickupBox(String boxName, BoxStack deliveryStack){
        hasRequests = true;
        amountPickupBoxes++;
        for (int i = 0; i < boxes.size(); i++){
            if (boxName.equals(boxes.get(i).getName())){
                if (i < lastPickupIndex) lastPickupIndex = i;
                boxes.get(i).setDeliveryStack(deliveryStack);
            }
        }
    }

    //Getters & Setters
    public int getLastPickupIndex() {
        return lastPickupIndex;
    }

    public String getName() {
        return name;
    }

    public Stack<Box> getBoxes() {
        return boxes;
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

    public int getPlaceLeft() {
        return stackCapacity - boxes.size();
    }

    public int getAmountRelocationBoxes(){
        return (boxes.size() - getLastPickupIndex()) - amountPickupBoxes;
    }

    public int boxesSize(){
        return boxes.size();
    }

    public int getEndTime(){
        return endTime;
    }

    public void setEndTime(int time){
        endTime = time;
    }

    public boolean hasRequests() {
        return hasRequests;
    }

    public void addDeliveryBox(){
        amountDeliveryBoxes++;
    }

    public int getAmountDeliveryBoxes(){
        return amountDeliveryBoxes;
    }
}
