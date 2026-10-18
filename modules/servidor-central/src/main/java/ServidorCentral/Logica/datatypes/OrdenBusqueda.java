package ServidorCentral.Logica.datatypes;

/**
 * Orden de los resultados de la Busqueda (requerimiento 7.5, "Ordenar por"). Sin orden elegido se usa ALFABETICO_ASC.
 * Alfabetico: por nombre, sin distinguir mayusculas ni tildes. Año: año de registro del curso; dentro del mismo año,
 * A-Z; los cursos sin fecha van al final.
 */
public enum OrdenBusqueda {
    ALFABETICO_ASC,
    ALFABETICO_DESC,
    ANIO_ASC,
    ANIO_DESC
}
