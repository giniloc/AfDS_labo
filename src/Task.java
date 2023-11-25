public class Task {
    int startTime = 0;
    int duration;

    TaskType type;
    String box;
    BoxStack stack;
    Vehicle vehicle;
    int x;
    int y;

    public Task(){
        type = TaskType.WAIT;
    }

    public Task(int duration, TaskType type, String box, Vehicle vehicle, BoxStack stack) {
        this.duration = duration;
        this.type = type;
        this.box = box;
        this.stack = stack;
        this.vehicle = vehicle;
        this.x = vehicle.getX();
        this.y = vehicle.getY();
    }

    public void print(){
        System.out.println(vehicle.getName() + ";" +
                x + ";" +
                y + ";" + startTime + ";" +
                stack.getX() + ";" +
                stack.getY() + ";" +
                (startTime+duration) + ";" +
                box + ";" +
                type);
    }

    //Getters & Setters
    public BoxStack getStack() {
        return stack;
    }

    public int getEndTime(){
        return startTime+duration;
    }

    public int getDuration(){
        return duration;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }
}

enum TaskType{
    PU,
    PL,
    WAIT
}
