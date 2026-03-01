package edu.escuelaing.arep.webframework;

@FunctionalInterface
public interface Route {
    String handle(Request req, Response res);
}
