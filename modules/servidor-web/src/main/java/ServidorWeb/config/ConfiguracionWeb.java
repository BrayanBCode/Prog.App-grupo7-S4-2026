package ServidorWeb.config;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuracion del Servidor Web, fuera del WAR para poder cambiarla sin volver a generarlo
 * (letra 7.7 y 7.8): {@code ~/.edEXT/web.properties}. Si no existe se crea con los valores por defecto.
 */
public final class ConfiguracionWeb {

    public static final String CENTRAL_URL = "central.url";

    private static final String CONTENIDO_POR_DEFECTO = """
            # Configuracion del Servidor Web de edEXT (se crea sola la primera vez).
            # Cambiarla no requiere regenerar el WAR: alcanza con reiniciar la aplicacion en Tomcat.

            # Direccion de los Web Services del Servidor Central (la "ws.url" de servidor.properties)
            central.url=http://localhost:9128/edext/central
            """;

    private final Properties valores = new Properties();

    private ConfiguracionWeb(Path archivo) {
        try {
            if (Files.notExists(archivo)) {
                Files.createDirectories(archivo.getParent());
                Files.writeString(archivo, CONTENIDO_POR_DEFECTO, StandardCharsets.UTF_8);
            }
            try (Reader in = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
                valores.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer la configuracion " + archivo, e);
        }
    }

    /** La de ~/.edEXT/web.properties (se lee cada vez: solo se pide al arrancar). */
    public static ConfiguracionWeb get() {
        return new ConfiguracionWeb(Paths.get(System.getProperty("user.home"), ".edEXT", "web.properties"));
    }

    /** La de otro archivo (tests). */
    public static ConfiguracionWeb desde(Path archivo) {
        return new ConfiguracionWeb(archivo);
    }

    /** El valor sin espacios a los costados; null si no esta o esta vacio. */
    public String valor(String clave) {
        String v = valores.getProperty(clave);
        return v == null || v.isBlank() ? null : v.trim();
    }
}
