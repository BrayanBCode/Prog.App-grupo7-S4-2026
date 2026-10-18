package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Listar aceptados a una edicion de curso". Actor: Docente que participa en la edicion (el
 * AccesoFilter garantiza el rol; el central, que participe). A diferencia de Seleccionar Estudiantes no se
 * limita a la vigente: /edicion/aceptados?edicion=E. Se llega desde la Consulta de Edicion, desde
 * "Mis cursos y ediciones" y desde "Ver aceptados" en el perfil propio del docente.
 */
@WebServlet("/edicion/aceptados")
public class ListarAceptadosServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("aceptados", controller.consultarAceptadosEdicion(usuario(req).getNickname(), param(req, "edicion")));
            req.setAttribute("titulo", "Estudiantes aceptados");
            vista(req, resp, "edicion/aceptados.jsp");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("titulo", "Cursos");
            vista(req, resp, "curso/lista.jsp");
        }
    }
}
