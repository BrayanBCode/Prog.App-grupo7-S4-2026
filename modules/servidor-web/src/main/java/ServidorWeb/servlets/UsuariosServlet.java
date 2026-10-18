package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTPerfil;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Consulta de Usuario" (Visitante, Estudiante, Docente).
 * <ul>
 *   <li>/usuarios          -> lista de todos los usuarios</li>
 *   <li>/usuarios?nick=X   -> perfil de X. Que se ve segun quien mira lo decide el central
 *       (PerfilService); esta pagina solo muestra lo que viene en el DTPerfil.</li>
 * </ul>
 */
@WebServlet("/usuarios")
public class UsuariosServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nick = param(req, "nick");
        if (nick.isEmpty()) {
            listar(req, resp);
            return;
        }
        DTSesion yo = usuario(req);
        try {
            DTPerfil perfil = controller.obtenerPerfil(nick, yo == null ? null : yo.getNickname());
            req.setAttribute("titulo", perfil.usuario().nickname());
            req.setAttribute("perfil", perfil);
            vista(req, resp, "usuario/perfil.jsp");
        } catch (RuntimeException e) {
            if (e instanceof ReglaNegocioException) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            req.setAttribute("error", mensajeDeError(e));
            listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Usuarios");
        req.setAttribute("usuarios", controller.listarUsuarios());
        vista(req, resp, "usuario/lista.jsp");
    }
}
