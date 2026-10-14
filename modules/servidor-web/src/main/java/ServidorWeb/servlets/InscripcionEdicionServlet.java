package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTSesion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Caso de uso "Inscripcion a Edicion de Curso" (Estudiante).
 * El estudiante indica el instituto, elige un curso, el sistema muestra la
 * edicion vigente y, al confirmar, registra la inscripcion en la fecha actual
 * (queda en estado Inscripto).
 */
@WebServlet("/edicion/inscripcion")
public class InscripcionEdicionServlet extends BaseServlet {

    private static final String RUTA = "/edicion/inscripcion";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Inscripción a edición de curso");
        String curso = param(req, "curso");
        if (curso.isEmpty()) {
            elegirCurso(req, RUTA);
        } else {
            req.setAttribute("curso", curso);
            String vigente = controller.obtenerEdicionVigente(curso);
            if (vigente != null) {
                try {
                    req.setAttribute("edicion", controller.obtenerEdicionCurso(vigente));
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                }
                String yo = usuario(req).getNickname();
                for (DTInscripcionEstudiante i : controller.listarInscripcionesEstudiante(yo)) {
                    if (i.edicion().equals(vigente)) {
                        req.setAttribute("yaInscripto", i);
                    }
                }
            }
        }
        vista(req, resp, "edicion/inscripcion.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String edicion = param(req, "edicion");
        String curso = param(req, "curso");
        DTSesion yo = usuario(req);
        try {
            controller.inscribirEstudianteEdicion(yo.getNickname(), yo.getMail(), edicion, LocalDate.now());
            flash(req, "Te inscribiste a " + edicion + ". Tu inscripción quedó en estado Inscripto.");
            redirigir(req, resp, "/ediciones?nombre=" + url(edicion));
        } catch (Exception e) {
            flashError(req, e.getMessage());
            redirigir(req, resp, RUTA + "?curso=" + url(curso));
        }
    }
}
