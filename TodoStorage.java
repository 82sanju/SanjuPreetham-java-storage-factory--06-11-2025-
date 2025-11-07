import java.util.List;

public interface TodoStorage {
    void save(Todo todo) throws Exception;

    // IDs is integer
    Todo retrieve(int id) throws Exception;

    List<Todo> retrieveAll() throws Exception;

    void update(Todo todo) throws Exception;

    void delete(int id) throws Exception;
}

