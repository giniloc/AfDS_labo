public class Scheduler {

    private int loadingDuration;
    private int vehicleSpeed;
    private int stackCapacity;

    private BoxStack[] boxStacks;
    private Buffer buffer;
    private Vehicle[] vehicles;
    private TransportRequest[] requests;
    public Scheduler(int lo, int vesp, int stcap, BoxStack[] bs, Buffer bu, Vehicle[] ve, TransportRequest[] re){
        loadingDuration = lo;
        vehicleSpeed = vesp;
        stackCapacity = stcap;
        boxStacks = bs;
        buffer = bu;
        vehicles = ve;
        requests = re;
    }
}
