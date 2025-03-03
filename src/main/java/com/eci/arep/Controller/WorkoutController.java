package com.eci.arep.Controller;

import org.springframework.web.bind.annotation.*;
import com.eci.arep.Server.WorkoutPlanner;
import java.util.Arrays;

@RestController 
public class WorkoutController {

    @PostMapping("/createWorkout") 
    public String createWorkout(@RequestBody String body) {
        System.out.println("Solicitud recibida: " + body);
        return "{\"message\": \"Rutina creada con exito\", \"body\": \"" + body + "\"}";
    }

    @GetMapping("/getWorkout") 
    public String getWorkout(@RequestParam String type, @RequestParam String level) {
        String[] workout = WorkoutPlanner.getWorkout(type, level);
        return "{\"workout\": " + Arrays.toString(workout) + "}";
    }
}