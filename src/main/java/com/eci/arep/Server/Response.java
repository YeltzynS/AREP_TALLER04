package com.eci.arep.Server;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Response {
    private final OutputStream out;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void send(String response) throws IOException {
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    public void sendJson(String jsonResponse) throws IOException {
        byte[] jsonBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        String headers = "HTTP/1.1 200 OK\r\n"
                       + "Content-Type: application/json; charset=UTF-8\r\n"
                       + "Content-Length: " + jsonBytes.length + "\r\n"
                       + "\r\n";
        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(jsonBytes);
        out.flush();
    }

    public void sendFile(byte[] fileContent, String contentType) throws IOException {
        String headers = "HTTP/1.1 200 OK\r\n"
                       + "Content-Type: " + contentType + "; charset=UTF-8\r\n"
                       + "Content-Length: " + fileContent.length + "\r\n"
                       + "\r\n";
        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(fileContent);
        out.flush();
    }

    public void sendError(int statusCode, String message) throws IOException {
        String errorJson = "{\"error\": \"" + message + "\"}";
        sendJson(errorJson);
    }
}
