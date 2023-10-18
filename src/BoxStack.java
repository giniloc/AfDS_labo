public class BoxStack {
    private int ID;
    private String name;
    private int x;
    private int y;
    private String[] boxes;

    public BoxStack(int ID, String name, int x, int y, String[] boxes) {
        System.out.println("new stack: " + " " + name + " " + x + " " + y);
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.boxes = boxes;
    }

    // methods to add box, remove box, check if full, etc.
}
