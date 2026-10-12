package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;
import java.util.List;

/**
 * Consulta de curso.
 * <ul>
 *   <li>docente: null si el curso no tiene docente asignado.</li>
 *   <li>previas: nombres de los cursos previos; nunca null (puede estar vacia).</li>
 *   <li>categorias: nombres de las categorias del curso; nunca null (puede estar vacia).</li>
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
        List<String> previas,
        List<String> categorias) {

    public DTCurso {
        previas = previas == null ? List.of() : List.copyOf(previas);
        categorias = categorias == null ? List.of() : List.copyOf(categorias);
    }

    /** Constructor sin categorias (curso sin categoria asociada). */
    public DTCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos,
                   String url, LocalDate fechaRegistro, String instituto, DTDocenteResumen docente,
                   List<String> previas) {
        this(nombre, descripcion, duracion, cantHoras, cantCreditos, url, fechaRegistro, instituto, docente, previas, null);
    }
}
