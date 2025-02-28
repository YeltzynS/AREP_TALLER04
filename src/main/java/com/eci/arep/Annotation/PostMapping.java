package com.eci.arep.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para marcar métodos que manejan solicitudes HTTP POST.
 * El valor de la anotación especifica la ruta (endpoint) que el método manejará.
 */
@Retention(RetentionPolicy.RUNTIME) // La anotación estará disponible en tiempo de ejecución
@Target(ElementType.METHOD) // La anotación solo se puede aplicar a métodos
public @interface PostMapping {
    String value();
}