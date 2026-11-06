package ServidorWeb.ws;

/**
 * Regla de negocio rota en el Servidor Central (llega como ReglaNegocioFault): el mensaje
 * es para mostrarselo al usuario tal cual.
 */
public class ErrorNegocio extends RuntimeException {

    public ErrorNegocio(String mensaje) {
        super(mensaje);
    }
}
