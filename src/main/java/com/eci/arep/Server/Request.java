package com.eci.arep.Server;

import java.io.*;
import java.util.*;

public class Request {
    private final String path;
    private final Map<String, String> queryParams;
    private final String method;
    private String body;

    public Request(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new IOException("Error leyendo la línea de la solicitud.");
        }

        String[] parts = requestLine.split(" ");
        this.method = parts[0]; // Obtener el método HTTP (GET, POST, etc.)
        String fullPath = parts.length > 1 ? parts[1] : "/";
        String[] pathAndQuery = fullPath.split("\\?");
        this.path = pathAndQuery[0];
        this.queryParams = new HashMap<>();

        if (pathAndQuery.length > 1) {
            String[] pairs = pathAndQuery[1].split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }

        // Leer encabezados para obtener Content-Length
        int contentLength = 0;
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        // Leer el cuerpo de la solicitud si es POST
        if ("POST".equals(method) && contentLength > 0) {
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars, 0, contentLength);
            this.body = new String(bodyChars);
        } else {
            this.body = "";
        }
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getValues(String key) {
        return queryParams.getOrDefault(key, "");
    }

    public String getBody() {
        return body; // Devuelve el cuerpo de la solicitud
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
