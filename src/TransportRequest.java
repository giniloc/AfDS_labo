public class TransportRequest {
    private String pickupLocation;
    private String deliveryLocation;
    private Box box;

    public TransportRequest(String pickupLocation, String deliveryLocation, Box box) {
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.box = box;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public Box getBox() {
        return box;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

}
