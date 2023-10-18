public class Vehicle {
    private int ID;
    private String name;
    private int capacity;
    private int x;
    private int y;

    public Vehicle(int id, String n, int capacity, int xc, int yc) {
        this.ID = id;
        name = n;
        this.capacity = capacity;
        x = xc;
        y = yc;
    }
/*
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

 */

}