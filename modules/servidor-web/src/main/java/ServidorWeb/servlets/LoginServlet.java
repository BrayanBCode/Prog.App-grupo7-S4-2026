package ServidorWeb.servlets;

import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorWeb.seguridad.SesionUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Inicio de Sesion". Actor: Visitante.
 * El AccesoFilter ya garantiza que solo un Visitante llega hasta aca; la logica
 * (verificar datos y determinar si es Estudiante o Docente) esta en el Servidor Central.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private IControllerV2 controller;

    @Override
    public void init() {
        this.controller = Fabrica.getInstance().getControllerV2();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/sesion/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String usuario = req.getParameter("usuario");
        String password = req.getParameter("password");

        try {
            DTSesion sesion = controller.iniciarSesion(usuario, password);
            SesionUsuario.iniciar(req, sesion);
            resp.sendRedirect(req.getContextPath() + "/inicio");
        } catch (IllegalArgumentException datosInvalidos) {
            // Advertencia al visitante para que reingrese o cancele (login.js muestra el mensaje)
            resp.sendRedirect(req.getContextPath() + "/login?error=true");
        }
    }
}
