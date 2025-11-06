import java.util.List;

public interface TodoStorage {
    void save(Todo todo) throws Exception;

    // IDs are now integers, not strings
    Todo retrieve(int id) throws Exception;

    List<Todo> retrieveAll() throws Exception;

    void update(Todo todo) throws Exception;

    void delete(int id) throws Exception;
}
