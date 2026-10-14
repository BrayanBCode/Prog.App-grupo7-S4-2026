package ServidorWeb.servlets;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Caso de uso "Seleccionar estudiantes para una edicion de curso" (Docente, Figura 5).
 * El docente indica el instituto, elige un curso, el sistema muestra la edicion vigente con
 * todas sus inscripciones y su estado; el docente marca Aceptada o Rechazada en las que
 * estan en estado Inscripto y guarda la seleccion. Que el docente participe en la edicion
 * lo controla el Servidor Central.
 */
@WebServlet("/edicion/seleccionar")
public class SeleccionarEstudiantesServlet extends BaseServlet {

    private static final String RUTA = "/edicion/seleccionar";
    private static final String PREFIJO = "estado_";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Seleccionar estudiantes");
        String edicion = param(req, "edicion");
        String curso = param(req, "curso");

        if (edicion.isEmpty() && !curso.isEmpty()) {
            edicion = controller.obtenerEdicionVigente(curso);
            req.setAttribute("curso", curso);
            if (edicion == null) {
                req.setAttribute("sinVigente", true);
                vista(req, resp, "edicion/seleccionar.jsp");
                return;
            }
        }

        if (edicion == null || edicion.isEmpty()) {
            elegirCurso(req, RUTA);
        } else {
            try {
                req.setAttribute("edicion", controller.obtenerEdicionCurso(edicion));
                req.setAttribute("inscripciones",
                        controller.listarInscripcionesEdicion(usuario(req).getNickname(), edicion));
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        vista(req, resp, "edicion/seleccionar.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String edicion = param(req, "edicion");

        Map<String, EstadoInscripcion> estados = new LinkedHashMap<>();
        Enumeration<String> nombres = req.getParameterNames();
        while (nombres.hasMoreElements()) {
            String nombre = nombres.nextElement();
            String valor = param(req, nombre);
            if (nombre.startsWith(PREFIJO) && !valor.isEmpty()) {
                try {
                    estados.put(nombre.substring(PREFIJO.length()), EstadoInscripcion.valueOf(valor));
                } catch (IllegalArgumentException ignorado) {
                    // valor manipulado: se ignora y el central valida el resto
                }
            }
        }

        try {
            controller.seleccionarEstudiantes(usuario(req).getNickname(), edicion, estados);
            flash(req, "Se guardó la selección de estudiantes.");
        } catch (Exception e) {
            flashError(req, e.getMessage());
        }
        redirigir(req, resp, RUTA + "?edicion=" + url(edicion));
    }
}
