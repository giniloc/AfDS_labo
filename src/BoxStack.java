import java.util.*;

public class BoxStack {
    private int ID;
    private final String name;
    private final int x;
    private final int y;
    private Stack<Box> boxes = new Stack<>();
    private int endTime = 0;

    private static int stackCapacity;
    private int placeLeft;

    private int lastPickupIndex = Integer.MAX_VALUE;
    private int amountPickupBoxes = 0;
    private boolean hasRequests = false;

    public BoxStack(int ID, String name, int x, int y) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
        placeLeft = stackCapacity;
    }

    //Methods on boxes stack
    public Box pop(){
        return boxes.pop();
    }

    public void push(Box box){
        if (boxes.size() >= stackCapacity) System.out.println("stack overflowing");
        boxes.push(box);
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
        return placeLeft;
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
}
