package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Consulta de Curso" (Visitante, Estudiante, Docente). Se entra por los institutos del lateral.
 * <ul>
 *   <li>/cursos?instituto=X -> cursos del instituto</li>
 *   <li>/cursos?nombre=Y    -> datos del curso con sus ediciones y programas (Figura 3)</li>
 * </ul>
 * Sin parametros (o con un instituto o curso que no existe) muestra el mensaje y la lista de institutos.
 */
@WebServlet("/cursos")
public class CursoServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombre = param(req, "nombre");
        String instituto = param(req, "instituto");
        try {
            if (!nombre.isEmpty()) {
                req.setAttribute("detalle", controller.consultarCurso(nombre));
                req.setAttribute("titulo", nombre);
                vista(req, resp, "curso/detalle.jsp");
            } else if (!instituto.isEmpty()) {
                req.setAttribute("cursos", controller.listarCursosInstituto(instituto));
                req.setAttribute("instituto", instituto);
                req.setAttribute("titulo", "Cursos de " + instituto);
                vista(req, resp, "curso/lista.jsp");
            } else {
                req.setAttribute("titulo", "Cursos");
                vista(req, resp, "curso/lista.jsp");
            }
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("titulo", "Cursos");
            req.removeAttribute("instituto");
            vista(req, resp, "curso/lista.jsp");
        }
    }
}
