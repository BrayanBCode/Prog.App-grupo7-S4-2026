package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Caso de uso "Alta de Curso" (Docente). El docente que da de alta el curso es el de la
 * sesion. Indica instituto, datos basicos, previas (0..n) e imagen opcional.
 * Si el nombre ya existe se vuelve al formulario con los datos cargados para corregirlos.
 */
@WebServlet("/curso/alta")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)
public class AltaCursoServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        formulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombre = param(req, "nombre");
        String descripcion = param(req, "descripcion");
        String instituto = param(req, "instituto");
        String url = param(req, "url");
        List<String> previas = valores(req, "previas");

        String error = null;
        int duracion = 0, creditos = 0;
        float horas = 0;
        try {
            duracion = Integer.parseInt(param(req, "duracion"));
            horas = Float.parseFloat(param(req, "horas"));
            creditos = Integer.parseInt(param(req, "creditos"));
        } catch (NumberFormatException e) {
            error = "Duración, cantidad de horas y créditos deben ser números.";
        }
        if (nombre.isEmpty() || descripcion.isEmpty() || instituto.isEmpty()) {
            error = "Completá el nombre, la descripción y el instituto.";
        } else if (error == null && (duracion <= 0 || horas <= 0 || creditos <= 0)) {
            error = "Duración, cantidad de horas y créditos deben ser mayores a cero.";
        }

        if (error == null) {
            Path imagen = null;
            try {
                imagen = imagenTemporal(req, "imagen");
                controller.altaCursoConImagen(nombre, descripcion, duracion, horas, creditos, url, instituto,
                        usuario(req).getNickname(), previas, imagen == null ? null : imagen.toString());
                flash(req, "Se dio de alta el curso " + nombre + ".");
                redirigir(req, resp, "/cursos?nombre=" + url(nombre));
                return;
            } catch (Exception e) {
                error = e.getMessage();
            } finally {
                borrar(imagen);
            }
        }

        req.setAttribute("error", error);
        req.setAttribute("previasElegidas", previas);
        formulario(req, resp);
    }

    private void formulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Alta de curso");
        req.setAttribute("todosLosCursos", controller.listarNombresCursos());
        vista(req, resp, "curso/alta.jsp");
    }

    private static List<String> valores(HttpServletRequest req, String nombre) {
        String[] v = req.getParameterValues(nombre);
        return v == null ? List.of() : Arrays.asList(v);
    }
}
