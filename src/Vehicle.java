import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Vehicle {
    private int ID;
    private String name;
    private final int capacity;
    private int x;
    private int y;

    private TransportRequest request;
    private Queue<Task> taskQueue = new LinkedList<>();
    //these need to be reset when request complete
    private boolean stacksLocked = false;


    public Vehicle(int id, String n, int capacity, int xc, int yc) {
        this.ID = id;
        this.name = n;
        this.capacity = capacity;
        this.x = xc;
        this.y = yc;
    }

    public float GiveRequest(TransportRequest req){
        //calculate time for first task in request
        System.out.println(ID + " started req: " + req.getBoxID());
        request = req;
        taskQueue.add(new Task(TaskType.Move, req.getPickupLocation(), req.getDeliveryLocation(), null, 5));
        return 5;
    }

    public float ExecuteNextTask(){
        //execute current task and calculate time for next one
        System.out.println(ID + " ended req: " + request.getBoxID());
        Scheduler.amountNotCompletedReq--;

        if (stacksLocked){
            Task newTask = taskQueue.poll();
            switch (newTask.getTaskType()){
                case Move: Move(newTask);
                case Take: Take(newTask);
                case Place: Place(newTask);
                case Relocate: Relocate(newTask);
            }
        }
        else{
            //Lock pickup stack
            List<Task> undoTasks = new ArrayList<>();
            if (request.getPickupLocation() != GlobalData.getBuffer()){
                request.getPickupLocation().AddToQueue(this);

                int index = request.getPickupLocation().GetInverseIndexOf(request.getBoxID());
                List<String> boxList = new ArrayList<>();
                if (index+1 <= capacity){
                    boxList.add(request.getBoxID());
                    taskQueue.add(new Task(TaskType.Take, request.getDeliveryLocation(), null, boxList, index*2+1));
                }
                else{
                    //TODO
                    //relocate
                }

            }
            else; //TODO

            //Lock Delivery stack
            List<String> boxList = new ArrayList<>();
            boxList.add(request.getBoxID());
            if (request.getDeliveryLocation() != GlobalData.getBuffer()){
                request.getDeliveryLocation().AddToQueue(this);
                taskQueue.add(new Task(TaskType.Place, null, request.getDeliveryLocation(), boxList, GlobalData.getLoadingDuration()));
            }
            else{
                taskQueue.add(new Task(TaskType.Place, null, request.getDeliveryLocation(), boxList, GlobalData.getLoadingDuration()));
            }

            //TODO add undos to taskqueue

            stacksLocked = true;
        }

        return 0;
    }

    //TODO
    private void Move(Task task){

    }

    private void Take(Task task){
        //dont poll but pop from stack
    }

    private void Place(Task task){

    }

    private void Relocate(Task task){

    }

    private BoxStack FindRelocateStack(){
        //problem: what if boxed need to be divided over stacks?
        int amountBeforeMe = Integer.MAX_VALUE;
        BoxStack stack = null;
        for (String key : GlobalData.getBoxStacks().keySet()){
            int amount = GlobalData.getBoxStacks().get(key).AmountInQueue();
            if (amount < amountBeforeMe){
                stack = GlobalData.getBoxStacks().get(key);
                amountBeforeMe = amount;
                if (amount == 0){
                    break;
                }
            }
        }
        return stack;
    }

}