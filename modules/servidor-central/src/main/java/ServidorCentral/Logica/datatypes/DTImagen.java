package ServidorCentral.Logica.datatypes;

import java.util.Locale;

/**
 * Imagen que llega de la presentacion: el contenido del archivo y su extension
 * ("jpg", "png"...). El Servidor Web la arma directo desde el upload, sin archivos temporales.
 * La validacion (formato y tamano) la hace AlmacenImagenes al guardarla.
 */
public record DTImagen(byte[] contenido, String extension) {

    public DTImagen {
        extension = extension == null ? "" : extension.trim().toLowerCase(Locale.ROOT);
        if (extension.startsWith(".")) {
            extension = extension.substring(1);
        }
    }

    /** Toma la extension del nombre del archivo original (ej. "foto.PNG" -> "png"). */
    public static DTImagen deArchivo(byte[] contenido, String nombreArchivo) {
        String nombre = nombreArchivo == null ? "" : nombreArchivo;
        int punto = nombre.lastIndexOf('.');
        return new DTImagen(contenido, punto < 0 ? "" : nombre.substring(punto + 1));
    }

    public boolean vacia() {
        return contenido == null || contenido.length == 0;
    }
}
