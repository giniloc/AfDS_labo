public class Task {
    private TaskType taskType;
    private BoxStack stack;
    private String box;
    private float executionTime;

    public Task(TaskType tt, BoxStack st, String bo, float ex){
        taskType = tt;
        stack = st;
        box = bo;
        executionTime = ex;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public BoxStack getStack() {
        return stack;
    }

    public String getBox() {
        return box;
    }

    public float getExecutionTime() {
        return executionTime;
    }
}

//TODO
enum TaskType{
    Move,
    Take,
    Place
}
