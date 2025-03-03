package com.eci.arep.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class HttpServer {

    private static final String STATIC_FOLDER = "src/main/resources/static";
    private static final Map<String, BiFunction<Request, Response, String>> routes = new HashMap<>();
    private static final Map<String, BiFunction<Request, Response, String>> postRoutes = new HashMap<>();
    private static ServerSocket serverSocket;

    public static void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en el puerto " + port + "...");

        // Registrar rutas de los controladores
        registerControllers();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            // Crear un nuevo hilo para manejar la solicitud
            new Thread(() -> {
                try {
                    handleRequest(clientSocket);
                } catch (Exception e) {
                    System.err.println("Error al manejar la solicitud: " + e.getMessage());
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    }
                }
            }).start();
        }
    }

    public static void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido.");
            }
        } catch (IOException e) {
            System.err.println("Error al detener el servidor: " + e.getMessage());
        }
    }

    public static void get(String path, BiFunction<Request, Response, String> handler) {
        routes.put(path, handler);
    }

    public static void post(String path, BiFunction<Request, Response, String> handler) {
        postRoutes.put(path, handler);
    }

    private static void registerControllers() {
        get("/getWorkout", (req, res) -> {
            String type = req.getValues("type");
            String level = req.getValues("level");

            if (type.isEmpty() || level.isEmpty()) {
                return "{\"error\": \"Faltan parámetros type y level\"}";
            }

            return "{\"exercises\": " + Arrays.toString(WorkoutPlanner.getWorkout(type, level)) + "}";
        });

        post("/addWorkout", (req, res) -> {
            Map<String, String> params = parseJson(req.getBody());
            String type = params.get("type");
            String level = params.get("level");
            String exercise = params.get("exercise");

            if (type == null || level == null || exercise == null) {
                return "{\"error\": \"Faltan parámetros\"}";
            }

            WorkoutPlanner.addWorkout(type, level, exercise);
            return "{\"message\": \"Ejercicio agregado exitosamente\"}";
        });
    }

    private static Map<String, String> parseJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            Request request = new Request(in);
            Response response = new Response(out);

            String filePath = request.getPath();
            String method = request.getMethod();

            if (method.equals("POST") && postRoutes.containsKey(filePath)) {
                String responseBody = postRoutes.get(filePath).apply(request, response);
                response.send("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + responseBody);
            } else if (routes.containsKey(filePath)) {
                String responseBody = routes.get(filePath).apply(request, response);
                response.send("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + responseBody);
            } else {
                serveStaticFile(response, filePath);
            }
        } catch (IOException e) {
            System.err.println("Error al manejar la solicitud: " + e.getMessage());
        }
    }

    private static void serveStaticFile(Response response, String filePath) throws IOException {
        if (filePath.equals("/")) filePath = "/index.html";

        Path file = Paths.get(STATIC_FOLDER, filePath).toAbsolutePath();

        if (Files.exists(file) && !Files.isDirectory(file)) {
            String contentType = getContentType(filePath);
            byte[] fileContent = Files.readAllBytes(file);
            response.sendFile(fileContent, contentType);
        } else {
            response.send("HTTP/1.1 404 Not Found\r\n\r\nArchivo no encontrado.");
        }
    }

    private static String getContentType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".js")) return "application/javascript";
        return "text/plain";
    }
}