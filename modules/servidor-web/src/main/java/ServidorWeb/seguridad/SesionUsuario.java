package ServidorWeb.seguridad;

import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.seguridad.Rol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Unico punto donde el sitio web lee/escribe la sesion. Regla simple:
 * sin DTSesion en la HttpSession  =>  VISITANTE (anonimo).
 * El rol ADMINISTRADOR nunca aparece aca: usa solo la Estacion de Trabajo.
 */
public final class SesionUsuario {

    private static final String ATRIBUTO = "usuarioActual";
    private static final int INACTIVIDAD_SEGUNDOS = 30 * 60;

    private SesionUsuario() {}

    /** @return los datos del usuario logueado, o null si es un Visitante. */
    public static DTSesion actual(HttpServletRequest req) {
        HttpSession s = req.getSession(false);   // false: NO crea sesion para un visitante
        return (s == null) ? null : (DTSesion) s.getAttribute(ATRIBUTO);
    }

    public static Rol rolActual(HttpServletRequest req) {
        DTSesion u = actual(req);
        return (u == null) ? Rol.VISITANTE : u.getRol();
    }

    /** Inicia la sesion. Se descarta la sesion anterior para evitar session fixation. */
    public static void iniciar(HttpServletRequest req, DTSesion usuario) {
        HttpSession vieja = req.getSession(false);
        if (vieja != null) {
            vieja.invalidate();
        }
        HttpSession nueva = req.getSession(true);
        nueva.setMaxInactiveInterval(INACTIVIDAD_SEGUNDOS);
        nueva.setAttribute(ATRIBUTO, usuario);
    }

    /** Cierre de sesion: el usuario pasa a ser Visitante. */
    public static void cerrar(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }
}
