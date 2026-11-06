package ServidorCentral.WebServices;

import ServidorCentral.Persistencia.AlmacenImagenes;
import jakarta.xml.ws.Endpoint;

import java.nio.file.Paths;

/**
 * Publica los Web Services del Servidor Central con el servidor HTTP de la JDK.
 * La URL sale de la propiedad del sistema "edext.ws.url"; pasa a ~/.edEXT con la configuracion externa (7.7).
 */
public class Publicador {

    public static final String URL_POR_DEFECTO = "http://localhost:9128/edext/central";

    public static Endpoint publicar() {
        sembrarImagenes();
        String url = System.getProperty("edext.ws.url", URL_POR_DEFECTO);
        Endpoint endpoint = Endpoint.publish(url, new ServicioCentral());
        System.out.println("Web Services publicados en " + url + "?wsdl");
        return endpoint;
    }

    /**
     * Copia las imagenes de los datos de prueba a la carpeta del central, sin pisar las que ya esten.
     * Antes lo hacia el web al arrancar, pero el web ya no ve esa carpeta: las pide por SOAP.
     * El origen es la propiedad "edext.imagenes.semilla" o la carpeta "imagenes" del directorio actual.
     */
    private static void sembrarImagenes() {
        String origen = System.getProperty("edext.imagenes.semilla", "imagenes");
        int copiadas = AlmacenImagenes.sembrar(Paths.get(origen));
        System.out.println("Imagenes en " + AlmacenImagenes.getCarpetaBase() + " (" + copiadas + " copiadas de " + origen + ")");
    }

    public static void main(String[] args) {
        publicar();
    }
}
