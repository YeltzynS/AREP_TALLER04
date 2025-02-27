/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eci.arep.Controller;


import com.eci.arep.Annotation.GetMapping;
import com.eci.arep.Annotation.RequestParam;
import com.eci.arep.Annotation.RestController;
import com.eci.arep.Server.WorkoutPlanner;
import com.eci.arep.Annotation.*;
import java.util.Arrays;


@RestController
public class WorkoutController {

   @GetMapping("/workout")
public String getWorkout(@RequestParam("type") String type, @RequestParam("level") String level) {
    String[] workout = WorkoutPlanner.getWorkout(type, level);
    return "{\"workout\": " + Arrays.toString(workout) + "}";
}



}

