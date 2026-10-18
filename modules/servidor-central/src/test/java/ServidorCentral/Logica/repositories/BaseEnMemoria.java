package ServidorCentral.Logica.repositories;

import ServidorCentral.Persistencia.Conexion;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base H2 en memoria para probar los repositories con JPA de verdad, sin MySQL: cada llamada a {@link #nueva()}
 * crea una base vacia con las tablas de persistence.xml, y al cerrarla desaparece. No deja datos en ningun lado.
 */
final class BaseEnMemoria {

    private static final AtomicInteger NUMERO = new AtomicInteger();

    private BaseEnMemoria() {
    }

    static Conexion nueva() {
        String nombre = "test" + NUMERO.incrementAndGet();
        return Conexion.conPropiedades(Map.of(
                "javax.persistence.jdbc.url", "jdbc:h2:mem:" + nombre + ";DB_CLOSE_DELAY=-1",
                "javax.persistence.jdbc.driver", "org.h2.Driver",
                "javax.persistence.jdbc.user", "sa",
                "javax.persistence.jdbc.password", "",
                "eclipselink.ddl-generation", "drop-and-create-tables",
                "eclipselink.session-name", nombre,
                "eclipselink.logging.level", "OFF"));
    }
}
