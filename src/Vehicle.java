import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private String id;
    private int capacity;
    private List<Box> loadedBoxes;
    private String currentLocation;

    public Vehicle(String id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.loadedBoxes = new ArrayList<>();
        this.currentLocation = "Buffer Point"; // assuming vehicles start at the buffer point
    }

    public void loadBox(Box box) {
        if (loadedBoxes.size() < capacity) {
            loadedBoxes.add(box);
        } else {
            System.out.println("Vehicle capacity reached. Can't load more boxes.");
        }
    }

    public void unloadBox(Box box) {
        loadedBoxes.remove(box);
    }

    public void moveToLocation(String location) {
        this.currentLocation = location;
    }
    public void executeRequest(TransportRequest request) {
        // Move to pickup location
        moveToLocation(request.getPickupLocation());
        // Load box
        loadBox(request.getBox());
        // Move to delivery location
        moveToLocation(request.getDeliveryLocation());
        // Unload box
        unloadBox(request.getBox());
    }

}