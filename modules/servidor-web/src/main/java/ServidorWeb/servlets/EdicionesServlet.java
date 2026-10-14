package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.seguridad.Rol;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso "Consulta de Edicion de Curso" (Visitante, Estudiante, Docente).
 * <ul>
 *   <li>/ediciones                         -> elegir instituto</li>
 *   <li>/ediciones?instituto=X             -> cursos del instituto</li>
 *   <li>/ediciones?curso=X                 -> ediciones del curso</li>
 *   <li>/ediciones?nombre=X                -> datos de la edicion y sus docentes</li>
 * </ul>
 */
@WebServlet("/ediciones")
public class EdicionesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombre = param(req, "nombre");
        if (!nombre.isEmpty()) {
            mostrarEdicion(req, resp, nombre);
            return;
        }

        String curso = param(req, "curso");
        String instituto = param(req, "instituto");
        req.setAttribute("titulo", "Ediciones de cursos");

        if (!curso.isEmpty()) {
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
        } else if (!instituto.isEmpty()) {
            List<String> cursos = new ArrayList<>();
            for (DTCursoResumen c : controller.listarCursosTabla(instituto)) {
                cursos.add(c.nombre());
            }
            req.setAttribute("filtro", instituto);
            req.setAttribute("cursos", cursos);
        }
        vista(req, resp, "edicion/lista.jsp");
    }

    private void mostrarEdicion(HttpServletRequest req, HttpServletResponse resp, String nombre)
            throws ServletException, IOException {
        DTEdicionCurso edicion;
        DTCurso curso;
        try {
            edicion = controller.obtenerEdicionCurso(nombre);
            curso = controller.obtenerDataCurso(edicion.nombreCurso());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("titulo", "Edición no encontrada");
            req.setAttribute("error", "No existe una edición llamada \"" + nombre + "\".");
            vista(req, resp, "edicion/lista.jsp");
            return;
        }

        List<DTDocenteResumen> docentes = controller.listarDocentesEdicion(nombre);
        DTSesion yo = usuario(req);
        boolean participa = false;
        if (yo != null && yo.getRol() == Rol.DOCENTE) {
            for (DTDocenteResumen d : docentes) {
                participa |= d.nickname().equals(yo.getNickname());
            }
        }

        req.setAttribute("titulo", edicion.nombre());
        req.setAttribute("edicion", edicion);
        req.setAttribute("curso", curso);
        req.setAttribute("docentes", docentes);
        req.setAttribute("participa", participa);
        req.setAttribute("esVigente", nombre.equals(controller.obtenerEdicionVigente(curso.nombre())));
        vista(req, resp, "edicion/detalle.jsp");
    }
}
