import java.util.List;

public class TransportRequest {
    private int ID;
    private List <String>  pickupLocations;
    private List <String> deliveryLocations;
    private String boxID;

    public TransportRequest(int requestID, List<String> pickupLocations, List<String> placeLocations, String boxID) {
        this.ID = ID;
        this.pickupLocations = pickupLocations;
        this.deliveryLocations = deliveryLocations;
        this.boxID = boxID;
        System.out.println("request: " + requestID);
    }

    public List<String> getPickupLocations() {
        return pickupLocations;
    }

    public String getBoxID() {
        return boxID;
    }

    public List<String>  getDeliveryLocations() {
        return deliveryLocations;
    }

}
