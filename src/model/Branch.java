public class Branch {
    private String name;
    private int id;
    private boolean isConnected;

    public Branch(String name, int id, boolean isConnected) {
        this.name = name;
        this.id = id;
        this.isConnected = isConnected;
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getId() {
        return id;
    }
}