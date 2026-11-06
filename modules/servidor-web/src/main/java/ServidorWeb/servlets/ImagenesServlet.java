package ServidorWeb.servlets;

import ServidorWeb.ws.Central;
import ServidorWeb.ws.central.DTImagen;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Sirve las imagenes de usuarios, cursos y ediciones, que viven en el Servidor Central:
 * /imagenes/cursos/X.jpg se le pide por SOAP como "cursos/X.jpg". Lo que no exista o quede
 * fuera de la carpeta del central (ej. /imagenes/../x) da 404.
 */
@WebServlet("/imagenes/*")
public class ImagenesServlet extends HttpServlet {

    private Central central;

    @Override
    public void init() {
        this.central = Central.de(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DTImagen imagen = central.obtenerImagen(req.getPathInfo());
        if (imagen == null || imagen.getContenido() == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String tipo = getServletContext().getMimeType("x." + imagen.getExtension());
        resp.setContentType(tipo == null ? "application/octet-stream" : tipo);
        resp.setContentLength(imagen.getContenido().length);
        // Una foto se puede reemplazar con el mismo nombre: el navegador la guarda solo un minuto.
        resp.setHeader("Cache-Control", "max-age=60");
        resp.getOutputStream().write(imagen.getContenido());
    }
}
