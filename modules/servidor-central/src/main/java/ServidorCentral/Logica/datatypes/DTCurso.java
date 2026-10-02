package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;
import java.util.List;

/**
 * Consulta de curso.
 * <ul>
 *   <li>docente: null si el curso no tiene docente asignado.</li>
 *   <li>previas: nombres de los cursos previos; nunca null (puede estar vacia).</li>
 * </ul>
 */
public record DTCurso(
        String nombre,
        String descripcion,
        int duracion,
        float cantHoras,
        int cantCreditos,
        String url,
        LocalDate fechaRegistro,
        String instituto,
        DTDocenteResumen docente,
        List<String> previas) {

    public DTCurso {
        previas = previas == null ? List.of() : List.copyOf(previas);
    }
}
