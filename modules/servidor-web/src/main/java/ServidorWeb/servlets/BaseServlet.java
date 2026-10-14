package ServidorWeb.servlets;

import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorWeb.seguridad.SesionUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Lo que comparten todos los servlets del sitio: el controller del Servidor Central
 * y algunos ayudantes (leer parametros, mostrar una vista, mensajes "flash", imagenes subidas).
 * La logica de negocio NO va aca: siempre se le pide al controller.
 */
public abstract class BaseServlet extends HttpServlet {

    protected IControllerV2 controller;

    @Override
    public void init() {
        this.controller = Fabrica.getInstance().getControllerV2();
    }

    protected void vista(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + jsp).forward(req, resp);
    }

    /**
     * Prepara el paso "elegir instituto y luego un curso" (ver comun/elegir-curso.jspf).
     * Si ya vino ?instituto=, carga los cursos del instituto.
     */
    protected void elegirCurso(HttpServletRequest req, String accion) {
        req.setAttribute("accion", accion);
        String instituto = param(req, "instituto");
        if (!instituto.isEmpty()) {
            req.setAttribute("filtro", instituto);
            req.setAttribute("cursos", controller.listarCursosPorInstituto(instituto));
        }
    }

    /** Parametro sin espacios a los costados; "" si no vino. */
    protected static String param(HttpServletRequest req, String nombre) {
        String v = req.getParameter(nombre);
        return v == null ? "" : v.trim();
    }

    /** null si el texto esta vacio o no es una fecha yyyy-MM-dd (formato del input date). */
    protected static LocalDate fecha(String texto) {
        try {
            return texto == null || texto.isBlank() ? null : LocalDate.parse(texto);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    protected static DTSesion usuario(HttpServletRequest req) {
        return SesionUsuario.actual(req);
    }

    /** Mensaje que se muestra una sola vez en la proxima pagina (patron Post/Redirect/Get). */
    protected static void flash(HttpServletRequest req, String mensaje) {
        req.getSession(true).setAttribute("flash", mensaje);
    }

    /** Igual que flash, pero se muestra como error. */
    protected static void flashError(HttpServletRequest req, String mensaje) {
        req.getSession(true).setAttribute("flashError", mensaje);
    }

    protected static void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    protected static String url(String valor) {
        return URLEncoder.encode(valor == null ? "" : valor, StandardCharsets.UTF_8);
    }

    /**
     * El Servidor Central recibe la imagen como RUTA de un archivo local: el upload se
     * guarda en un temporal que el servlet borra despues de llamar al controller.
     *
     * @return el temporal, o null si no se subio imagen.
     * @throws IllegalArgumentException si no es JPG o PNG.
     */
    protected static Path imagenTemporal(HttpServletRequest req, String campo) throws IOException, ServletException {
        Part parte = req.getPart(campo);
        if (parte == null || parte.getSize() == 0) {
            return null;
        }
        String original = parte.getSubmittedFileName();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
        if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png")) {
            throw new IllegalArgumentException("Formato de imagen no soportado. Use JPG o PNG.");
        }
        Path tmp = Files.createTempFile("edext-", ext);
        try (InputStream in = parte.getInputStream()) {
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
        }
        return tmp;
    }

    protected static void borrar(Path temporal) {
        if (temporal != null) {
            try {
                Files.deleteIfExists(temporal);
            } catch (IOException ignorada) {
                // un temporal que quedo no rompe nada
            }
        }
    }
}
