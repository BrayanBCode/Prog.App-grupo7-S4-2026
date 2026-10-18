package ServidorWeb.servlets;

import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Lo que comparten todos los servlets del sitio
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

    /**
     * Mensaje para mostrarle al usuario cuando falla una llamada al controller: el de la regla de
     * negocio tal cual, o uno generico (y se loguea) si es un error inesperado.
     */
    protected String mensajeDeError(Exception e) {
        if (e instanceof ReglaNegocioException) {
            return e.getMessage();
        }
        getServletContext().log("Error inesperado en " + getClass().getSimpleName(), e);
        return "No se pudo completar la operación. Intentá nuevamente.";
    }

    protected static void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    protected static String url(String valor) {
        return URLEncoder.encode(valor == null ? "" : valor, StandardCharsets.UTF_8);
    }

    /**
     * Imagen subida en el campo {@code campo} (form con enctype multipart y servlet con
     * {@code @MultipartConfig}). El formato y el tamano los valida el Servidor Central.
     *
     * @return la imagen, o null si no se subio ninguna.
     */
    protected static DTImagen leerImagen(HttpServletRequest req, String campo) throws IOException, ServletException {
        Part parte = req.getPart(campo);
        if (parte == null || parte.getSize() == 0) {
            return null;
        }
        try (InputStream in = parte.getInputStream()) {
            return DTImagen.deArchivo(in.readAllBytes(), parte.getSubmittedFileName());
        }
    }
}
