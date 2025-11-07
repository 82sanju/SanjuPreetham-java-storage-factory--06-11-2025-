import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBTodoStorage implements TodoStorage {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoDBTodoStorage(String connectionString, String dbName) {
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(dbName);
        this.collection = database.getCollection("todos");
    }

    @Override
    public void save(Todo todo) {
        Document doc = new Document("title", todo.getTitle())
                .append("description", todo.getDescription())
                .append("completed", todo.isCompleted());
        collection.insertOne(doc);

        todo.setMongoId(doc.getObjectId("_id").toString());
    }


    @Override
    public Todo retrieve(int id) throws Exception {
        throw new UnsupportedOperationException("MongoDB uses ObjectId (not int). Use retrieveByObjectId instead.");
    }

    // Add this helper if you want to retrieve by MongoDB's ObjectId string:
    public Todo retrieveByObjectId(String objectId) {
        Document doc = collection.find(eq("_id", new ObjectId(objectId))).first();
        if (doc != null) {
            return new Todo(
                    0, // MongoDB doesn't use int IDs, but keep 0 for compatibility
                    doc.getString("title"),
                    doc.getString("description"),
                    doc.getBoolean("completed", false)
            );
        }
        return null;
    }

    @Override
    public List<Todo> retrieveAll() {
        List<Todo> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                list.add(new Todo(
                        0, // dummy integer ID
                        doc.getString("title"),
                        doc.getString("description"),
                        doc.getBoolean("completed", false)
                ));
            }
        }
        return list;
    }

    @Override
    public void update(Todo todo) {
        if (todo.getMongoId() == null || todo.getMongoId().isEmpty()) {
            throw new IllegalArgumentException("MongoDBTodoStorage: missing ObjectId for update");
        }

        collection.updateOne(eq("_id", new ObjectId(todo.getMongoId())),
                Updates.combine(
                        Updates.set("title", todo.getTitle()),
                        Updates.set("description", todo.getDescription()),
                        Updates.set("completed", todo.isCompleted())
                ));
    }


    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("MongoDB uses ObjectId (not int). Use deleteByObjectId instead.");
    }

    // Helper to delete by ObjectId
    public void deleteByObjectId(String objectId) {
        collection.deleteOne(eq("_id", new ObjectId(objectId)));
    }
}
