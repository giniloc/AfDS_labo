public class GlobalData {
    private static Buffer buffer;
    private static Scheduler scheduler;

    public static void SetData(Buffer bu, Scheduler sc){
        buffer = bu;
        scheduler = sc;
    }

    //Getters
    public static Buffer getBuffer() {
        return buffer;
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

}
