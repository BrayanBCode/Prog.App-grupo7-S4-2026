package ServidorWeb.ws;

import ServidorCentral.DataTypes.DTSesion;
import ServidorCentral.Logica.seguridad.Rol;
import ServidorWeb.config.ConfiguracionWeb;
import ServidorWeb.ws.central.DTCursoDetalle;
import ServidorWeb.ws.central.DTCursoResumen;
import ServidorWeb.ws.central.DTDocenteResumen;
import ServidorWeb.ws.central.DTEdicionDetalle;
import ServidorWeb.ws.central.DTImagen;
import ServidorWeb.ws.central.DTInscripcionEstudiante;
import ServidorWeb.ws.central.DTInscripcionesEdicion;
import ServidorWeb.ws.central.DTMisCursos;
import ServidorWeb.ws.central.DTPerfil;
import ServidorWeb.ws.central.DTSeleccion;
import ServidorWeb.ws.central.DTUsuario;
import ServidorWeb.ws.central.OrdenBusqueda;
import ServidorWeb.ws.central.ReglaNegocioFault_Exception;
import ServidorWeb.ws.central.ServicioCentral;
import ServidorWeb.ws.central.ServicioCentral_Service;
import jakarta.servlet.ServletContext;
import jakarta.xml.ws.BindingProvider;

import java.time.LocalDate;
import java.util.List;

/**
 * Cliente de los Web Services del Servidor Central, el unico punto del sitio que habla SOAP.
 * <ul>
 *   <li>La direccion sale de "central.url" en ~/.edEXT/web.properties ({@link ConfiguracionWeb}).</li>
 *   <li>Las fechas van como texto ISO (yyyy-MM-dd).</li>
 *   <li>Un ReglaNegocioFault se convierte en {@link ErrorNegocio}; si el central no responde
 *       llega una WebServiceException. Las dos son RuntimeException, como antes con el controller.</li>
 * </ul>
 */
public class Central {

    private static final String ATRIBUTO = Central.class.getName();

    private final ServicioCentral puerto;

    private Central(String url) {
        puerto = new ServicioCentral_Service().getServicioCentralPort();
        ((BindingProvider) puerto).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    }

    /** El cliente compartido por toda la aplicacion web (se crea la primera vez que se pide). */
    public static synchronized Central de(ServletContext contexto) {
        Central central = (Central) contexto.getAttribute(ATRIBUTO);
        if (central == null) {
            String url = ConfiguracionWeb.get().valor(ConfiguracionWeb.CENTRAL_URL);
            if (url == null) {
                throw new IllegalStateException("Falta central.url en ~/.edEXT/web.properties");
            }
            contexto.log("Servidor Central en " + url);
            central = new Central(url);
            contexto.setAttribute(ATRIBUTO, central);
        }
        return central;
    }

    /** Una llamada al central que puede devolver un ReglaNegocioFault. */
    private interface Llamada<T> {
        T ejecutar() throws ReglaNegocioFault_Exception;
    }

    private static <T> T llamar(Llamada<T> llamada) {
        try {
            return llamada.ejecutar();
        } catch (ReglaNegocioFault_Exception e) {
            throw new ErrorNegocio(e.getMessage());
        }
    }

    private static String texto(LocalDate fecha) {
        return fecha == null ? null : fecha.toString();
    }

    // ---------------- Sesion y usuarios ----------------

    /** La sesion se guarda como DTSesion (Serializable) para que Tomcat pueda persistirla. */
    public DTSesion iniciarSesion(String identificador, String contrasena) {
        ServidorWeb.ws.central.DTSesion s = llamar(() -> puerto.iniciarSesion(identificador, contrasena));
        return new DTSesion(s.getNickname(), s.getMail(), s.getNombre(), s.getApellido(), s.getImagen(),
                Rol.valueOf(s.getRol().name()));
    }

