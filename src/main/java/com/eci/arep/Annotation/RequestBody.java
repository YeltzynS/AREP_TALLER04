package com.eci.arep.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para marcar parámetros que deben recibir el cuerpo de la solicitud HTTP.
 * Esta anotación se usa en métodos de controladores para indicar que un parámetro
 * debe ser mapeado al cuerpo de la solicitud.
 */
@Retention(RetentionPolicy.RUNTIME) // La anotación estará disponible en tiempo de ejecución
@Target(ElementType.PARAMETER) // La anotación solo se puede aplicar a parámetros de métodos
public @interface RequestBody {
}