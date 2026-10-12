package ServidorCentral.Logica.datatypes;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;

import java.time.LocalDate;

/**
 * Una inscripcion de un estudiante a una edicion de curso, con su estado
 * (Seleccionar estudiantes, Listar aceptados, Listar resultados).
 */
public record DTInscripcionEdicion(
        String nickname,
        String mail,
        String nombre,
        String apellido,
        LocalDate fechaInscripcion,
        EstadoInscripcion estado) {
}