    public void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac,
                            String instituto, DTImagen imagen, String contrasena) {
        llamar(() -> {
            puerto.altaUsuario(nickname, mail, nombre, apellido, texto(fechaNac), instituto, imagen, contrasena);
            return null;
        });
    }

    public boolean nicknameDisponible(String nickname) {
        return puerto.nicknameDisponible(nickname);
    }

    public boolean mailDisponible(String mail) {
        return puerto.mailDisponible(mail);
    }

    public List<DTUsuario> listarUsuarios() {
        return puerto.listarUsuarios();
    }

    /** null si no existe. */
    public DTUsuario obtenerUsuario(String nickname) {
        return puerto.obtenerUsuario(nickname);
    }

    public DTPerfil obtenerPerfil(String nickConsultado, String nickQueMira) {
        return llamar(() -> puerto.obtenerPerfil(nickConsultado, nickQueMira));
    }

    public void modificarDatosUsuario(String nickname, String nombre, String apellido, LocalDate fechaNac) {
        llamar(() -> {
            puerto.modificarDatosUsuario(nickname, nombre, apellido, texto(fechaNac));
            return null;
        });
    }

    public int seguirUsuario(String seguidor, String seguido) {
        return llamar(() -> puerto.seguirUsuario(seguidor, seguido));
    }

    public int dejarDeSeguirUsuario(String seguidor, String seguido) {
        return llamar(() -> puerto.dejarDeSeguirUsuario(seguidor, seguido));
    }

    // ---------------- Institutos y cursos ----------------

    public List<String> listarNombresInstitutos() {
        return puerto.listarNombresInstitutos();
    }

    public List<String> listarNombresCursos() {
        return puerto.listarNombresCursos();
    }

    public List<DTDocenteResumen> listarDocentesPorInstituto(String instituto) {
        return llamar(() -> puerto.listarDocentesPorInstituto(instituto));
    }

    public List<DTCursoResumen> listarCursosInstituto(String instituto) {
        return llamar(() -> puerto.listarCursosInstituto(instituto));
    }

    public List<DTCursoResumen> buscarCursos(String texto, OrdenBusqueda orden) {
        return llamar(() -> puerto.buscarCursos(texto, orden));
    }

    public DTCursoDetalle consultarCurso(String nombre) {
        return llamar(() -> puerto.consultarCurso(nombre));
    }

    public void altaCurso(String nickDocente, String nombre, String descripcion, int duracion, float cantHoras,
                          int cantCreditos, String url, String instituto, List<String> previas, DTImagen imagen) {
        llamar(() -> {
            puerto.altaCurso(nickDocente, nombre, descripcion, duracion, cantHoras, cantCreditos, url, instituto,
                    previas, imagen);
            return null;
        });
    }

    public DTMisCursos misCursosDocente(String nickDocente) {
        return llamar(() -> puerto.misCursosDocente(nickDocente));
    }

    // ---------------- Ediciones e inscripciones ----------------

    public DTEdicionDetalle consultarEdicion(String nombre, String nickQueMira) {
        return llamar(() -> puerto.consultarEdicion(nombre, nickQueMira));
    }

    /** cupo: null = sin limite. */
    public void altaEdicion(String nickDocente, String nombre, String curso, LocalDate fechaInicio, LocalDate fechaFin,
                            Integer cupo, List<String> docentes, DTImagen imagen) {
        llamar(() -> {
            puerto.altaEdicion(nickDocente, nombre, curso, texto(fechaInicio), texto(fechaFin), cupo, docentes, imagen);
            return null;
        });
    }

    public void inscribirse(String nickEstudiante, String edicion) {
        llamar(() -> {
            puerto.inscribirse(nickEstudiante, edicion);
            return null;
        });
    }

    public DTInscripcionesEdicion consultarInscripcionesEdicion(String nickDocente, String edicion) {
        return llamar(() -> puerto.consultarInscripcionesEdicion(nickDocente, edicion));
    }

    public DTInscripcionesEdicion consultarAceptadosEdicion(String nickDocente, String edicion) {
        return llamar(() -> puerto.consultarAceptadosEdicion(nickDocente, edicion));
    }

    public void seleccionarEstudiantes(String nickDocente, String edicion, List<DTSeleccion> selecciones) {
        llamar(() -> {
            puerto.seleccionarEstudiantes(nickDocente, edicion, selecciones);
            return null;
        });
    }

    public List<DTInscripcionEstudiante> resultadosInscripciones(String nickEstudiante) {
        return llamar(() -> puerto.resultadosInscripciones(nickEstudiante));
    }

    // ---------------- Imagenes ----------------

    /** null si no existe. */
    public DTImagen obtenerImagen(String ruta) {
        return puerto.obtenerImagen(ruta);
    }
}
