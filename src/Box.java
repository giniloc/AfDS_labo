public class Box {
    private String name;
    private BoxStack deliveryStack;

    public Box(String name){
        this.name = name;
    }

    //Getters & setters
    public String getName() {
        return name;
    }

    public void setDeliveryStack(BoxStack stack){
        deliveryStack = stack;
    }

    public BoxStack getDeliveryStack(){
        return deliveryStack;
    }
}
