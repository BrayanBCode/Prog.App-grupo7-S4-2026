package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.OrdenBusqueda;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Busqueda de cursos (requerimiento 7.5): el cuadro del cabezal manda aca el texto (/buscar?q=X&orden=Y). Lista los cursos
 * cuyo nombre o descripcion lo contienen; sin texto, todos. Para el grupo solo cursos (los programas estan en gris).
 * Disponible para todos (Visitante, Estudiante, Docente).
 */
@WebServlet("/buscar")
public class BusquedaServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String texto = param(req, "q");
        OrdenBusqueda orden = orden(param(req, "orden"));
        try {
            req.setAttribute("cursos", controller.buscarCursos(texto, orden));
        } catch (RuntimeException e) {
            req.setAttribute("error", mensajeDeError(e));
        }
        req.setAttribute("busqueda", texto);
        req.setAttribute("orden", orden);
        req.setAttribute("titulo", texto.isEmpty() ? "Todos los cursos" : "Búsqueda: " + texto);
        vista(req, resp, "curso/busqueda.jsp");
    }

    /** "Ordenar por" de la pagina de resultados; null (sin elegir o un valor que no existe) = alfabetico. */
    private static OrdenBusqueda orden(String valor) {
        try {
            return valor.isEmpty() ? null : OrdenBusqueda.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
