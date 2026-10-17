package ServidorWeb.seguridad;

import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.seguridad.CasoDeUso;
import ServidorCentral.Logica.seguridad.Rol;
import ServidorWeb.config.Json;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

/**
 * Hace tres cosas en TODAS las peticiones (menos los archivos estaticos):
 *
 * 1) Publica en el request lo que necesitan los JSP para mostrar u ocultar opciones:
 *      ${rol}            -> Rol del actor actual (VISITANTE / ESTUDIANTE / DOCENTE)
 *      ${usuarioActual}  -> DTSesion (null si es Visitante)
 *      ${puede.XXX}      -> true/false segun la matriz CasoDeUso. Ej: ${puede.INICIO_SESION}
 *
 * 2) Hace cumplir la matriz en el SERVIDOR para las rutas de RUTAS_PROTEGIDAS: esconder un
 *    link no alcanza, alguien podria escribir la URL a mano. Para sumar un caso de uso nuevo
 *    se agrega una linea al mapa.
 *
 * 3) Carga lo que muestra el marco de todas las paginas (institutos del
 *    lateral) y el mensaje "flash" de la accion anterior, si hay.
 */
@WebFilter("/*")
public class AccesoFilter implements Filter {

    private static final Map<String, CasoDeUso> RUTAS_PROTEGIDAS = Map.ofEntries(
            entry("/login", CasoDeUso.INICIO_SESION),
            entry("/logout", CasoDeUso.CIERRE_SESION),
            entry("/registro", CasoDeUso.ALTA_USUARIO),
            entry("/curso/alta", CasoDeUso.ALTA_CURSO),
            entry("/edicion/alta", CasoDeUso.ALTA_EDICION_CURSO),
            entry("/edicion/inscripcion", CasoDeUso.INSCRIPCION_EDICION_CURSO),
            entry("/usuarios/seguimiento", CasoDeUso.SEGUIR_USUARIO),
            entry("/edicion/seleccionar", CasoDeUso.SELECCIONAR_ESTUDIANTES_EDICION),
            entry("/edicion/aceptados", CasoDeUso.LISTAR_ACEPTADOS_EDICION),
            // acceso rapido del docente (no es un caso de uso de la letra): se protege con un permiso solo de docentes
            entry("/docente/cursos", CasoDeUso.ALTA_CURSO)
    );

    /**
     * Rutas que se llaman con Ajax (fetch): si no hay permiso se contesta JSON con 401 (sin sesion) o
     * 403 (rol sin permiso), porque un fetch no sigue la redireccion al login como el navegador.
     */
    private static final Set<String> RUTAS_AJAX = Set.of("/edicion/inscripcion", "/usuarios/seguimiento");

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

    private IControllerV2 controller;

    @Override
    public void init(FilterConfig config) {
        this.controller = Fabrica.getInstance().getControllerV2();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String ruta = req.getServletPath();

        // Ruta completa: en un servlet mapeado a "/x/*" (ej. ImagenesServlet) el servletPath es solo "/x"
        if (esEstatico(req.getRequestURI().substring(req.getContextPath().length()))) {
            chain.doFilter(request, response);
            return;
        }

        DTSesion usuario = SesionUsuario.actual(req);
        Rol rol = (usuario == null) ? Rol.VISITANTE : usuario.getRol();

        req.setAttribute("rol", rol);
        req.setAttribute("usuarioActual", usuario);
        req.setAttribute("puede", PERMISOS_POR_ROL.get(rol));

        CasoDeUso requerido = RUTAS_PROTEGIDAS.get(ruta);
        if (requerido != null && !requerido.permitidoPara(rol)) {
            if (RUTAS_AJAX.contains(ruta)) {
                if (rol.isAutenticado()) {
                    Json.error(resp, HttpServletResponse.SC_FORBIDDEN, "Tu rol no puede realizar esta acción.");
                } else {
                    Json.error(resp, HttpServletResponse.SC_UNAUTHORIZED, "Iniciá sesión para continuar.");
                }
                return;
            }
            // Visitante donde hace falta sesion -> al login. Usuario logueado donde no le corresponde -> al inicio.
            String destino = rol.isAutenticado() ? "/inicio" : "/login";
            resp.sendRedirect(req.getContextPath() + destino);
            return;
        }

        req.setAttribute("institutosMenu", controller.listarNombresInstitutos());

        HttpSession sesion = req.getSession(false);
        if (sesion != null) {
            for (String clave : new String[]{"flash", "flashError"}) {
                if (sesion.getAttribute(clave) != null) {
                    req.setAttribute(clave, sesion.getAttribute(clave));
                    sesion.removeAttribute(clave);
                }
            }
        }

        chain.doFilter(request, response);
    }

    private static boolean esEstatico(String ruta) {
        return ruta.startsWith("/css/") || ruta.startsWith("/js/") || ruta.startsWith("/imagenes/")
                || ruta.startsWith("/webjars/");
    }
}
