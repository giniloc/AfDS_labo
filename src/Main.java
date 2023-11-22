import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/Instances2L/I20_20_2_2_8b2.json"  /*"src/I3_3_1_5.json"*/)));
            JSONObject jsonData = new JSONObject(jsonContent);

            int loadingDuration = jsonData.getInt("loadingduration");
            int vehicleSpeed = jsonData.getInt("vehiclespeed");
            int stackCapacity = jsonData.getInt("stackcapacity");

            Vehicle.setVehicleSpeed(vehicleSpeed);
            BoxStack.setStackCapacity(stackCapacity);

            //stacks
            JSONArray stackArray = jsonData.getJSONArray("stacks");
            Map<String, BoxStack> stacks = new HashMap<>();

            for (int i = 0; i < stackArray.length(); i++){
                if (stackArray.isNull(i)) continue;
                else {
                    JSONObject stackObject = stackArray.getJSONObject(i);
                    int ID = stackObject.getInt("ID");
                    String name = stackObject.getString("name");
                    int x = stackObject.getInt("x");
                    int y = stackObject.getInt("y");
                    BoxStack stack = new BoxStack(ID, name, x, y);

                    JSONArray boxesArray = stackObject.getJSONArray("boxes");
                    for (int j = 0; j < boxesArray.length(); j++) {
                        String boxID = boxesArray.getString(j);
                        stack.addBox(boxID); // Add the box to the BoxStack
                    }

                    stacks.put(name, stack);
                }
            }

            //buffer
            JSONArray bufferArray = jsonData.getJSONArray("bufferpoints");
            Map<String, Buffer> buffers = new HashMap<>();
            for (int i=0; i< bufferArray.length(); i++){
                if (bufferArray.isNull(i)) continue;
                else {
                    JSONObject bufferObject = bufferArray.getJSONObject(i);
                    int bufferID = bufferObject.getInt("ID");
                    String bufferName = bufferObject.getString("name");
                    int bufferX = bufferObject.getInt("x");
                    int bufferY = bufferObject.getInt("y");
                    Buffer buffer = new Buffer(bufferID, bufferName, bufferX, bufferY);
                    buffers.put(bufferName, buffer);
                }
            }


            //vehicles
            JSONArray vehicleArray = jsonData.getJSONArray("vehicles");
            List<Vehicle> vehicles = new ArrayList<>();

            for (int i = 0; i < vehicleArray.length(); i++){
                if (vehicleArray.isNull(i)) continue;
                else {
                    JSONObject vehicleObject = vehicleArray.getJSONObject(i);
                    int ID = vehicleObject.getInt("ID");
                    String name = vehicleObject.getString("name");
                    int capacity = vehicleObject.getInt("capacity");
                    int x = vehicleObject.getInt("x");//deze aanpassen voor andere inputs
                    int y = vehicleObject.getInt("y");//deze aanpassen voor andere inputs
                    Vehicle vehicle = new Vehicle(ID, name, capacity, x, y);
                    vehicles.add(vehicle);
                }
            }

            //requests

            JSONArray requestsArray = jsonData.getJSONArray("requests");
            Queue<Request> requests = new LinkedList<>();

            for (int i = 0; i < requestsArray.length(); i++) {
                if (requestsArray.isNull(i)) continue;
                else {
                    JSONObject requestObject = requestsArray.getJSONObject(i);
                    int requestID = requestObject.getInt("ID");
                    String pickupLocation = requestObject.getString("pickupLocation");
                    String placeLocation = requestObject.getString("placeLocation");
                    String boxID = requestObject.getString("boxID");

                    List<String> pickupLocations = new ArrayList<>();
                    pickupLocations.add(pickupLocation);
                    List<String> placeLocations = new ArrayList<>();
                    placeLocations.add(placeLocation);
                    Request request = new Request(requestID, stacks.get(pickupLocations.get(0)), buffers.get(placeLocations.get(0)), boxID);
                    requests.add(request);
                }
            }

            Scheduler scheduler = new Scheduler(vehicles, requests, loadingDuration, stacks);
          //  scheduler.preProcess();
            scheduler.start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}