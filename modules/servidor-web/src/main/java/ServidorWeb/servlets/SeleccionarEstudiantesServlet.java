package ServidorWeb.servlets;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Caso de uso "Seleccionar estudiantes para una edicion de curso" (Figura 5). Actor: Docente que participa
 * en la edicion (el AccesoFilter garantiza el rol; el central, que participe). Se llega desde la Consulta de
 * Edicion vigente: /edicion/seleccionar?edicion=E.
 * Cada inscripcion en Inscripto tiene un selector Pendiente / Aceptar / Rechazar; el formulario manda
 * seleccion=nick:ACEPTADA o seleccion=nick:RECHAZADA por cada una que se decidio (Post/Redirect/Get).
 */
@WebServlet("/edicion/seleccionar")
public class SeleccionarEstudiantesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String edicion = param(req, "edicion");
        try {
            req.setAttribute("seleccion", controller.consultarInscripcionesEdicion(usuario(req).getNickname(), edicion));
            req.setAttribute("titulo", "Seleccionar estudiantes");
            vista(req, resp, "edicion/seleccionar.jsp");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("titulo", "Cursos");
            vista(req, resp, "curso/lista.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String edicion = param(req, "edicion");
        Map<String, EstadoInscripcion> estados = new LinkedHashMap<>();
        String[] elegidos = req.getParameterValues("seleccion");
        if (elegidos != null) {
            for (String valor : elegidos) {
                int dosPuntos = valor.lastIndexOf(':');
                if (dosPuntos > 0) {   // "" = Pendiente: no se manda
                    try {
                        estados.put(valor.substring(0, dosPuntos), EstadoInscripcion.valueOf(valor.substring(dosPuntos + 1)));
                    } catch (IllegalArgumentException ignorado) {
                        // valor armado a mano: se ignora y el central avisa si no queda nada para guardar
                    }
                }
            }
        }
        try {
            controller.seleccionarEstudiantes(usuario(req).getNickname(), edicion, estados);
            flash(req, "Se guardó la selección de estudiantes.");
        } catch (RuntimeException e) {
            flashError(req, mensajeDeError(e));
        }
        redirigir(req, resp, "/edicion/seleccionar?edicion=" + url(edicion));
    }
}
