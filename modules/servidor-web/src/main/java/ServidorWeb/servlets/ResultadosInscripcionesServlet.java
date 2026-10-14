package ServidorWeb.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Caso de uso "Listar resultados de inscripciones a ediciones de cursos" (Estudiante). */
@WebServlet("/inscripciones/resultados")
public class ResultadosInscripcionesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Resultados de mis inscripciones");
        req.setAttribute("inscripciones", controller.listarInscripcionesEstudiante(usuario(req).getNickname()));
        vista(req, resp, "usuario/resultados.jsp");
    }
}
