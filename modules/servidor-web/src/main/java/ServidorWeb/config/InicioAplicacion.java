package ServidorWeb.config;

import ServidorCentral.Persistencia.AlmacenImagenes;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.nio.file.Paths;

/**
 * Al levantar la aplicacion web se copian las imagenes de los datos de prueba (que vienen en el
 * WAR, en /imagenes) a la carpeta fija del Servidor Central, sin pisar las que ya esten.
 * Las imagenes se sirven desde esa carpeta (ver ImagenesServlet), no desde el WAR: asi las
 * fotos subidas no se pierden al redesplegar.
 */
@WebListener
public class InicioAplicacion implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ServletContext contexto = evento.getServletContext();
        String imagenesWar = contexto.getRealPath("/imagenes");
        int copiadas = imagenesWar == null ? 0 : AlmacenImagenes.sembrar(Paths.get(imagenesWar));
        contexto.log("Imagenes en " + AlmacenImagenes.getCarpetaBase() + " (" + copiadas + " copiadas del WAR)");
    }
}
