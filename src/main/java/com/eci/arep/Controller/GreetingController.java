/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eci.arep.Controller;



import com.eci.arep.Annotation.GetMapping;
import com.eci.arep.Annotation.RequestParam;
import com.eci.arep.Annotation.RestController;
import com.eci.arep.Server.Request;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
public String getGreeting(@RequestParam("name") String name) {
    return "{\"message\": \"Bienvenido, " + (name.isEmpty() ? "Usuario" : name) + " a tu rutina.\"}";
}

}

