package escuelaing.edu.arep.awsapp;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        port(getPort());
        staticFiles.location("/public");
        get("/log", (req, res) -> RRInvoker.invoke(req.queryParams("msg")));
    }
    
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35001;
    }

}
