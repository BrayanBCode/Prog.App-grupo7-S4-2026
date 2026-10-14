package ServidorCentral.Logica.datatypes;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;

import java.time.LocalDate;

/**
 * Una inscripcion de un estudiante vista desde el estudiante (Consulta de Usuario,
 * Listar resultados de inscripciones): a que edicion/curso y en que estado esta.
 */
public record DTInscripcionEstudiante(
        String edicion,
        String curso,
        LocalDate fechaInscripcion,
        EstadoInscripcion estado) {
}
