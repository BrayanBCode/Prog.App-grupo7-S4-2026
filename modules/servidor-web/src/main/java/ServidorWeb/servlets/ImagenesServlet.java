package ServidorWeb.servlets;

import ServidorCentral.Persistencia.AlmacenImagenes;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Sirve las imagenes de usuarios, cursos y ediciones desde la carpeta fija del Servidor Central
 * (no desde el WAR). /imagenes/cursos/X.jpg -> carpeta/cursos/X.jpg. Lo que no exista o quede
 * fuera de la carpeta (ej. /imagenes/../x) da 404.
 */
@WebServlet("/imagenes/*")
public class ImagenesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Path archivo = AlmacenImagenes.resolver(req.getPathInfo());
        if (archivo == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String tipo = getServletContext().getMimeType(archivo.getFileName().toString());
        resp.setContentType(tipo == null ? "application/octet-stream" : tipo);
        resp.setContentLengthLong(Files.size(archivo));
        // Una foto se puede reemplazar con el mismo nombre: el navegador revalida con Last-Modified.
        resp.setHeader("Cache-Control", "no-cache");
        Files.copy(archivo, resp.getOutputStream());
    }

    /** Habilita el "304 Not Modified" de HttpServlet: si la foto no cambio, no se vuelve a mandar. */
    @Override
    protected long getLastModified(HttpServletRequest req) {
        try {
            Path archivo = AlmacenImagenes.resolver(req.getPathInfo());
            return archivo == null ? -1 : Files.getLastModifiedTime(archivo).toMillis() / 1000 * 1000;
        } catch (IOException e) {
            return -1;
        }
    }
}
