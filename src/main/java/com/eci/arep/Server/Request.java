package com.eci.arep.Server;

import java.io.*;
import java.util.*;

public class Request {
    private final String path;
    private final Map<String, String> queryParams;

    public Request(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new IOException("Error leyendo la línea de la solicitud.");
        }

        String[] parts = requestLine.split(" ");
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
    }

    public String getPath() {
        return path;
    }

    public String getValues(String key) {
        return queryParams.getOrDefault(key, "");
    }
}