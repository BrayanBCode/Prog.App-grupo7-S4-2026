package ServidorCentral.Logica.excepciones;

/**
 * Una regla de negocio no se cumple (nickname repetido, edicion sin cupo, etc.). El mensaje es
 * para el usuario: la presentacion lo muestra tal cual. Cualquier otra excepcion es un error
 * inesperado y se muestra con un mensaje generico.
 * <p>
 * Extiende IllegalArgumentException para que los {@code catch} que ya existen en Swing y en el
 * Servidor Web sigan funcionando mientras se migra cada caso de uso.
 */
public class ReglaNegocioException extends IllegalArgumentException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
