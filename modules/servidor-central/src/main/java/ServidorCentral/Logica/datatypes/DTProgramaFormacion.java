package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;
import java.util.List;

/**
 * Consulta de programa de formacion. Las fechas pueden ser null.
 * cursos: nombres de los cursos del programa; nunca null (puede estar vacia).
 */
public record DTProgramaFormacion(
        String nombre,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDate fechaAlta,
        List<String> cursos) {

    public DTProgramaFormacion {
        cursos = cursos == null ? List.of() : List.copyOf(cursos);
    }
}
