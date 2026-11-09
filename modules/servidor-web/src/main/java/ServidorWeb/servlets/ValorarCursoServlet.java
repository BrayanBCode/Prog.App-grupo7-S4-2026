package ServidorWeb.servlets;

import ServidorWeb.ws.ErrorNegocio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Caso de uso "Valorar un Curso" (Tarea 3). Actor: Estudiante (lo garantiza el AccesoFilter).
 * <ul>
 *   <li>GET /cursos/valorar -> los cursos en los que participa (aceptado en alguna edicion).</li>
 *   <li>GET /cursos/valorar?curso=X -> ademas, el promedio del curso y su puntaje si ya lo valoro.</li>
 *   <li>POST curso=X, puntaje=1..5 -> lo valora (una sola vez) y vuelve al GET (Post/Redirect/Get).</li>
 * </ul>
 * Se entra por el lateral (INSCRIPCIONES -> Valorar cursos) y desde la edicion en la que fue aceptado.
 * La valoracion es del curso, no de la edicion.
 */
@WebServlet("/cursos/valorar")
public class ValorarCursoServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nick = usuario(req).getNickname();
        String curso = param(req, "curso");
        try {
            List<String> cursos = central.cursosParaValorar(nick);
            req.setAttribute("cursos", cursos);
            if (!curso.isEmpty()) {
                if (!cursos.contains(curso)) {
                    throw new ErrorNegocio("Solo podés valorar cursos en los que participaste.");
                }
                req.setAttribute("curso", curso);
                req.setAttribute("valoracion", central.consultarValoracionCurso(curso, nick));
            }
        } catch (RuntimeException e) {
            req.setAttribute("error", mensajeDeError(e));
        }
        req.setAttribute("titulo", "Valorar cursos");
        vista(req, resp, "curso/valorar.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String curso = param(req, "curso");
        try {
            int puntaje;
            try {
                puntaje = Integer.parseInt(param(req, "puntaje"));
            } catch (NumberFormatException e) {
                throw new ErrorNegocio("Elegí de 1 a 5 estrellas.");
            }
            central.valorarCurso(usuario(req).getNickname(), curso, puntaje);
            flash(req, "Gracias por valorar " + curso + ".");
        } catch (RuntimeException e) {
            flashError(req, mensajeDeError(e));
        }
        redirigir(req, resp, "/cursos/valorar?curso=" + url(curso));
    }
}
