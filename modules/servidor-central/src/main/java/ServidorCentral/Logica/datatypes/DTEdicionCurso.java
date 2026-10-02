package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;

/**
 * Consulta de edicion de curso. cupo == 0 significa "sin limite".
 */
public record DTEdicionCurso(
        String nombre,
        String nombreCurso,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        int cupo,
        LocalDate fechaPublicacion) {
}
