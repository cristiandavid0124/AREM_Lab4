package escuelaing.edu.arep.awsapp;

import static spark.Spark.*;

/**
 * Main application class that sets up a simple web service using the Spark framework.
 */
public class App {

    /**
     * The entry point of the application. 
     * It configures the server port, sets up the static files location, 
     * and defines the routes.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Set the port for the application to run on
        port(getPort());
        
        // Serve static files from the "/public" directory
        staticFiles.location("/public");
        
        // Define a GET route that listens on "/log" and processes the "msg" query parameter
        get("/log", (req, res) -> RRInvoker.invoke(req.queryParams("msg")));
    }
    
    /**
     * Determines the port on which the server will run.
     * The port is retrieved from the environment variable "PORT". 
     * If "PORT" is not set, it defaults to 35001.
     *
     * @return The port number for the server to run on.
     */
    private static int getPort() {
        // Check if the "PORT" environment variable is set and return its value
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        // Return the default port if "PORT" is not set
        return 35001;
    }

}
