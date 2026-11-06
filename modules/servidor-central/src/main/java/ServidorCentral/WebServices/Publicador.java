package ServidorCentral.WebServices;

import ServidorCentral.Persistencia.Configuracion;
import jakarta.xml.ws.Endpoint;

/**
 * Publica los Web Services del Servidor Central con el servidor HTTP de la JDK,
 * en la URL "ws.url" de ~/.edEXT/servidor.properties.
 */
public class Publicador {

    public static Endpoint publicar() {
        String url = Configuracion.get().valor(Configuracion.WS_URL);
        if (url == null) {
            throw new IllegalStateException("Falta ws.url en ~/.edEXT/servidor.properties");
        }
        Endpoint endpoint = Endpoint.publish(url, new ServicioCentral());
        System.out.println("Web Services publicados en " + url + "?wsdl");
        return endpoint;
    }

    public static void main(String[] args) {
        publicar();
    }
}
