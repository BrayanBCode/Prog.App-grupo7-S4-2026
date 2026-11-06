package ServidorCentral.Persistencia;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuracion del Servidor Central (y de la Estacion de Trabajo), fuera del JAR para poder
 * cambiarla sin recompilar (letra 7.7 y 7.8): {@code ~/.edEXT/servidor.properties}.
 * Si el archivo no existe se crea con los valores por defecto, que son los de desarrollo.
 */
public final class Configuracion {

    public static final String WS_URL = "ws.url";
    public static final String DB_URL = "db.url";
    public static final String DB_USUARIO = "db.usuario";
    public static final String DB_CONTRASENA = "db.contrasena";
    public static final String IMAGENES_CARPETA = "imagenes.carpeta";

    private static final String CONTENIDO_POR_DEFECTO = """
            # Configuracion del Servidor Central de edEXT (se crea sola la primera vez).
            # Cambiarla no requiere recompilar: alcanza con reiniciar el servidor.

            # Direccion donde se publican los Web Services que consume el Servidor Web
            ws.url=http://localhost:9128/edext/central

            # Base de datos MySQL
            db.url=jdbc:mysql://localhost:3310/laboratorio_db?serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL
            db.usuario=root
            db.contrasena=root

            # Carpeta donde se guardan las imagenes de usuarios, cursos y ediciones
            # (vacia = ~/edext/imagenes)
            imagenes.carpeta=
            """;

    private static Configuracion instancia;

    private final Properties valores = new Properties();

    private Configuracion(Path archivo) {
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

    /** La de ~/.edEXT/servidor.properties. */
    public static synchronized Configuracion get() {
        if (instancia == null) {
            instancia = new Configuracion(Paths.get(System.getProperty("user.home"), ".edEXT", "servidor.properties"));
        }
        return instancia;
    }

    /** La de otro archivo (tests). */
    public static Configuracion desde(Path archivo) {
        return new Configuracion(archivo);
    }

    /** El valor sin espacios a los costados; null si no esta o esta vacio. */
    public String valor(String clave) {
        String v = valores.getProperty(clave);
        return v == null || v.isBlank() ? null : v.trim();
    }

    /** Las propiedades de conexion que pisan las de persistence.xml (solo las que estan configuradas). */
    public Map<String, String> propiedadesJpa() {
        Map<String, String> jpa = new HashMap<>();
        if (valor(DB_URL) != null) {
            jpa.put("javax.persistence.jdbc.url", valor(DB_URL));
        }
        if (valor(DB_USUARIO) != null) {
            jpa.put("javax.persistence.jdbc.user", valor(DB_USUARIO));
        }
        // La contrasena puede ser vacia a proposito: se pasa siempre que la clave exista
        if (valores.getProperty(DB_CONTRASENA) != null) {
            jpa.put("javax.persistence.jdbc.password", valores.getProperty(DB_CONTRASENA).trim());
        }
        return jpa;
    }
}
