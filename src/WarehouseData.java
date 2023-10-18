import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class WarehouseData {
    @JsonProperty("loadingduration")
    private int loadingDuration;

    @JsonProperty("vehiclespeed")
    private int vehicleSpeed;

    @JsonProperty("stackcapacity")
    private int stackCapacity;

    @JsonProperty("stacks")
    private List<Stack> stacks;

    @JsonProperty("bufferpoints")
    private List<BufferPoint> bufferPoints;

    @JsonProperty("vehicles")
    private List<Vehicle> vehicles;

    @JsonProperty("requests")
    private List<Request> requests;

    // Getters and setters for the above fields

    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "{...}"; // Replace with your JSON data
            WarehouseData warehouseData = objectMapper.readValue(json, WarehouseData.class);

            // Now you can work with the parsed Java object
            System.out.println(warehouseData.getLoadingDuration());
            System.out.println(warehouseData.getStacks().get(0).getName());
            // ... and so on
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
