package com.eci.arep.Controller;

import com.eci.arep.Annotation.PostMapping;
import com.eci.arep.Annotation.RequestBody;
import com.eci.arep.Server.WorkoutPlanner;
import java.util.Arrays;


public class WorkoutController {

    @PostMapping("/createWorkout")
public String createWorkout(@RequestBody String body) {
    System.out.println("Solicitud recibida: " + body);
    return "{\"message\": \"Rutina creada con exito\", \"body\": \"" + body + "\"}";
}


    public String getWorkout(String type, String level) {
        String[] workout = WorkoutPlanner.getWorkout(type, level);
        return "{\"workout\": " + Arrays.toString(workout) + "}";
    }
}