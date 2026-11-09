package ServidorCentral.DataTypes;

/**
 * Fila de la tabla de cursos (Swing) y tarjeta de la lista de cursos de un instituto (web).
 * imagen puede ser null. promedio y valoraciones solo vienen cargados en la Busqueda (7.5 y 7.6, estrellas en los
 * resultados); en el resto quedan en 0.
 */
public record DTCursoResumen(String nombre, String descripcion, String imagen, double promedio, int valoraciones) {

    /** Sin valoraciones (promedio 0). */
    public DTCursoResumen(String nombre, String descripcion, String imagen) {
        this(nombre, descripcion, imagen, 0, 0);
    }
}
