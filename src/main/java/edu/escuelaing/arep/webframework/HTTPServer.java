package edu.escuelaing.arep.webframework;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class HTTPServer {
    private boolean running = false;
    private WebFramework framework;

    public HTTPServer(WebFramework framework) {
        this.framework = framework;
    }

    public boolean isRunning() {
        return running;
    }

    public void start(int port) {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(out, true)) {

            String inputLine = in.readLine();
            if (inputLine == null)
                return;

            System.out.println("Request: " + inputLine);
            String[] tokens = inputLine.split(" ");
            if (tokens.length < 2)
                return;

            String method = tokens[0];
            String uri = tokens[1];

            String path = uri;
            String queryStr = "";
            if (uri.contains("?")) {
                int qIndex = uri.indexOf("?");
                path = uri.substring(0, qIndex);
                queryStr = uri.substring(qIndex + 1);
            }

            // Consume rest of headers
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.isEmpty())
                    break;
            }

            if (framework.getRoute(path) != null) {
                Route route = framework.getRoute(path);
                Request req = new Request(path, queryStr);
                Response res = new Response();

                String result = route.handle(req, res);
                res.setBody(result);

                sendResponse(writer, res.getStatusCode(), res.getContentType(), res.getBody());
            } else {
                // Try static files
                if (framework.getStaticFolder() != null) {
                    serveStaticFile(path, out, writer);
                } else {
                    send404(writer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serveStaticFile(String path, OutputStream out, PrintWriter writer) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }

        String folder = framework.getStaticFolder();
        if (folder.startsWith("/"))
            folder = folder.substring(1);

        // Search in target/classes as requested by user
        String filePath = "target/classes/" + folder + path;
        File file = new File(filePath);
        if (!file.exists()) {
            // Fallback search in src/main/resources
            filePath = "src/main/resources/" + folder + path;
            file = new File(filePath);
        }

        if (file.exists() && file.isFile()) {
            String contentType = guessContentType(path);
            byte[] fileData = Files.readAllBytes(file.toPath());

            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: " + contentType);
            writer.println("Content-Length: " + fileData.length);
            writer.println();
            writer.flush();
            out.write(fileData);
            out.flush();
        } else {
            send404(writer);
        }
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html"))
            return "text/html";
        if (path.endsWith(".css"))
            return "text/css";
        if (path.endsWith(".js"))
            return "application/javascript";
        if (path.endsWith(".png"))
            return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
            return "image/jpeg";
        return "text/plain";
    }

    private void sendResponse(PrintWriter writer, int statusCode, String contentType, String body) {
        writer.println("HTTP/1.1 " + statusCode + " OK");
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + body.length());
        writer.println();
        writer.print(body);
        writer.flush();
    }

    private void send404(PrintWriter writer) {
        String body = "404 Not Found";
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/plain");
        writer.println("Content-Length: " + body.length());
        writer.println();
        writer.print(body);
        writer.flush();
    }
}
