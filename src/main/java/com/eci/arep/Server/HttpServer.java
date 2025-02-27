package com.eci.arep.Server;

import com.eci.arep.Controller.GreetingController;
import com.eci.arep.Controller.WorkoutController;
import com.eci.arep.Server.Response;
import com.eci.arep.Server.WorkoutPlanner;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class HttpServer {

    private static final String STATIC_FOLDER = "src/main/resources/static";
    private static final Map<String, BiFunction<Request, Response, String>> routes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Servidor iniciado en el puerto 8080...");

        // Agregar rutas de ejemplo usando lambda
        get("/hello", (req, res) -> "<html><body><h1>Hello, Gymrat!</h1></body></html>");
        get("/greet", (req, res) -> {
            String name = req.getValues("name");  // Obtener el parámetro 'name' de la consulta
            if (name == null || name.isEmpty()) {
                return "<html><body><h1>Hello, Stranger!</h1></body></html>";
            } else {
                return "<html><body><h1>Hello, " + name + "!</h1></body></html>";
            }
        });

        get("/api", (req, res) -> "<html><body><h1>Hello, " + req.getValues("name") + "</h1></body></html>");

        // **Registrar automáticamente los controladores antes de iniciar el servidor**
        loadControllers();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleRequest(clientSocket);
        }
    }
    private static void loadControllers() {
    try {
        // Registrar manualmente los controladores
        WorkoutController workoutController = new WorkoutController();
        GreetingController greetingController = new GreetingController();

        routes.put("/workout", (req, res) -> workoutController.getWorkout(req.getValues("type"), req.getValues("level")));
        routes.put("/greeting", (req, res) -> greetingController.getGreeting(req.getValues("name")));

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void get(String path, BiFunction<Request, Response, String> handler) {
        routes.put(path, handler);
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {
            
            Request request = new Request(in);
            Response response = new Response(out);
            
            String filePath = request.getPath();
            
            if (filePath.equals("/api")) {
                response.send("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Hello " + request.getValues("name") + "</h1>");
            } else if (filePath.startsWith("/api/workout")) {
                handleWorkoutRequest(response, request);
            } else {
                // Si la ruta coincide con alguna definida, usamos la función correspondiente
                if (routes.containsKey(filePath)) {
                    String responseBody = routes.get(filePath).apply(request, response);
                    response.send("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n" + responseBody);
                } else {
                    serveStaticFile(response, filePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al manejar la solicitud: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private static void handleWorkoutRequest(Response response, Request request) throws IOException {
        try {
            // Obtener los parámetros de la solicitud
            String type = request.getValues("type");
            String level = request.getValues("level");
            String name = request.getValues("name");  // Extraer el nombre de la consulta
    
            // Validar parámetros
            if (type == null || level == null || type.isEmpty() || level.isEmpty()) {
                response.send("HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nFaltan parametros");
                return;
            }
    
            // Validar el tipo de ejercicio
            if (type.equals("running")) {
                type = "cardio";
            }
    
            // Obtener la rutina de ejercicios
            String[] workout = WorkoutPlanner.getWorkout(type, level);
    
            // Crear el JSON de respuesta
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{");
            jsonResponse.append("\"name\":\"").append(name.isEmpty() ? "Stranger" : name).append("\",");  // Agregar el nombre al JSON
            jsonResponse.append("\"workout\":[");

            // Agregar las rutinas de ejercicios al JSON
            for (int i = 0; i < workout.length; i++) {
                jsonResponse.append("\"").append(workout[i]).append("\"");
                if (i < workout.length - 1) {
                    jsonResponse.append(",");
                }
            }

            jsonResponse.append("]}");

            // Enviar el JSON como respuesta
            response.sendJson(jsonResponse.toString());
    
        } catch (Exception e) {
            response.send("HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nError en la solicitud");
        }
    }

    private static void serveStaticFile(Response response, String filePath) throws IOException {
        if (filePath.equals("/")) filePath = "/index.html";

        Path file = Paths.get(STATIC_FOLDER, filePath).toAbsolutePath();
        System.out.println("Buscando archivo en: " + file.toString());

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
        if (filePath.endsWith(".png")) return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
}
