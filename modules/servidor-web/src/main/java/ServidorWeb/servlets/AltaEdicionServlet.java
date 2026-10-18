package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTCursoDetalle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Caso de uso "Alta de Edicion de Curso". Actor: Docente (lo garantiza el AccesoFilter).
 * Se llega desde la Consulta de Curso: /edicion/alta?curso=X (instituto -> curso, como dice la letra).
 * El docente que da el alta siempre participa en la edicion (D3); el central lo agrega.
 */
@WebServlet("/edicion/alta")
// Limite holgado: el de 2 MB de la imagen lo controla el central, que devuelve un mensaje claro.
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AltaEdicionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarFormulario(req, resp, param(req, "curso"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombre = param(req, "nombre");
        String curso = param(req, "curso");
        String[] docentes = req.getParameterValues("docentes");
        try {
            controller.altaEdicion(usuario(req).getNickname(), nombre, curso, fecha(param(req, "fechaInicio")),
                    fecha(param(req, "fechaFin")), cupo(param(req, "cupo")),
                    docentes == null ? List.of() : Arrays.asList(docentes), leerImagen(req, "imagen"));
            flash(req, "Se creó la edición \"" + nombre + "\".");
            redirigir(req, resp, "/ediciones?nombre=" + url(nombre));
        } catch (RuntimeException e) {
            // Vuelve al formulario con lo que se ingreso (menos la imagen, que el navegador no reenvia)
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("docentesElegidos", docentes == null ? List.of() : Arrays.asList(docentes));
            mostrarFormulario(req, resp, curso);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp, String curso)
            throws ServletException, IOException {
        try {
            DTCursoDetalle detalle = controller.consultarCurso(curso);
            req.setAttribute("cursoAlta", detalle.curso());
            req.setAttribute("docentesInstituto", controller.listarDocentesPorInstituto(detalle.curso().instituto()));
            req.setAttribute("titulo", "Crear edición");
            vista(req, resp, "edicion/alta.jsp");
        } catch (RuntimeException e) {
            // Curso inexistente o sin elegir: se elige desde la Consulta de Curso
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", "Elegí el curso desde su página (Institutos → curso → Crear edición).");
            req.setAttribute("titulo", "Cursos");
            vista(req, resp, "curso/lista.jsp");
        }
    }

    /** null si viene vacio (= sin limite); -1 si no es un numero, para que el central lo rechace con su mensaje. */
    private static Integer cupo(String texto) {
        if (texto.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
