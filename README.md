# Web Framework

This project is a fully functional web framework for Java, designed to develop web applications with backend REST services. It supports HTML files, JavaScript, CSS, images, and REST services using lambda functions with query parameter extraction.

## Architecture

The framework is composed of the following core components:
- **`WebFramework`**: The main facade of the library. It provides static methods (`get()`, `staticfiles()`) to map URL paths to lambda functions or static directories.
- **`HTTPServer`**: A Java Socket-based basic HTTP server implementation. It listens on port 8080, reads the HTTP requests, delegates to matching REST routes or serves static files, and sends back the HTTP responses.
- **`Request`**: Encapsulates the HTTP request details. It parses the request URI and query parameters, providing standard access via `req.getValues(key)`.
- **`Response`**: Encapsulates the HTTP response details, allowing developers to set headers like Content-Type, or status codes, and the body payload.
- **`Route`**: A functional interface that developers implement via lambda expressions to handle incoming requests and produce output.

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven
- Git

### Installation & Execution
1. Clone the repository: `git clone <repo-url>`
2. Compile and package the project:
   ```bash
   mvn clean package
   ```
3. Run the example App:
   ```bash
   mvn exec:java -Dexec.mainClass="edu.escuelaing.arep.webframework.example.App"
   ```
   *Alternatively, run the `App` class from your IDE.*

### Example Usage
```java
import static edu.escuelaing.arep.webframework.WebFramework.*;

public class App {
    public static void main(String[] args) {
        // Define directory for static files (e.g. src/main/resources/webroot)
        staticfiles("/webroot");
        
        // Define REST endpoint with query parameters
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        
        // Define REST endpoint returning mathematical PI
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });
    }
}
```

Once running, navigate to:
- **REST Service with Query Param:** http://localhost:8080/App/hello?name=Pedro
- **Basic REST Service:** http://localhost:8080/App/pi
- **Static File Resource:** http://localhost:8080/index.html

## Tests Overview
The framework is verified using automated unit tests written in JUnit. Tests prove the reliability of HTTP query parameter parsing and response defaults.
To execute tests, run:
```bash
mvn test
```
