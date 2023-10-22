import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                BoxStack stack = new BoxStack(ID, name, x, y);

                JSONArray boxesArray = stackObject.getJSONArray("boxes");
                for (int j = 0; j < boxesArray.length(); j++) {
                    String boxID = boxesArray.getString(j);
                    stack.addBox(boxID); // Add the box to the BoxStack
                }

                stacks[i] = stack;
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
            Stack<TransportRequest> requests = new Stack<>();

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
                requests.add(request);
            }
            Scheduler scheduler = new Scheduler(loadingDuration, vehicleSpeed, stackCapacity, stacks, buffer, vehicles, requests);
            //scheduler.scheduleRequests();

            // Create an ExecutorService with a fixed number of threads (e.g., one thread per vehicle)
            ExecutorService executorService = Executors.newFixedThreadPool(vehicles.length);

            // Create and start worker threads for each vehicle
            for (Vehicle vehicle : vehicles) {
                Runnable worker = new VehicleWorker(vehicle, scheduler);
                executorService.execute(worker);
            }

            // Shutdown the executor when all tasks are complete
            executorService.shutdown();




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}