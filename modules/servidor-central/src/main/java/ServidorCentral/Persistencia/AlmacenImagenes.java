package ServidorCentral.Persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;

/**
 * Carpeta local donde se guardan las imagenes de usuarios, cursos y ediciones.
 * En la BD se guarda la ruta RELATIVA a esta carpeta (ej. "cursos/Flor_del_Ceibo.jpg"),
 * nunca la ruta absoluta ni una URL (anexo de datos de prueba, Obs2).
 * <p>
 * La carpeta se elige asi (la primera que aplique):
 * <ol>
 *   <li>{@link #setCarpetaBase(Path)} (el Servidor Web la apunta a la carpeta /imagenes de la webapp).</li>
 *   <li>Propiedad de sistema {@code edext.imagenes} o variable de entorno {@code EDEXT_IMAGENES}.</li>
 *   <li>La carpeta {@code imagenes} del directorio actual o del primer directorio padre que la tenga
 *       (asi la Estacion de Trabajo la encuentra tanto desde la raiz del repo como desde modules/swing).</li>
 * </ol>
 */
public final class AlmacenImagenes {

    public static final String USUARIOS = "usuarios";
    public static final String CURSOS = "cursos";
    public static final String EDICIONES = "ediciones";

    private static Path carpetaBase;

    private AlmacenImagenes() {}

    public static synchronized void setCarpetaBase(Path carpeta) {
        carpetaBase = carpeta;
    }

    public static synchronized Path getCarpetaBase() {
        if (carpetaBase == null) {
            carpetaBase = carpetaPorDefecto();
        }
        return carpetaBase;
    }

    private static Path carpetaPorDefecto() {
        String configurada = System.getProperty("edext.imagenes", System.getenv("EDEXT_IMAGENES"));
        if (configurada != null && !configurada.isBlank()) {
            return Paths.get(configurada);
        }
        Path actual = Paths.get("").toAbsolutePath();
        for (Path p = actual; p != null; p = p.getParent()) {
            if (Files.isDirectory(p.resolve("imagenes"))) {
                return p.resolve("imagenes");
            }
        }
        return actual.resolve("imagenes");
    }

    /**
     * Copia la imagen elegida (ruta de un archivo local) a {@code carpetaBase/subcarpeta}
     * con el nombre {@code nombreBase} (limpio de caracteres raros) y su extension.
     *
     * @return la ruta relativa a guardar en la BD, o null si no se eligio imagen.
     * @throws IllegalArgumentException si el formato no es JPG o PNG.
     */
    public static String guardar(String rutaOrigen, String subcarpeta, String nombreBase) {
        if (rutaOrigen == null || rutaOrigen.isBlank()) {
            return null;
        }
        Path origen = Paths.get(rutaOrigen);
        if (!Files.exists(origen)) {
            return null;
        }

        String nombreOrigen = origen.getFileName().toString().toLowerCase();
        String extension = nombreOrigen.contains(".") ? nombreOrigen.substring(nombreOrigen.lastIndexOf('.') + 1) : "";
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new IllegalArgumentException("Formato de imagen no soportado. Use JPG o PNG.");
        }

        String relativa = subcarpeta + "/" + nombreArchivo(nombreBase) + "." + extension;
        try {
            Path destino = getCarpetaBase().resolve(relativa);
            Files.createDirectories(destino.getParent());
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            return relativa;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar la imagen: " + ex.getMessage(), ex);
        }
    }

    /** @return la ruta absoluta de una imagen guardada, o null si no tiene o el archivo no esta. */
    public static String rutaAbsoluta(String rutaRelativa) {
        if (rutaRelativa == null || rutaRelativa.isBlank()) {
            return null;
        }
        Path archivo = getCarpetaBase().resolve(rutaRelativa).normalize();
        return Files.isRegularFile(archivo) ? archivo.toString() : null;
    }

    /** "Taller de robótica educativa." -> "Taller_de_robotica_educativa" (sin tildes, espacios ni '/'). */
    static String nombreArchivo(String nombre) {
        String sinTildes = Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String limpio = sinTildes.replaceAll("[^A-Za-z0-9_-]+", "_").replaceAll("^_+|_+$", "");
        return limpio.isEmpty() ? "imagen" : limpio;
    }
}
