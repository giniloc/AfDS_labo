import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String I0 = "I20_20_2_2_8b2.json";
        String I1 = "I30_100_1_1_10.json";
        String I2 = "I30_100_3_3_10.json";
        String I3 = "I30_200_3_3_10.json";
        String I4 = "I100_50_2_2_8b2.json";
        String I5 = "I100_120_2_2_8b2.json";

        String Itest = "I3_3_1_5.json";
        String currentInput = Itest;

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(currentInput)));
            JSONObject jsonData = new JSONObject(jsonContent);

            int loadingDuration = jsonData.getInt("loadingduration");
            int vehicleSpeed = jsonData.getInt("vehiclespeed");
            int stackCapacity = jsonData.getInt("stackcapacity");

            Vehicle.setVehicleSpeed(vehicleSpeed);
            Vehicle.setLoadingDuration(loadingDuration);
            BoxStack.setStackCapacity(stackCapacity);

            //stacks
            JSONArray stackArray = jsonData.getJSONArray("stacks");
            Map<String, BoxStack> stacks = new HashMap<>();

            for (int i = 0; i < stackArray.length(); i++){
                if (stackArray.isNull(i)) continue;
                else{
                    JSONObject stackObject = stackArray.getJSONObject(i);
                    int ID = stackObject.getInt("ID");
                    String name = stackObject.getString("name");
                    int x = stackObject.getInt("x");
                    int y = stackObject.getInt("y");
                    BoxStack stack = new BoxStack(ID, name, x, y);

                    JSONArray boxesArray = stackObject.getJSONArray("boxes");
                    for (int j = 0; j < boxesArray.length(); j++) {
                        String boxID = boxesArray.getString(j);
                        stack.push(new Box(boxID)); // Add the box to the BoxStack
                    }

                    stacks.put(name, stack);
                }
            }

            //buffer
            JSONArray bufferArray = jsonData.getJSONArray("bufferpoints");
            Map<String, Buffer> buffers = new HashMap<>();
            for (int i = 0; i < bufferArray.length(); i++){
                if (bufferArray.isNull(i)) continue;
                else{
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
            Vehicle[] vehicles = new Vehicle[vehicleArray.length()];

            for (int i = 0; i < vehicleArray.length(); i++){
                if (vehicleArray.isNull(i)) continue;
                else {
                    JSONObject vehicleObject = vehicleArray.getJSONObject(i);
                    int ID = vehicleObject.getInt("ID");
                    String name = vehicleObject.getString("name");
                    int capacity = vehicleObject.getInt("capacity");
                    int x = vehicleObject.getInt("x"); //deze aanpassen voor andere inputs
                    int y = vehicleObject.getInt("y"); //deze aanpassen voor andere inputs
                    vehicles[i] = new Vehicle(ID, name, capacity, x, y);
                }
            }

            //requests

            JSONArray requestsArray = jsonData.getJSONArray("requests");

            for (int i = 0; i < requestsArray.length(); i++) {
                if (!requestsArray.isNull(i)){
                    JSONObject requestObject = requestsArray.getJSONObject(i);
                    String pickupLocation = requestObject.getString("pickupLocation");
                    String placeLocation = requestObject.getString("placeLocation");
                    String boxID = requestObject.getString("boxID");

                    if (buffers.containsKey(pickupLocation)){
                        stacks.get(placeLocation).addDeliveryBox();
                        buffers.get(pickupLocation).addPickupBox(placeLocation, new Box(boxID));
                    }
                    else{
                        stacks.get(pickupLocation).addPickupBox(boxID, buffers.get(placeLocation));
                    }
                }
            }

            Scheduler scheduler = new Scheduler(vehicles, stacks, buffers, currentInput);
            scheduler.Start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}