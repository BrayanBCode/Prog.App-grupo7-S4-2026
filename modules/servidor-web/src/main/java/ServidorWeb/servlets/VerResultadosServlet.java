package ServidorWeb.servlets;

import ServidorWeb.ws.central.DTInscripcionesEdicion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Ver resultados de evaluaciones en ediciones de cursos" (Tarea 3). Actor: Docente que participa en la
 * edicion (el AccesoFilter garantiza el rol; el central, que participe): /edicion/acta?edicion=E.
 * Muestra el acta de una edicion cerrada: los estudiantes aceptados con la nota obtenida. Se llega desde
 * "Mis cursos y ediciones" y desde la Consulta de Edicion. Si la edicion todavia no se cerro, lleva a
 * Ingresar resultados.
 */
@WebServlet("/edicion/acta")
public class VerResultadosServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String edicion = param(req, "edicion");
        DTInscripcionesEdicion acta;
        try {
            acta = central.consultarAceptadosEdicion(usuario(req).getNickname(), edicion);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("titulo", "Cursos");
            vista(req, resp, "curso/lista.jsp");
            return;
        }
        if (!acta.getEdicion().isCerrada()) {
            flashError(req, "La edición todavía no está cerrada: el acta se puede ver después del cierre.");
            redirigir(req, resp, "/edicion/resultados?edicion=" + url(edicion));
            return;
        }
        req.setAttribute("acta", acta);
        req.setAttribute("titulo", "Acta de " + edicion);
        vista(req, resp, "edicion/acta.jsp");
    }
}
