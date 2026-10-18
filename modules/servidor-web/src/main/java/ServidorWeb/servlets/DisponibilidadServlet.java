package ServidorWeb.servlets;

import ServidorWeb.config.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Alta de Usuario con Ajax (pedido del docente): js/registro.js pregunta, al salir del campo, si el nickname o
 * el correo ya estan en uso, sin enviar el formulario. Actor: Visitante (AccesoFilter).
 * /registro/disponible?nickname=X  o  ?mail=X  ->  {"ok": true, "disponible": false, "mensaje": "El nickname ya está en uso."}
 * Es solo una ayuda: el alta lo vuelve a controlar en el central al registrar.
 */
@WebServlet("/registro/disponible")
public class DisponibilidadServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String nickname = param(req, "nickname");
        String mail = param(req, "mail");
        boolean disponible;
        String mensaje;
        if (!nickname.isEmpty()) {
            disponible = controller.nicknameDisponible(nickname);
            mensaje = disponible ? "" : "El nickname ya está en uso.";
        } else if (!mail.isEmpty()) {
            disponible = controller.mailDisponible(mail);
            mensaje = disponible ? "" : "El correo electrónico ya está en uso.";
        } else {
            Json.error(resp, HttpServletResponse.SC_BAD_REQUEST, "Ingrese el nickname o el correo a consultar.");
            return;
        }
        Json.escribir(resp, HttpServletResponse.SC_OK, Map.of("ok", true, "disponible", disponible, "mensaje", mensaje));
    }
}
