package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Caso de uso "Alta de Edicion de Curso" (Docente). Primero se indica el instituto
 * (?instituto=X) y el sistema lista sus cursos; luego se ingresan nombre, fechas,
 * cupo (opcional), docentes que participan e imagen opcional.
 */
@WebServlet("/edicion/alta")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)
public class AltaEdicionServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<String> docentes = new ArrayList<>();
        docentes.add(usuario(req).getNickname());   // el docente que la crea participa por defecto
        req.setAttribute("docentesElegidos", docentes);
        formulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String curso = param(req, "curso");
        String nombre = param(req, "nombre");
        LocalDate inicio = fecha(param(req, "fechaInicio"));
        LocalDate fin = fecha(param(req, "fechaFin"));
        String cupoTexto = param(req, "cupo");
        String[] elegidos = req.getParameterValues("docentes");
        List<String> docentes = elegidos == null ? List.of() : Arrays.asList(elegidos);

        String error = null;
        int cupo = 0;   // 0 = sin limite
        if (curso.isEmpty() || nombre.isEmpty() || inicio == null || fin == null) {
            error = "Completá el curso, el nombre y las fechas de inicio y fin.";
        } else if (!cupoTexto.isEmpty()) {
            try {
                cupo = Integer.parseInt(cupoTexto);
                if (cupo <= 0) error = "El cupo debe ser mayor a cero (o dejalo vacío si no tiene límite).";
            } catch (NumberFormatException e) {
                error = "El cupo debe ser un número.";
            }
        }

        if (error == null) {
            Path imagen = null;
            try {
                imagen = imagenTemporal(req, "imagen");
                controller.altaEdicionCurso(nombre, curso, inicio, fin, cupo, docentes,
                        imagen == null ? null : imagen.toString());
                flash(req, "Se dio de alta la edición " + nombre + ".");
                redirigir(req, resp, "/ediciones?nombre=" + url(nombre));
                return;
            } catch (Exception e) {
                error = e.getMessage();
            } finally {
                borrar(imagen);
            }
        }

        req.setAttribute("error", error);
        req.setAttribute("docentesElegidos", docentes);
        formulario(req, resp);
    }

    private void formulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Alta de edición de curso");
        String instituto = param(req, "instituto");
        if (!instituto.isEmpty()) {
            req.setAttribute("cursosInstituto", controller.listarCursosPorInstituto(instituto));
            req.setAttribute("docentes", controller.listarDocentesTabla());
        }
        vista(req, resp, "edicion/alta.jsp");
    }
}
