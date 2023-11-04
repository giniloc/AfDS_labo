import java.util.ArrayList;
import java.util.List;

public class Task {
    private TaskType taskType;
    private BoxStack startStack;
    private BoxStack endStack;
    private List<String> boxes;
    private float executionTime;

    public Task(TaskType tt, BoxStack ss, BoxStack es, List<String> bo, float ex){
        taskType = tt;
        startStack = ss;
        endStack = es;
        if (bo != null) boxes = new ArrayList<>(bo);
        executionTime = ex;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public BoxStack getStartStack() {
        return startStack;
    }

    public BoxStack getEndStack() {
        return endStack;
    }

    public List<String> getBoxes() {
        return boxes;
    }

    public float getExecutionTime() {
        return executionTime;
    }
}

enum TaskType{
    Move,
    Take,
    Place,
    Relocate
}
