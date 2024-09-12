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

/**
 * MongoUtil is a utility class that provides methods for connecting to a MongoDB database,
 * adding log entries, and retrieving the latest log entries. It handles the connection
 * and disconnection from the database, ensuring that each operation is encapsulated properly.
 */
public class MongoUtil {

    // MongoDB connection details
    private static final String CONNECTION_STRING = "mongodb://db:27017";
    private static final String DATABASE_NAME = "logsDB";
    
    // MongoDB client and collection for storing log entries
    private MongoClient client;
    private MongoCollection<Document> logsCollection;

    /**
     * Adds a log entry to the MongoDB database. 
     * Each log entry includes a timestamp of when it was added.
     *
     * @param log The log message to be added.
     */
    public void addLog(String log) {
        // Establish a connection to the database
        connect();
        
        // Create a new document with the log message and current timestamp
        Document newLog = new Document("log", log).append("date", LocalDateTime.now());
        
        // Insert the document into the logs collection
        logsCollection.insertOne(newLog);
        
        // Close the connection after the operation
        disconnect();
    }

    /**
     * Retrieves the 10 most recent log entries from the MongoDB database.
     *
     * @return A list of log entries in JSON format.
     */
    public List<String> getLogs() {
        // Establish a connection to the database
        connect();
        
        // Create a list to store the retrieved logs
        List<String> logs = new ArrayList<>();
        
        // Fetch the 10 most recent logs, sorted by their insertion order
        logsCollection.find()
                      .sort(Sorts.descending("_id"))
                      .limit(10)
                      .forEach(log -> logs.add(log.toJson()));
        
        // Close the connection after the operation
        disconnect();
        
        return logs;
    }

    /**
     * Establishes a connection to the MongoDB database and selects the logs collection.
     */
    private void connect() {
        // Create a new MongoDB client with the specified connection string
        this.client = MongoClients.create(new ConnectionString(CONNECTION_STRING));
        
        // Get the database and the collection where logs are stored
        MongoDatabase db = client.getDatabase(DATABASE_NAME);
        this.logsCollection = db.getCollection("logs");
    }

    /**
     * Closes the connection to the MongoDB database.
     */
    private void disconnect() {
        // Close the MongoDB client to release resources
        this.client.close();
    }
    
}

