import java.util.List;

public class TodoService {
    private final TodoStorage storage;

    public TodoService(TodoStorage storage) {
        this.storage = storage;
    }

    public void createTodo(Todo todo) throws Exception {
        storage.save(todo);
    }

    // Use int instead of String
    public Todo getTodo(int id) throws Exception {
        return storage.retrieve(id);
    }

    public List<Todo> getAll() throws Exception {
        return storage.retrieveAll();
    }

    public void updateTodo(Todo todo) throws Exception {
        storage.update(todo);
    }

    // Use int instead of String
    public void deleteTodo(int id) throws Exception {
        storage.delete(id);
    }
}
