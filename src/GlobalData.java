import java.util.Map;

public class GlobalData {
    private static int loadingDuration;
    private static int vehicleSpeed;
    private static int stackCapacity;

    private static Map<String, BoxStack> boxStacks;
    private static Buffer buffer;
    private static Scheduler scheduler;

    public static void SetData(int ld, int vs, int sca, Map<String, BoxStack> bs, Buffer bu, Scheduler sc){
        loadingDuration = ld;
        vehicleSpeed = vs;
        stackCapacity = sca;
        boxStacks = bs;
        buffer = bu;
        scheduler = sc;
    }

    //Getters
    public static int getLoadingDuration() {
        return loadingDuration;
    }

    public static int getVehicleSpeed() {
        return vehicleSpeed;
    }

    public static int getStackCapacity() {
        return stackCapacity;
    }

    public static Buffer getBuffer() {
        return buffer;
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public static Map<String, BoxStack> getBoxStacks() {
        return boxStacks;
    }

}
