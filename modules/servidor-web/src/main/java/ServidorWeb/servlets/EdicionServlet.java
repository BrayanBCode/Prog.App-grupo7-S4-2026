package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Consulta de Edicion de Curso" (Visitante, Estudiante, Docente).
 * /ediciones?nombre=E -> datos de la edicion y sus docentes (Figura 4). Se llega desde la
 * Consulta de Curso (lista de ediciones) o desde el perfil de un usuario.
 */
@WebServlet("/ediciones")
public class EdicionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombre = param(req, "nombre");
        try {
            req.setAttribute("detalleEdicion", controller.consultarEdicion(nombre));
            req.setAttribute("titulo", nombre);
            vista(req, resp, "edicion/detalle.jsp");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("titulo", "Cursos");
            vista(req, resp, "curso/lista.jsp");
        }
    }
}
