package escuelaing.edu.arep.awsapp;

import static spark.Spark.port;

import java.util.List;

import static spark.Spark.get;

public class LogService {
    private static final MongoUtil mongoUtil = new MongoUtil();
    public static void main(String[] args) {
        port(getPort());
        get("/logservice", (req, res) -> saveLog(req.queryParams("log")));
    }

    private static List<String> saveLog(String log) {
        mongoUtil.addLog(log);
        return mongoUtil.getLogs();
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }
}
