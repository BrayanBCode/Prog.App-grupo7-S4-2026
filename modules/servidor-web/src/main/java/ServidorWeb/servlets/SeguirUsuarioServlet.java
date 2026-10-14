package ServidorWeb.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Seguir a un Usuario" (Estudiante, Docente). Es el boton "Seguir" del
 * perfil de otro usuario; el AccesoFilter ya garantiza que hay sesion iniciada.
 */
@WebServlet("/usuarios/seguir")
public class SeguirUsuarioServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String nick = param(req, "nick");
        try {
            controller.seguirUsuario(usuario(req).getNickname(), nick);
            flash(req, "Ahora seguís a " + nick + ".");
        } catch (Exception e) {
            flashError(req, e.getMessage());
        }
        redirigir(req, resp, "/usuarios?nick=" + url(nick));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirigir(req, resp, "/usuarios");
    }
}
