package edu.escuelaing.arep.webframework.example;

import static edu.escuelaing.arep.webframework.WebFramework.*;

public class App {
    public static void main(String[] args) {
        staticfiles("/webroot");
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
    }
}
