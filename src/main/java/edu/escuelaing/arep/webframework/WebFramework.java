package edu.escuelaing.arep.webframework;

import java.util.HashMap;
import java.util.Map;

public class WebFramework {
    private static WebFramework instance = new WebFramework();
    private Map<String, Route> routes;
    private String staticFolder;
    private HTTPServer server;

    private WebFramework() {
        routes = new HashMap<>();
        server = new HTTPServer(this);
    }

    public static void get(String path, Route route) {
        instance.routes.put(path, route);
        instance.startServer();
    }

    public static void staticfiles(String folder) {
        instance.staticFolder = folder;
        instance.startServer();
    }

    private void startServer() {
        if (!server.isRunning()) {
            new Thread(() -> server.start(8080)).start();
            try {
                Thread.sleep(500); // Give server time to start
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Route getRoute(String path) {
        return routes.get(path);
    }

    public String getStaticFolder() {
        return staticFolder;
    }
}
