package ServidorWeb.servlets;

import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

/** Caso de uso "Alta de Usuario". Actor: Visitante (el AccesoFilter ya lo garantiza). */
@WebServlet("/registro")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)   // necesario por el <input type="file">
public class AltaUsuarioServlet extends HttpServlet {

    // Sin "/" ni ".." : el central usa el nickname como nombre de archivo de la imagen
    private static final Pattern NICK = Pattern.compile("^[A-Za-z0-9_-]{3,30}$");
    private static final Pattern MAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private IControllerV2 controller;

    @Override
    public void init() {
        this.controller = Fabrica.getInstance().getControllerV2();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String nickname = trim(req.getParameter("nickname"));
        String nombre   = trim(req.getParameter("nombre"));
        String apellido = trim(req.getParameter("apellido"));
        String mail     = trim(req.getParameter("mail"));
        String fechaStr = trim(req.getParameter("fechaNac"));
        String password = req.getParameter("password");
        String confirmar = req.getParameter("confirmar");
        boolean esDocente = "DOCENTE".equals(req.getParameter("tipo"));
        String instituto = esDocente ? trim(req.getParameter("instituto")) : null;

        // ---- Validaciones de la vista (las de negocio las hace el central) ----
        String error = null;
        LocalDate fechaNac = null;
        if (nickname.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || mail.isEmpty()
                || fechaStr.isEmpty() || password == null || password.isEmpty()) {
            error = "Completá todos los campos obligatorios.";
        } else if (!NICK.matcher(nickname).matches()) {
            error = "El nickname debe tener 3 a 30 caracteres (letras, números, _ o -).";
        } else if (!MAIL.matcher(mail).matches()) {
            error = "El correo electrónico no es válido.";
        } else if (!password.equals(confirmar)) {
            error = "La contraseña y su confirmación no coinciden.";
        } else if (esDocente && (instituto == null || instituto.isEmpty())) {
            error = "Un docente debe indicar el instituto al que pertenece.";
        } else {
            try {
                fechaNac = LocalDate.parse(fechaStr);        // formato yyyy-MM-dd del input date
                if (!fechaNac.isBefore(LocalDate.now())) {
                    error = "La fecha de nacimiento debe ser anterior a hoy.";
                }
            } catch (DateTimeParseException e) {
                error = "La fecha de nacimiento no es válida.";
            }
        }

        // ---- Llamada al Servidor Central ----
        Path temporal = null;
        if (error == null) {
            try {
                temporal = guardarImagenTemporal(req.getPart("imagen"));
                controller.altaUsuario(nickname, mail, nombre, apellido, fechaNac, instituto,
                        temporal == null ? null : temporal.toString(), password);

                // Éxito: redirect (patrón Post/Redirect/Get) para que F5 no reenvíe el alta
                resp.sendRedirect(req.getContextPath() + "/login?registrado=true");
                return;
            } catch (IllegalArgumentException e) {
                error = e.getMessage();   // nick/mail repetido, instituto inexistente, formato de imagen
            } catch (RuntimeException e) {
                getServletContext().log("Error en alta de usuario", e);
                error = "No se pudo completar el alta. Intentá nuevamente.";
            } finally {
                if (temporal != null) Files.deleteIfExists(temporal);
            }
        }

        // ---- Error: volver al formulario conservando lo ingresado (menos las contraseñas) ----
        req.setAttribute("error", error);
        req.setAttribute("nickname", nickname);
        req.setAttribute("nombre", nombre);
        req.setAttribute("apellido", apellido);
        req.setAttribute("mail", mail);
        req.setAttribute("fechaNac", fechaStr);
        req.setAttribute("tipo", esDocente ? "DOCENTE" : "ESTUDIANTE");
        req.setAttribute("institutoElegido", instituto);
        mostrarFormulario(req, resp);
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<String> institutos = controller.listarNombresInstitutos();
        req.setAttribute("institutos", institutos);
        req.getRequestDispatcher("/WEB-INF/views/usuario/registro.jsp").forward(req, resp);
    }

    /** El central espera una RUTA de archivo local: guardamos el upload en un temporal. */
    private Path guardarImagenTemporal(Part parte) throws IOException {
        if (parte == null || parte.getSize() == 0) return null;
        String original = parte.getSubmittedFileName();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
        if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png")) {
            throw new IllegalArgumentException("Formato de imagen no soportado. Use JPG o PNG.");
        }
        Path tmp = Files.createTempFile("usuario-", ext);
        try (InputStream in = parte.getInputStream()) {
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
        }
        return tmp;
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}