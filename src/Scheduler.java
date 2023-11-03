import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class Scheduler {

    public static List<BoxStack> neededStacks = new ArrayList<>();

    private final Vehicle[] vehicles;
    private Stack<TransportRequest> requests;

    public static int amountNotCompletedReq;

    public Scheduler(Vehicle[] ve, Stack<TransportRequest> re) {
        this.vehicles = ve;
        this.requests = re;
        amountNotCompletedReq = requests.size();
    }

    public void Start(){
        float[] taskTimePerVehicle = new float[vehicles.length];
        int minTimeIndex = 0;
        int time = 0;
        for (int i = 0; i < vehicles.length; i++){
            taskTimePerVehicle[i] = vehicles[i].GiveRequest(requests.pop());
            if (taskTimePerVehicle[i] < taskTimePerVehicle[minTimeIndex]){
                minTimeIndex = i;
            }
        }

        float minTime;
        while(amountNotCompletedReq > 0){
            minTime = taskTimePerVehicle[minTimeIndex];
            for (int i = 0; i < vehicles.length; i++){
                if (i == minTimeIndex){
                    taskTimePerVehicle[i] = vehicles[i].ExecuteNextTask(time);
                    time += taskTimePerVehicle[i];
                    if (!vehicles[i].isBusy()){
                        if (requests.isEmpty()){
                            taskTimePerVehicle[i] = Integer.MAX_VALUE;
                        }
                        else{
                            taskTimePerVehicle[i] = vehicles[i].GiveRequest(requests.pop());
                        }
                    }
                }
                else{
                    taskTimePerVehicle[i] -= minTime;
                }
                if (taskTimePerVehicle[i] < taskTimePerVehicle[minTimeIndex]){
                    minTimeIndex = i;
                }
            }
        }
    }
}
