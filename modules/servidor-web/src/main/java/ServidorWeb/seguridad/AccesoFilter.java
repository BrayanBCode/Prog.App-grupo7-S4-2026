package ServidorWeb.seguridad;

import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.seguridad.CasoDeUso;
import ServidorCentral.Logica.seguridad.Rol;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hace dos cosas en TODAS las peticiones:
 *
 * 1) Publica en el request lo que necesitan los JSP para mostrar u ocultar opciones:
 *      ${rol}            -> Rol del actor actual (VISITANTE / ESTUDIANTE / DOCENTE)
 *      ${usuarioActual}  -> DTSesion (null si es Visitante)
 *      ${puede.XXX}      -> true/false segun la matriz CasoDeUso. Ej: ${puede.INICIO_SESION}
 *
 * 2) Hace cumplir la matriz en el SERVIDOR para las rutas de RUTAS_PROTEGIDAS: esconder un
 *    link no alcanza, alguien podria escribir la URL a mano. Para sumar un caso de uso nuevo
 *    se agrega una linea al mapa.
 */
@WebFilter("/*")
public class AccesoFilter implements Filter {

    private static final Map<String, CasoDeUso> RUTAS_PROTEGIDAS = Map.of(
            "/login", CasoDeUso.INICIO_SESION,
            "/logout", CasoDeUso.CIERRE_SESION,
            "/registro", CasoDeUso.ALTA_USUARIO
    );

    /** Permisos ya calculados por rol (la matriz no cambia en ejecucion). */
    private static final Map<Rol, Map<String, Boolean>> PERMISOS_POR_ROL = new EnumMap<>(Rol.class);

    static {
        for (Rol rol : Rol.values()) {
            Map<String, Boolean> permisos = new LinkedHashMap<>();
            for (CasoDeUso cu : CasoDeUso.values()) {
                permisos.put(cu.name(), cu.permitidoPara(rol));
            }
            PERMISOS_POR_ROL.put(rol, Map.copyOf(permisos));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        DTSesion usuario = SesionUsuario.actual(req);
        Rol rol = (usuario == null) ? Rol.VISITANTE : usuario.getRol();

        req.setAttribute("rol", rol);
        req.setAttribute("usuarioActual", usuario);
        req.setAttribute("puede", PERMISOS_POR_ROL.get(rol));

        CasoDeUso requerido = RUTAS_PROTEGIDAS.get(req.getServletPath());
        if (requerido != null && !requerido.permitidoPara(rol)) {
            // Visitante donde hace falta sesion -> al login. Usuario logueado donde no le corresponde -> al inicio.
            String destino = rol.isAutenticado() ? "/inicio" : "/login";
            resp.sendRedirect(req.getContextPath() + destino);
            return;
        }

        chain.doFilter(request, response);
    }
}
