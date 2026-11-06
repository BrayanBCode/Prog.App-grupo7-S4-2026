package ServidorCentral.Persistencia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionTest {

    @TempDir
    Path dir;

    @Test
    void siNoExisteSeCreaConLosValoresPorDefecto() {
        Path archivo = dir.resolve(".edEXT").resolve("servidor.properties");

        Configuracion c = Configuracion.desde(archivo);

        assertTrue(Files.exists(archivo));
        assertEquals("http://localhost:9128/edext/central", c.valor(Configuracion.WS_URL));
        assertEquals("root", c.valor(Configuracion.DB_USUARIO));
        assertNull(c.valor(Configuracion.IMAGENES_CARPETA));
        assertTrue(c.propiedadesJpa().get("javax.persistence.jdbc.url").startsWith("jdbc:mysql://localhost:3310/"));
    }

    @Test
    void leeLoQueCambioElUsuario() throws IOException {
        Path archivo = dir.resolve("servidor.properties");
        Files.writeString(archivo, """
                ws.url = http://192.168.0.10:9000/edext
                db.url=jdbc:mysql://otra:3306/edext
                db.contrasena=
                imagenes.carpeta=/srv/imagenes
                """);

        Configuracion c = Configuracion.desde(archivo);

        assertEquals("http://192.168.0.10:9000/edext", c.valor(Configuracion.WS_URL));
        assertEquals("/srv/imagenes", c.valor(Configuracion.IMAGENES_CARPETA));
        // Sin db.usuario se usa el de persistence.xml; la contrasena vacia se respeta
        assertEquals(Map.of("javax.persistence.jdbc.url", "jdbc:mysql://otra:3306/edext",
                "javax.persistence.jdbc.password", ""), c.propiedadesJpa());
    }

    @Test
    void noPisaUnArchivoQueYaExiste() throws IOException {
        Path archivo = dir.resolve("servidor.properties");
        Files.writeString(archivo, "ws.url=http://x:1/y\n");

        Configuracion.desde(archivo);

        assertEquals("ws.url=http://x:1/y\n", Files.readString(archivo));
    }
}
