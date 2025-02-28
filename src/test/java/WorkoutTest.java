import com.eci.arep.Server.HttpServer;
import com.eci.arep.Server.WorkoutPlanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.net.*;

public class WorkoutTest {

    @Test
    void testGetWorkoutStrengthBeginner() {
        String[] workout = WorkoutPlanner.getWorkout("strength", "beginner");
        assertNotNull(workout, "La rutina no debe ser nula");
        assertEquals(3, workout.length, "Debe haber 3 ejercicios en la rutina");
    }

    @Test
    void testGetWorkoutCardioAdvanced() {
        String[] workout = WorkoutPlanner.getWorkout("cardio", "advanced");
        assertNotNull(workout, "La rutina no debe ser nula");
        assertTrue(workout[0].contains("Carrera de 10 km"), "El primer ejercicio debe ser 'Carrera de 10 km'");
    }

    @Test
    void testGetWorkoutFlexibilityIntermediate() {
        String[] workout = WorkoutPlanner.getWorkout("flexibility", "intermediate");
        assertNotNull(workout, "La rutina no debe ser nula");
        assertEquals(3, workout.length, "Debe haber 3 ejercicios en la rutina");
    }

    @Test
    void testGetWorkoutInvalidType() {
        String[] workout = WorkoutPlanner.getWorkout("unknown", "beginner");
        assertEquals(1, workout.length, "Debe devolver un solo mensaje");
        assertEquals("Rutina no encontrada.", workout[0], "Debe indicar que la rutina no se encontró");
    }



    @Test
    void testGetWorkoutStrengthAdvanced() {
        String[] workout = WorkoutPlanner.getWorkout("strength", "advanced");
        assertNotNull(workout, "La rutina no debe ser nula");
        assertEquals(3, workout.length, "Debe haber 3 ejercicios en la rutina avanzada");
    }

    @Test
    void testGetWorkoutCardioBeginner() {
        String[] workout = WorkoutPlanner.getWorkout("cardio", "beginner");
        assertNotNull(workout, "La rutina no debe ser nula");
        assertEquals(3, workout.length, "Debe haber 3 ejercicios en la rutina de cardio");
    }

    @Test
    void testGetWorkoutFlexibilityAdvanced() {
        String[] workout = WorkoutPlanner.getWorkout("flexibility", "advanced");
        assertNotNull(workout, "La rutina no debe ser nula");
        assertEquals(3, workout.length, "Debe haber 3 ejercicios en la rutina avanzada");
    }

}
