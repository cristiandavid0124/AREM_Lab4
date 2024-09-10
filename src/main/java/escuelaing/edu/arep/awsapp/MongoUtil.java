package escuelaing.edu.arep.awsapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class MongoUtil {
    private static final String CONNECTION_STRING = "mongodb://db:27017";
    private static final String DATABASE_NAME = "logsDB";
    private MongoClient client;
    private MongoCollection<Document> logsCollection;

    public void addLog(String log) {
        connect();
        Document newLlog = new Document("log", log).append("date", LocalDateTime.now());
        logsCollection.insertOne(newLlog);
        disconnect();
    }

    public List<String> getLogs() {
        connect();
        List<String> logs = new ArrayList<>();
        logsCollection.find().sort(Sorts.descending("_id")).limit(10).forEach(log -> {
            logs.add(log.toJson());});
        disconnect();
        return logs;
    }

    private void connect() {
        this.client = MongoClients.create(new ConnectionString(CONNECTION_STRING));
        MongoDatabase db = client.getDatabase(DATABASE_NAME);
        this.logsCollection = db.getCollection("logs");
    }

    private void disconnect() {
        this.client.close();
    }
    
}
