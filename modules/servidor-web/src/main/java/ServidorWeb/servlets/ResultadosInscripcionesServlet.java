package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Listar resultados de inscripciones a ediciones de curso" (4.9). Actor: Estudiante (lo garantiza el
 * AccesoFilter). Se entra por el lateral: INSCRIPCIONES -> Ver resultados (Figura 3).
 */
@WebServlet("/inscripciones/resultados")
public class ResultadosInscripcionesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("resultados", controller.resultadosInscripciones(usuario(req).getNickname()));
        } catch (RuntimeException e) {
            req.setAttribute("error", mensajeDeError(e));
        }
        req.setAttribute("titulo", "Resultados de mis inscripciones");
        vista(req, resp, "inscripcion/resultados.jsp");
    }
}
