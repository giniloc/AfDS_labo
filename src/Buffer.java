public class Buffer extends BoxStack {
    private int ID;
    private String name;
    private int x;
    private int y;

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
