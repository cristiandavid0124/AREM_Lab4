package escuelaing.edu.arep.awsapp;

import static spark.Spark.port;
import java.util.List;
import static spark.Spark.get;

/**
 * LogService is a web service that listens for log entries and stores them in a MongoDB database.
 * It uses the Spark framework to handle HTTP requests.
 */
public class LogService {

    // MongoUtil instance to interact with the MongoDB database
    private static final MongoUtil mongoUtil = new MongoUtil();

    /**
     * The entry point of the application. 
     * It configures the server port and defines the /logservice route to save logs.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Set the port for the application
        port(getPort());
        
        // Define a GET route that listens on "/logservice" and saves the "log" query parameter
        get("/logservice", (req, res) -> saveLog(req.queryParams("log")));
    }

    /**
     * Saves a log entry into the MongoDB database using MongoUtil and returns the list of logs.
     *
     * @param log The log entry to be saved.
     * @return A list of all log entries from the database.
     */
    private static List<String> saveLog(String log) {
        // Add the log entry to the database
        mongoUtil.addLog(log);
        
        // Retrieve and return the list of all logs
        return mongoUtil.getLogs();
    }

    /**
     * Determines the port on which the server will run.
     * It first checks if the environment variable "PORT" is set.
     * If not, it returns a default port of 35000.
     *
     * @return The port number for the server.
     */
    private static int getPort() {
        // Retrieve the port from the environment variable "PORT" if available
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        // Default to port 35000 if "PORT" is not set
        return 35000;
    }
}

