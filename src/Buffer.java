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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Buffer{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
