import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLTodoStorage implements TodoStorage {
    private final String url;
    private final String user;
    private final String password;

    public PostgreSQLTodoStorage(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void save(Todo todo) throws SQLException {
        String sql = "INSERT INTO todos (title, description, completed) VALUES (?, ?, ?)";
        // Enable retrieval of generated serial ID
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getDescription());
            ps.setBoolean(3, todo.isCompleted());
            ps.executeUpdate();

            // Retrieve the generated serial ID from PostgreSQL
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    todo.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Todo retrieve(int id) throws SQLException {
        String sql = "SELECT id, title, description, completed FROM todos WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Todo(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBoolean("completed")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Todo> retrieveAll() throws SQLException {
        String sql = "SELECT id, title, description, completed FROM todos ORDER BY id ASC";
        List<Todo> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Todo(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("completed")
                ));
            }
        }
        return list;
    }

    @Override
    public void update(Todo todo) throws SQLException {
        String sql = "UPDATE todos SET title = ?, description = ?, completed = ? WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getDescription());
            ps.setBoolean(3, todo.isCompleted());
            ps.setInt(4, todo.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
