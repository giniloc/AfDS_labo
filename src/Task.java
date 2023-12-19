public class Task {
    private int startTime = 0;
    private final int duration;

    private final TaskType type;
    private final String box;

    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;

    private final Vehicle vehicle;
    private final BoxStack endStack;

    public Task(TaskType type, String box, Vehicle vehicle, int x0, int y0, BoxStack endStack) {
        this.type = type;
        this.box = box;
        this.vehicle = vehicle;
        this.startX = x0;
        this.startY = y0;
        this.endX = endStack.getX();
        this.endY = endStack.getY();
        this.endStack = endStack;

        duration = calculateDriveTime(startX, startY, endX, endY) + Vehicle.getLoadingDuration();
    }

    public Task(TaskType type, String box, Vehicle vehicle, BoxStack stack) {
        this.type = type;
        this.box = box;
        this.vehicle = vehicle;
        this.startX = vehicle.getX();
        this.startY = vehicle.getY();
        this.endX = stack.getX();
        this.endY = stack.getY();
        this.endStack = stack;

        duration = Vehicle.getLoadingDuration();
    }

    private int calculateDriveTime(int x0, int y0, int x1, int y1){
        return Math.abs((x0 - x1) + (y0 - y1)) / Vehicle.getVehicleSpeed();
    }

    public String print() {
        return vehicle.getName() + ";" +
                startX + ";" +
                startY + ";" + startTime + ";" +
                endX + ";" +
                endY + ";" +
                (startTime + duration) + ";" +
                box + ";" +
                type;
    }

    //Getters & Setters
    public int getDuration(){
        return duration;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public BoxStack getEndStack() {
        return endStack;
    }
}

enum TaskType{
    PU,
    PL
}
