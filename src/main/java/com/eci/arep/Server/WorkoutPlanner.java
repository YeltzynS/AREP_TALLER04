package com.eci.arep.Server;

import java.util.*;

public class WorkoutPlanner {

    private static final Map<String, List<String>> WORKOUTS = new HashMap<>();

    static {
        // Fuerza
        WORKOUTS.put("strength-beginner", new ArrayList<>(Arrays.asList(
            "Sentadillas - 3 series de 10 repeticiones",
            "Flexiones - 3 series de 8 repeticiones",
            "Peso muerto con mancuerna - 3 series de 10 repeticiones"
        )));

        WORKOUTS.put("strength-intermediate", new ArrayList<>(Arrays.asList(
            "Dominadas - 3 series de 8 repeticiones",
            "Press de banca - 3 series de 10 repeticiones",
            "Peso muerto - 3 series de 10 repeticiones"
        )));

        WORKOUTS.put("strength-advanced", new ArrayList<>(Arrays.asList(
            "Clean and Jerk - 3 series de 5 repeticiones",
            "Sentadilla con barra - 3 series de 10 repeticiones",
            "Press militar - 3 series de 8 repeticiones"
        )));

        // Cardio
        WORKOUTS.put("cardio-beginner", new ArrayList<>(Arrays.asList(
            "Caminata rápida - 30 minutos",
            "Saltar la cuerda - 3 series de 1 minuto",
            "Burpees - 3 series de 10 repeticiones"
        )));

        WORKOUTS.put("cardio-intermediate", new ArrayList<>(Arrays.asList(
            "Carrera de 5 km",
            "Sprint 100m x 5 series",
            "Jumping Jacks - 3 series de 1 minuto"
        )));

        WORKOUTS.put("cardio-advanced", new ArrayList<>(Arrays.asList(
            "Carrera de 10 km",
            "Sprints de 200m x 8 series",
            "Burpees con salto - 3 series de 15 repeticiones"
        )));

        // Flexibilidad
        WORKOUTS.put("flexibility-beginner", new ArrayList<>(Arrays.asList(
            "Estiramiento de piernas - 10 minutos",
            "Estiramiento de espalda - 5 minutos",
            "Postura del perro boca abajo (yoga) - 1 minuto"
        )));

        WORKOUTS.put("flexibility-intermediate", new ArrayList<>(Arrays.asList(
            "Postura del guerrero (yoga) - 3 minutos",
            "Estiramiento de isquiotibiales - 5 minutos",
            "Estiramiento de espalda profunda - 5 minutos"
        )));

        WORKOUTS.put("flexibility-advanced", new ArrayList<>(Arrays.asList(
            "Postura del loto completa (yoga) - 5 minutos",
            "Estiramiento en split - 10 minutos",
            "Puente de espalda - 3 minutos"
        )));
    }

    public static String[] getWorkout(String type, String level) {
        String key = type + "-" + level;
        return WORKOUTS.getOrDefault(key, List.of("Rutina no encontrada.")).toArray(new String[0]);
    }

    public static void addWorkout(String type, String level, String exercise) {
        String key = type + "-" + level;
        WORKOUTS.putIfAbsent(key, new ArrayList<>());
        WORKOUTS.get(key).add(exercise);
    }

    public static boolean removeWorkout(String type, String level, String exercise) {
        String key = type + "-" + level;
        List<String> exercises = WORKOUTS.get(key);
        if (exercises != null) {
            return exercises.remove(exercise);
        }
        return false;
    }
}
