package ServidorWeb.servlets;

import ServidorWeb.seguridad.SesionUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Cierre de Sesion". Actor: usuario con sesion (Estudiante / Docente).
 * Se hace por POST (boton del cabezal) para que un link o imagen ajena no pueda
 * cerrarle la sesion al usuario. Un GET simplemente vuelve al inicio.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SesionUsuario.cerrar(req);
        resp.sendRedirect(req.getContextPath() + "/inicio");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/inicio");
    }
}
