package ServidorWeb.servlets;

import ServidorWeb.seguridad.DTSesion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Consulta de Curso" (Visitante, Estudiante, Docente). Se entra por los institutos del lateral.
 * <ul>
 *   <li>/cursos?instituto=X -> cursos del instituto</li>
 *   <li>/cursos?nombre=Y    -> datos del curso con sus ediciones, programas y valoraciones (Figura 3 y 7)</li>
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
                req.setAttribute("detalle", central.consultarCurso(nombre));
                // Valoraciones del curso (7.6, Figura 7): promedio, resumen por puntaje y el puntaje de quien mira
                DTSesion sesion = usuario(req);
                req.setAttribute("valoracion", central.consultarValoracionCurso(nombre, sesion == null ? null : sesion.getNickname()));
                req.setAttribute("titulo", nombre);
                vista(req, resp, "curso/detalle.jsp");
            } else if (!instituto.isEmpty()) {
                req.setAttribute("cursos", central.listarCursosInstituto(instituto));
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
