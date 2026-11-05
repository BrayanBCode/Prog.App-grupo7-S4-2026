package ServidorCentral.WebServices;

import jakarta.xml.ws.Endpoint;

/**
 * Publica los Web Services del Servidor Central con el servidor HTTP de la JDK.
 * La URL sale de la propiedad del sistema "edext.ws.url"; pasa a ~/.edEXT con la configuracion externa (7.7).
 */
public class Publicador {

    public static final String URL_POR_DEFECTO = "http://localhost:9128/edext/central";

    public static Endpoint publicar() {
        String url = System.getProperty("edext.ws.url", URL_POR_DEFECTO);
        Endpoint endpoint = Endpoint.publish(url, new ServicioCentral());
        System.out.println("Web Services publicados en " + url + "?wsdl");
        return endpoint;
    }

    public static void main(String[] args) {
        publicar();
    }
}
