package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;

/**
 * Consulta de edicion de curso. cupo == 0 significa "sin limite".
 * imagen: ruta relativa a la carpeta de imagenes; null si no tiene.
 */
public record DTEdicionCurso(
        String nombre,
        String nombreCurso,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        int cupo,
        LocalDate fechaPublicacion,
        String imagen) {
}
