public class Request {
    private int ID;
    private final BoxStack pickupLocation;
    private final BoxStack deliveryLocation;
    private final String boxID;

    public Request(int requestID, BoxStack pickupLocation, BoxStack placeLocations, String boxID) {
        this.ID = requestID;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = placeLocations;
        this.boxID = boxID;
    }

    public BoxStack getPickupLocation() {
        return pickupLocation;
    }

    public String getBoxID() {
        return boxID;
    }

    public BoxStack getDeliveryLocation() {
        return deliveryLocation;
    }
}
