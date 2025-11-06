public class StorageFactory {
    public enum Type { POSTGRESQL, MYSQL, MONGODB }


    public static TodoStorage create(Type type) {
        switch (type) {
            case POSTGRESQL:

                String url = "jdbc:postgresql://localhost:5432/todo_db";
                String user = "postgres"; // change if needed
                String password = "password"; // change if needed
                return new PostgreSQLTodoStorage(url, user, password);
           /* case MYSQL:
// Provide an example JDBC url for MySQL; user must change credentials
                String mysqlUrl = "jdbc:mysql://localhost:3306/todo_db";
                return new MySQLTodoStorage(mysqlUrl, "root", "password");
            case MONGODB:
// Example: mongodb://localhost:27017
                return new MongoDBTodoStorage("mongodb://localhost:27017", "todo_db");
            */
            default:
                throw new IllegalArgumentException("Unknown storage type");
        }
    }
}