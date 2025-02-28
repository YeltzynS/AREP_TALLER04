package com.eci.arep;

import com.eci.arep.Annotation.GetMapping;
import com.eci.arep.Annotation.PostMapping;
import com.eci.arep.Annotation.RequestBody;
import com.eci.arep.Annotation.RequestParam;
import com.eci.arep.Annotation.RestController;
import com.eci.arep.Controller.GreetingController;
import com.eci.arep.Controller.WorkoutController;
import com.eci.arep.Server.HttpServer;
import com.eci.arep.Server.Request;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class SpringApplication {

    public static void main(String[] args) {
        // Agregar un hook para detener el servidor cuando la aplicación termine
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Deteniendo el servidor...");
            HttpServer.stop();
        }));

        System.out.println("Iniciando SpringApplication...");

        // Cargar los controladores
        loadControllers();

        // Iniciar el servidor HTTP en el puerto 35000
        try {
            HttpServer.start(35000);
            System.out.println("Servidor iniciado en el puerto 35000...");
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    /**
     * Carga los controladores de la aplicación.
     */
    private static void loadControllers() {
        // Registrar manualmente los controladores
        registerController(new GreetingController());
        registerController(new WorkoutController());
    }

    /**
     * Registra un controlador en el servidor HTTP.
     *
     * @param controllerInstance Instancia del controlador.
     */
    private static void registerController(Object controllerInstance) {
        Class<?> controllerClass = controllerInstance.getClass();

        // Verificar si la clase está anotada con @RestController
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            System.err.println("La clase " + controllerClass.getName() + " no está anotada con @RestController.");
            return;
        }

        // Obtener todos los métodos de la clase
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            // Registrar métodos anotados con @GetMapping
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                String path = getMapping.value();
                HttpServer.get(path, (req, res) -> invokeControllerMethod(method, controllerInstance, req));
            }

            // Registrar métodos anotados con @PostMapping
            if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                String path = postMapping.value();
                HttpServer.post(path, (req, res) -> invokeControllerMethod(method, controllerInstance, req));
            }
        }
    }

    /**
     * Invoca un método del controlador, inyectando los parámetros según las anotaciones.
     *
     * @param method              Método a invocar.
     * @param controllerInstance  Instancia del controlador.
     * @param request             Objeto Request con los datos de la solicitud.
     * @return Respuesta del método como una cadena.
     */
    private static String invokeControllerMethod(Method method, Object controllerInstance, Request request) {
        try {
            // Obtener los parámetros del método
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            // Procesar cada parámetro
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];

                // Inyectar el cuerpo de la solicitud si está anotado con @RequestBody
                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    args[i] = request.getBody(); // Aquí podrías parsear el JSON si es necesario
                }

                // Inyectar parámetros de la consulta si está anotado con @RequestParam
                else if (parameter.isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    String paramName = requestParam.value();
                    String paramValue = request.getQueryParams().get(paramName);

                    // Usar el valor por defecto si no se proporciona el parámetro
                    if (paramValue == null || paramValue.isEmpty()) {
                        paramValue = requestParam.defaultValue();
                    }

                    args[i] = paramValue;
                }
            }

            // Invocar el método y devolver el resultado como una cadena
            Object result = method.invoke(controllerInstance, args);
            return result != null ? result.toString() : "";
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "{\"error\": \"Error interno del servidor\"}";
        }
    }
}