package edu.escuelaing.arep.webframework;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebFrameworkTest {

    @Test
    public void testRequestQueryParsingSingleParam() {
        Request req = new Request("/test", "name=Pedro");
        assertEquals("Pedro", req.getValues("name"));
        assertEquals("/test", req.getPath());
    }

    @Test
    public void testRequestQueryParsingMultipleParams() {
        Request req = new Request("/test", "name=Pedro&age=30&city=Bogota");
        assertEquals("Pedro", req.getValues("name"));
        assertEquals("30", req.getValues("age"));
        assertEquals("Bogota", req.getValues("city"));
        assertEquals("/test", req.getPath());
    }

    @Test
    public void testEmptyQuery() {
        Request req = new Request("/hello", "");
        assertNull(req.getValues("name"));
    }

    @Test
    public void testMalformedOrMissingValueQuery() {
        Request req = new Request("/hello", "name=&age");
        assertEquals("", req.getValues("name"));
        assertEquals("", req.getValues("age"));
    }

    @Test
    public void testResponseProperties() {
        Response res = new Response();
        assertEquals(200, res.getStatusCode());
        assertEquals("text/html", res.getContentType());

        res.setStatusCode(404);
        res.setContentType("application/json");
        res.setBody("{\"error\": \"not found\"}");

        assertEquals(404, res.getStatusCode());
        assertEquals("application/json", res.getContentType());
        assertEquals("{\"error\": \"not found\"}", res.getBody());
    }

    @Test
    public void testIntegrationGetRoute() throws Exception {
        // Register route and auto-start server
        WebFramework.get("/integration-test", (req, res) -> "Integration success: " + req.getValues("key"));
        
        URL url = new URL("http://localhost:8080/integration-test?key=value123");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        int responseCode = con.getResponseCode();
        assertEquals(200, responseCode);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        
        assertEquals("Integration success: value123", content.toString());
    }

    @Test
    public void testIntegration404NotFound() throws Exception {
        // Ensure server is started using a dummy registration
        WebFramework.get("/dummy", (req, res) -> "dummy");
        
        URL url = new URL("http://localhost:8080/does-not-exist");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        int responseCode = con.getResponseCode();
        // Since the route does not exist and it is not a valid static file, it will return 404
        assertEquals(404, responseCode);
    }
}
