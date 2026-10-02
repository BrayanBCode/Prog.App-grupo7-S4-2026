package ServidorCentral.Logica.datatypes;

/**
 * Datos basicos de un docente para listas y tablas.
 * El texto a mostrar ("Nombre Apellido (nickname)") lo arma la presentacion.
 */
public record DTDocenteResumen(String nickname, String nombre, String apellido) {
}
