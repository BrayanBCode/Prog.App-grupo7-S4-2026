package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;
import java.util.List;

/**
 * Consulta de curso.
 * <ul>
 *   <li>docente: null si el curso no tiene docente asignado.</li>
 *   <li>previas: nombres de los cursos previos; nunca null (puede estar vacia).</li>
 *   <li>imagen: ruta relativa a la carpeta de imagenes; null si no tiene.</li>
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
        String imagen) {

    public DTCurso {
        previas = previas == null ? List.of() : List.copyOf(previas);
    }

    /** Constructor sin imagen. */
    public DTCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos,
                   String url, LocalDate fechaRegistro, String instituto, DTDocenteResumen docente,
                   List<String> previas) {
        this(nombre, descripcion, duracion, cantHoras, cantCreditos, url, fechaRegistro, instituto, docente, previas, null);
    }
}
