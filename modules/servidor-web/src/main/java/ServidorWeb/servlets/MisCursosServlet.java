package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * "Mis cursos y ediciones" del docente: acceso rapido desde el lateral (Docencia) a los cursos que dicta y a las
 * ediciones en las que participa, sin pasar por su perfil. No es un caso de uso de la letra: es navegacion hacia
 * Consulta de Curso/Edicion, Alta de Edicion y Seleccionar Estudiantes. Solo docentes (AccesoFilter).
 */
@WebServlet("/docente/cursos")
public class MisCursosServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("misCursos", controller.misCursosDocente(usuario(req).getNickname()));
        } catch (RuntimeException e) {
            req.setAttribute("error", mensajeDeError(e));
        }
        req.setAttribute("titulo", "Mis cursos y ediciones");
        vista(req, resp, "docente/mis-cursos.jsp");
    }
}
