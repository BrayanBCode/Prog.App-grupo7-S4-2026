package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso "Listar aceptados a una edicion de curso" (Docente).
 * Instituto -> curso -> ediciones del curso -> inscripciones en estado Aceptada.
 */
@WebServlet("/edicion/aceptados")
public class AceptadosServlet extends BaseServlet {

    private static final String RUTA = "/edicion/aceptados";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Estudiantes aceptados");
        String edicion = param(req, "edicion");
        String curso = param(req, "curso");

        if (!edicion.isEmpty()) {
            try {
                req.setAttribute("edicion", controller.obtenerEdicionCurso(edicion));
                req.setAttribute("aceptados", controller.listarAceptadosEdicion(usuario(req).getNickname(), edicion));
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
            }
        } else if (!curso.isEmpty()) {
            List<DTEdicionCurso> ediciones = new ArrayList<>();
            for (String ed : controller.listarEdicionesCurso(curso)) {
                try {
                    ediciones.add(controller.obtenerEdicionCurso(ed));
                } catch (Exception ignorada) {
                    // edicion borrada entre consultas
                }
            }
            req.setAttribute("curso", curso);
            req.setAttribute("ediciones", ediciones);
        } else {
            elegirCurso(req, RUTA);
        }
        vista(req, resp, "edicion/aceptados.jsp");
    }
}
