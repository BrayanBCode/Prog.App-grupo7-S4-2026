package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTImagen;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

/** Caso de uso "Alta de Usuario". Actor: Visitante (el AccesoFilter ya lo garantiza). */
@WebServlet("/registro")
// Limite holgado: el de 2 MB lo controla el central, que devuelve un mensaje claro.
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AltaUsuarioServlet extends BaseServlet {

    // Sin "/" ni ".." : el central usa el nickname como nombre de archivo de la imagen
    private static final Pattern NICK = Pattern.compile("^[A-Za-z0-9_-]{3,30}$");
    private static final Pattern MAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String nickname = param(req, "nickname");
        String nombre   = param(req, "nombre");
        String apellido = param(req, "apellido");
        String mail     = param(req, "mail");
        String fechaStr = param(req, "fechaNac");
        String password = req.getParameter("password");
        String confirmar = req.getParameter("confirmar");
        boolean esDocente = "DOCENTE".equals(req.getParameter("tipo"));
        String instituto = esDocente ? param(req, "instituto") : null;
        LocalDate fechaNac = fecha(fechaStr);

        // ---- Validaciones de la vista (las de negocio las hace el central) ----
        String error = null;
        if (nickname.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || mail.isEmpty()
                || fechaStr.isEmpty() || password == null || password.isEmpty()) {
            error = "Completá todos los campos obligatorios.";
        } else if (!NICK.matcher(nickname).matches()) {
            error = "El nickname debe tener 3 a 30 caracteres (letras, números, _ o -).";
        } else if (!MAIL.matcher(mail).matches()) {
            error = "El correo electrónico no es válido.";
        } else if (!password.equals(confirmar)) {
            error = "La contraseña y su confirmación no coinciden.";
        } else if (esDocente && instituto.isEmpty()) {
            error = "Un docente debe indicar el instituto al que pertenece.";
        } else if (fechaNac == null) {
            error = "La fecha de nacimiento no es válida.";
        } else if (!fechaNac.isBefore(LocalDate.now())) {
            error = "La fecha de nacimiento debe ser anterior a hoy.";
        }

        // ---- Llamada al Servidor Central ----
        if (error == null) {
            try {
                DTImagen imagen = leerImagen(req, "imagen");
                controller.altaUsuario(nickname, mail, nombre, apellido, fechaNac, instituto, imagen, password);

                // Éxito: redirect (patrón Post/Redirect/Get) para que F5 no reenvíe el alta
                redirigir(req, resp, "/login?registrado=true");
                return;
            } catch (IllegalArgumentException e) {
                error = e.getMessage();   // nick/mail repetido, instituto inexistente, imagen invalida
            } catch (RuntimeException e) {
                getServletContext().log("Error en alta de usuario", e);
                error = "No se pudo completar el alta. Intentá nuevamente.";
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
        req.setAttribute("institutos", controller.listarNombresInstitutos());
        vista(req, resp, "usuario/registro.jsp");
    }
}
