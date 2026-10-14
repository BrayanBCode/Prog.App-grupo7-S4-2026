package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso "Consulta de Curso" (Visitante, Estudiante, Docente).
 * <ul>
 *   <li>/cursos                      -> elegir instituto</li>
 *   <li>/cursos?instituto=X          -> cursos del instituto</li>
 *   <li>/cursos?nombre=X             -> datos del curso, con sus ediciones y programas</li>
 * </ul>
 */
@WebServlet("/cursos")
public class CursoServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombre = param(req, "nombre");
        String instituto = param(req, "instituto");

        if (!nombre.isEmpty()) {
            mostrarCurso(req, resp, nombre);
            return;
        }

        if (!instituto.isEmpty()) {
            List<String> nombres = new ArrayList<>();
            for (DTCursoResumen c : controller.listarCursosTabla(instituto)) {
                nombres.add(c.nombre());
            }
            req.setAttribute("titulo", "Cursos de " + instituto);
            req.setAttribute("cursos", datosCursos(nombres));
        } else {
            req.setAttribute("titulo", "Cursos");
            req.setAttribute("elegir", true);
        }
        vista(req, resp, "curso/lista.jsp");
    }

    private void mostrarCurso(HttpServletRequest req, HttpServletResponse resp, String nombre)
            throws ServletException, IOException {
        DTCurso curso;
        try {
            curso = controller.obtenerDataCurso(nombre);
        } catch (Exception e) {
            curso = null;
        }
        if (curso == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("titulo", "Curso no encontrado");
            req.setAttribute("error", "No existe un curso llamado \"" + nombre + "\".");
            req.setAttribute("elegir", true);
            vista(req, resp, "curso/lista.jsp");
            return;
        }

        List<DTEdicionCurso> ediciones = new ArrayList<>();
        for (String ed : controller.listarEdicionesCurso(nombre)) {
            try {
                ediciones.add(controller.obtenerEdicionCurso(ed));
            } catch (Exception ignorada) {
                // si la edicion desaparecio entre las dos consultas, no se muestra
            }
        }
        ediciones.sort((a, b) -> b.fechaInicio().compareTo(a.fechaInicio()));

        req.setAttribute("titulo", curso.nombre());
        req.setAttribute("curso", curso);
        req.setAttribute("ediciones", ediciones);
        req.setAttribute("programas", controller.listarProgramasPorCurso(nombre));
        req.setAttribute("edicionVigente", controller.obtenerEdicionVigente(nombre));
        vista(req, resp, "curso/detalle.jsp");
    }

    private List<DTCurso> datosCursos(List<String> nombres) {
        List<DTCurso> resultado = new ArrayList<>();
        for (String n : nombres) {
            try {
                resultado.add(controller.obtenerDataCurso(n));
            } catch (Exception ignorada) {
                // curso borrado entre consultas
            }
        }
        return resultado;
    }

}
