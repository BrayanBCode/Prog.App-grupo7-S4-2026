package ServidorWeb.servlets;

import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorWeb.config.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Casos de uso "Seguir a un Usuario" y "Dejar de seguir a un Usuario" con Ajax (pedido del docente).
 * Actor: Estudiante o Docente (el AccesoFilter contesta 401 JSON sin sesion). Lo llama js/seguimiento.js
 * desde el perfil, sin recargar la pagina. Recibe nick (a quien) y accion (seguir | dejar) y responde
 * {"ok": true, "siguiendo": true, "seguidores": 5} o {"ok": false, "error": "..."}.
 */
@WebServlet("/usuarios/seguimiento")
public class SeguimientoServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String yo = usuario(req).getNickname();
        String nick = param(req, "nick");
        boolean dejar = "dejar".equals(param(req, "accion"));
        try {
            int seguidores = dejar ? controller.dejarDeSeguirUsuario(yo, nick) : controller.seguirUsuario(yo, nick);
            Json.escribir(resp, HttpServletResponse.SC_OK, Map.of("ok", true, "siguiendo", !dejar, "seguidores", seguidores));
        } catch (RuntimeException e) {
            int status = e instanceof ReglaNegocioException
                    ? HttpServletResponse.SC_CONFLICT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            Json.error(resp, status, mensajeDeError(e));
        }
    }
}
