package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTImagen;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Caso de uso "Alta de Curso". Actor: Docente (lo garantiza el AccesoFilter). El docente del curso
 * es el que da el alta. /curso/alta?instituto=X deja el instituto ya elegido.
 */
@WebServlet("/curso/alta")
// Limite holgado: el de 2 MB de la imagen lo controla el central, que devuelve un mensaje claro.
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AltaCursoServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("institutoElegido", param(req, "instituto"));
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombre = param(req, "nombre");
        String[] previas = req.getParameterValues("previas");
        try {
            controller.altaCurso(usuario(req).getNickname(), nombre, param(req, "descripcion"),
                    entero(req, "duracion"), (float) decimal(req, "cantHoras"), entero(req, "cantCreditos"),
                    param(req, "url"), param(req, "instituto"),
                    previas == null ? List.of() : Arrays.asList(previas), leerImagen(req, "imagen"));
            flash(req, "Se creó el curso \"" + nombre + "\".");
            redirigir(req, resp, "/cursos?nombre=" + url(nombre));
        } catch (RuntimeException e) {
            // Vuelve al formulario con lo que se ingreso (menos la imagen, que el navegador no reenvia)
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("institutoElegido", param(req, "instituto"));
            req.setAttribute("previasElegidas", previas == null ? List.of() : Arrays.asList(previas));
            mostrarFormulario(req, resp);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Crear curso");
        req.setAttribute("institutos", controller.listarNombresInstitutos());
        req.setAttribute("cursos", controller.listarNombresCursos());
        vista(req, resp, "curso/alta.jsp");
    }

    /** Numero entero del formulario; 0 si no vino o no es un numero (el central lo rechaza con su mensaje). */
    private static int entero(HttpServletRequest req, String campo) {
        try {
            return Integer.parseInt(param(req, campo));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double decimal(HttpServletRequest req, String campo) {
        try {
            return Double.parseDouble(param(req, campo).replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
