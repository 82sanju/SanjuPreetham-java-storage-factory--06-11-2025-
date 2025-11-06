public class Todo {
    private int id;
    private String title;
    private String description;
    private boolean completed;

    // Default constructor
    public Todo() {}

    // Constructor without ID (used when creating new TODOs)
    public Todo(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Constructor with ID (used when reading from DB)
    public Todo(int id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return String.format(
                "Todo{id=%d, title='%s', description='%s', completed=%b}",
                id, title, description, completed
        );
    }
}
