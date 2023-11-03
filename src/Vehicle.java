import java.util.*;

public class Vehicle {
    private int ID;
    private String name;
    private final int capacity;
    private int x;
    private int y;
    private boolean isBusy = false;

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
        System.out.println("Vehicle " + ID + " started req: " + req.getBoxID());
        isBusy = true;
        request = req;
        float moveTime  = GetMoveTime(req.getPickupLocation().getX(), req.getPickupLocation().getY());
        taskQueue.add(new Task(TaskType.Move, req.getPickupLocation(), null, moveTime));
        return moveTime;
    }

    public float ExecuteNextTask(int time){
        //TODO give time with and see if stack free, otherwise wait for time of other vehicles in stack!!!
        //execute current task and calculate time for next one
        float ret = 1; //temp 0 initialisation //TODO set with task initialisation

        if (stacksLocked){
            Task newTask = taskQueue.poll();
            if (newTask == null){
                isBusy = false;
                stacksLocked = false;
                Scheduler.amountNotCompletedReq--;
                return 0;
            }
            switch (newTask.getTaskType()){
                case Move: Move(newTask); break;
                case Take: Take(newTask); break;
                case Place: Place(newTask); break;
            }
            ret = newTask.getExecutionTime();
        }
        else{
            Task newTask = taskQueue.poll();
            Move(newTask);
            ret = newTask.getExecutionTime();

            //Lock pickup stack & relocate stack(s)
            List<Task> undoTasks = new ArrayList<>();
            if (request.getPickupLocation() != GlobalData.getBuffer()){
                request.getPickupLocation().AddToQueue(this);
                int index = request.getPickupLocation().GetDepth(request.getBoxID());
                int size = request.getPickupLocation().GetStackSize();
                if (index <= capacity){
                    //relocate to self
                    for (int i = 0; i < index-1; i++){
                        String nextBox = request.getPickupLocation().GetFromStack(size-(i+1));
                        taskQueue.add(new Task(TaskType.Take, request.getPickupLocation(), nextBox, GlobalData.getLoadingDuration()));
                        undoTasks.add(new Task(TaskType.Place, request.getPickupLocation(), nextBox, GlobalData.getLoadingDuration()));
                    }
                }
                else{
                    //relocate to other
                    System.out.println("Relocate!");
                    //TODO
                }
                //Required box itself
                taskQueue.add(new Task(TaskType.Take, request.getPickupLocation(), request.getBoxID(), GlobalData.getLoadingDuration()));
            }
            else {
                taskQueue.add(new Task(TaskType.Take, request.getDeliveryLocation(), request.getBoxID(), GlobalData.getLoadingDuration()));
            }

            taskQueue.addAll(undoTasks);

            //Lock Delivery stack
            float moveTime = GetMoveTime(request.getDeliveryLocation().getX(), request.getDeliveryLocation().getY());
            taskQueue.add(new Task(TaskType.Move, request.getDeliveryLocation(), null, moveTime));
            if (request.getDeliveryLocation() != GlobalData.getBuffer()){
                request.getDeliveryLocation().AddToQueue(this);
                taskQueue.add(new Task(TaskType.Place, request.getDeliveryLocation(), request.getBoxID(), GlobalData.getLoadingDuration()));
            }
            else{
                taskQueue.add(new Task(TaskType.Place, request.getDeliveryLocation(), request.getBoxID(), GlobalData.getLoadingDuration()));
            }

            stacksLocked = true;
        }

        return ret;
    }

    //TODO
    private void Move(Task task){
        x = task.getStack().getX();
        y = task.getStack().getY();
    }

    private void Take(Task task){
        task.getStack().Pop();
        System.out.println("Vehicle " + ID + " Took " + task.getBox() + " from " + task.getStack().getName());
    }

    private void Place(Task task){
        task.getStack().Push(task.getBox());
        System.out.println("Vehicle " + ID + " Added " + task.getBox() + " to " + task.getStack().getName());
    }

    private List<BoxStack> FindRelocateStacks(int boxAmount){
        //TODO dealing with boxamount
        List<BoxStack> relocateStacks = new ArrayList<>();
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
        return null; //TODO temp
    }

    private float GetMoveTime(int x_end, int y_end){
        //return (float)((Math.sqrt((x_end-x)*(x_end-x) + (y_end-y)*(y_end-y)))/GlobalData.getVehicleSpeed());          //Euclidisch
        return Math.abs((float)((x_end-x)+(y_end-y)))/GlobalData.getVehicleSpeed();                                     //Manhattan
    }

    public boolean isBusy() {
        return isBusy;
    }
}