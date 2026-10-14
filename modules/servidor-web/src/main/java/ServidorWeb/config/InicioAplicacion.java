package ServidorWeb.config;

import ServidorCentral.Persistencia.AlmacenImagenes;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.nio.file.Paths;

/**
 * Al levantar la aplicacion web se apunta la carpeta de imagenes del Servidor Central
 * a la carpeta /imagenes de la webapp: asi Tomcat sirve las imagenes como archivos
 * estaticos (src="/imagenes/cursos/X.jpg") y en la BD queda solo la ruta relativa.
 */
@WebListener
public class InicioAplicacion implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        String carpeta = evento.getServletContext().getRealPath("/imagenes");
        if (carpeta != null) {
            AlmacenImagenes.setCarpetaBase(Paths.get(carpeta));
        }
    }
}
