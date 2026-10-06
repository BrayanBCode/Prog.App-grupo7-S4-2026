package ServidorCentral.Logica.datatypes;

/**
 * Datos minimos de un usuario para mostrar en tablas/listas.
 * Nickname + mail forman la clave compuesta del usuario (ver UsuarioID).
 */
public record DTUsuarioResumen(String nickname, String mail) {
}
