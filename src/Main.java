import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/I3_3_1_5.json")));
            JSONObject jsonData = new JSONObject(jsonContent);

            int loadingDuration = jsonData.getInt("loadingduration");
            int vehicleSpeed = jsonData.getInt("vehiclespeed");
            int stackCapacity = jsonData.getInt("stackcapacity");

            //stacks
            JSONArray stackArray = jsonData.getJSONArray("stacks");
            BoxStack[] stacks = new BoxStack[stackArray.length()];

            for (int i = 0; i < stackArray.length(); i++){
                JSONObject stackObject = stackArray.getJSONObject(i);
                int ID = stackObject.getInt("ID");
                String name = stackObject.getString("name");
                int x = stackObject.getInt("x");
                int y = stackObject.getInt("y");
                JSONArray boxesArray = stackObject.getJSONArray("boxes");
                String[] boxes = new String[boxesArray.length()];

                for (int j = 0; j < boxesArray.length(); j++) {
                    boxes[j] = boxesArray.getString(j);
                }
                stacks[i] = new BoxStack(ID, name, x, y, boxes);
            }

            //buffer
            JSONObject bufferObject = jsonData.getJSONArray("bufferpoints").getJSONObject(0);
            int bufferID = bufferObject.getInt("ID");
            String bufferName = bufferObject.getString("name");
            int bufferX = bufferObject.getInt("x");
            int bufferY = bufferObject.getInt("y");
            Buffer buffer = new Buffer(bufferID, bufferName, bufferX, bufferY);
            System.out.println("Buffer: " + buffer);


            //vehicles
            JSONArray vehicleArray = jsonData.getJSONArray("vehicles");
            Vehicle[] vehicles = new Vehicle[vehicleArray.length()];

            for (int i = 0; i < vehicleArray.length(); i++){
                JSONObject vehicleObject = vehicleArray.getJSONObject(i);
                int ID = vehicleObject.getInt("ID");
                String name = vehicleObject.getString("name");
                int capacity = vehicleObject.getInt("capacity");
                int x = vehicleObject.getInt("xCoordinate");
                int y = vehicleObject.getInt("yCoordinate");
                vehicles[i] = new Vehicle(ID, name, capacity, x, y);
            }

            //requests

            JSONArray requestsArray = jsonData.getJSONArray("requests");
            TransportRequest[] requests = new TransportRequest[requestsArray.length()];

            for (int i = 0; i < requestsArray.length(); i++) {
                JSONObject requestObject = requestsArray.getJSONObject(i);
                int requestID = requestObject.getInt("ID");
                JSONArray pickupLocationsArray = requestObject.getJSONArray("pickupLocation");
                JSONArray placeLocationsArray = requestObject.getJSONArray("placeLocation");
                String boxID = requestObject.getString("boxID");

                List<String> pickupLocations = new ArrayList<>();
                for (int j = 0; j < pickupLocationsArray.length(); j++) {
                    pickupLocations.add(pickupLocationsArray.getString(j));
                }

                List<String> placeLocations = new ArrayList<>();
                for (int j = 0; j < placeLocationsArray.length(); j++) {
                    placeLocations.add(placeLocationsArray.getString(j));
                }

                TransportRequest request = new TransportRequest(requestID, pickupLocations, placeLocations, boxID);
                requests[i] = request;
            }

            Scheduler scheduler = new Scheduler(loadingDuration, vehicleSpeed, stackCapacity, stacks, buffer, vehicles, requests);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}