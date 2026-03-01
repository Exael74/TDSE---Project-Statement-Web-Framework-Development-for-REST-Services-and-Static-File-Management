package edu.escuelaing.arep.webframework;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String path;
    private Map<String, String> queryParams;

    public Request(String path, String queryStr) {
        this.path = path;
        this.queryParams = new HashMap<>();
        parseQuery(queryStr);
    }

    private void parseQuery(String queryStr) {
        if (queryStr != null && !queryStr.isEmpty()) {
            String[] pairs = queryStr.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    queryParams.put(kv[0], kv[1]);
                } else if (kv.length == 1) {
                    queryParams.put(kv[0], "");
                }
            }
        }
    }

    public String getValues(String key) {
        return queryParams.get(key);
    }

    public String getPath() {
        return path;
    }
}
