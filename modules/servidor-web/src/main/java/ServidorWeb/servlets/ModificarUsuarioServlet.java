package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorWeb.seguridad.SesionUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Caso de uso "Modificar Datos de Usuario" (4.11). Actor: Estudiante o Docente, siempre sobre sus propios datos (lo
 * garantiza el AccesoFilter; el nickname sale de la sesion, no del formulario). Se entra por los lapices de la
 * pestaña General del perfil propio (Figura 6). Nickname y correo no se modifican.
 */
@WebServlet("/usuarios/modificar")
public class ModificarUsuarioServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DTUsuario yo = controller.obtenerUsuario(usuario(req).getNickname());
        if (yo == null) {
            flashError(req, "No se encontraron tus datos.");
            redirigir(req, resp, "/inicio");
            return;
        }
        req.setAttribute("nombre", yo.nombre());
        req.setAttribute("apellido", yo.apellido());
        req.setAttribute("fechaNac", yo.fechaNac());
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        DTSesion sesion = usuario(req);
        String nombre = param(req, "nombre");
        String apellido = param(req, "apellido");
        try {
            controller.modificarDatosUsuario(sesion.getNickname(), nombre, apellido, fecha(param(req, "fechaNac")));
            // El saludo del inicio usa el nombre de la sesion: se actualiza para no mostrar el viejo
            SesionUsuario.actualizar(req, new DTSesion(sesion.getNickname(), sesion.getMail(), nombre, apellido,
                    sesion.getImagen(), sesion.getRol()));
            flash(req, "Se actualizaron tus datos.");
            redirigir(req, resp, "/usuarios?nick=" + url(sesion.getNickname()));
        } catch (RuntimeException e) {
            // Vuelve al formulario con lo que se ingreso
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("nombre", nombre);
            req.setAttribute("apellido", apellido);
            req.setAttribute("fechaNac", param(req, "fechaNac"));
            mostrarFormulario(req, resp);
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("titulo", "Modificar mis datos");
        vista(req, resp, "usuario/modificar.jsp");
    }
}
