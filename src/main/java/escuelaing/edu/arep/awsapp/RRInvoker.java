package escuelaing.edu.arep.awsapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * RRInvoker is a utility class that performs round-robin invocations of different log services.
 * It sends HTTP GET requests to a set of predefined log service servers and cycles between them.
 */
public class RRInvoker {

    // The User-Agent header for the HTTP request
    private static final String USER_AGENT = "Mozilla/5.0";

    // Index to keep track of the current server being used
    private static int serverIndex = 0;

    // List of log service server URLs to use in a round-robin fashion
    private static final String[] SERVERS = new String[] {
        "http://logservice1:35000/logservice?log=",
        "http://logservice2:35000/logservice?log=",
        "http://logservice3:35000/logservice?log="
    };

    /**
     * Invokes a log service server using an HTTP GET request. The request is sent to one of the
     * predefined servers in a round-robin manner, cycling between servers on each request.
     *
     * @param logmsg The log message to be sent as a query parameter.
     * @return The response from the server as a string.
     * @throws IOException If an input or output exception occurs during the request.
     */
    public static String invoke(String logmsg) throws IOException {
        // Construct the GET URL by appending the log message to the selected server's URL
        String GET_URL = SERVERS[serverIndex] + logmsg;
        System.out.println("GET URL: " + GET_URL);

        // Create a URL object from the GET URL
        URL obj = new URL(GET_URL);

        // Open a connection to the URL and configure it for an HTTP GET request
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // Get the response code from the server
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        // Initialize a StringBuffer to store the server's response
        StringBuffer response = new StringBuffer();

        // If the response code is HTTP OK (200), read the response from the input stream
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            // If the response is not OK, print an error message
            System.out.println("GET request not worked");
        }

        // Print the completion message for the GET request
        System.out.println("GET DONE");

        // Switch to the next server for the next request
        changeServer();
        System.out.println("Server changed to: " + serverIndex);

        // Return the server's response as a string
        return response.toString();
    }

    /**
     * Cycles the server index to point to the next server in the list.
     * This is used to implement round-robin server selection.
     */
    private static void changeServer() {
        // Increment the server index and wrap around to 0 when reaching the end of the list
        serverIndex = (serverIndex + 1) % SERVERS.length;
    }
}
