public class Buffer extends BoxStack {
    private final int ID;
    private final String name;
    private final int x;
    private final int y;

    public Buffer(int ID, String name, int x, int y) {
        super(ID, name, x, y);
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y; 
    }

    // Getters and setters for the class attributes

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
