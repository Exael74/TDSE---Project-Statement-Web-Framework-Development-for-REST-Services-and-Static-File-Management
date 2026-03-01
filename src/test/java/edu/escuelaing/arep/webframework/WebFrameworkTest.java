package edu.escuelaing.arep.webframework;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebFrameworkTest {

    @Test
    public void testRequestQueryParsing() {
        Request req = new Request("/test", "name=Pedro&age=30");
        assertEquals("Pedro", req.getValues("name"));
        assertEquals("30", req.getValues("age"));
        assertEquals("/test", req.getPath());
    }

    @Test
    public void testEmptyQuery() {
        Request req = new Request("/hello", "");
        assertNull(req.getValues("name"));
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
}
