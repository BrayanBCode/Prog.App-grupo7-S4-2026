package ServidorCentral.WebServices;

import ServidorCentral.DataTypes.OrdenBusqueda;
import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Persistencia.AlmacenImagenes;
import ServidorCentral.WebServices.dto.DTCursoDetalleWS;
import ServidorCentral.WebServices.dto.DTCursoResumenWS;
import ServidorCentral.WebServices.dto.DTDocenteResumenWS;
import ServidorCentral.WebServices.dto.DTEdicionDetalleWS;
import ServidorCentral.WebServices.dto.DTImagenWS;
import ServidorCentral.WebServices.dto.DTInscripcionEstudianteWS;
import ServidorCentral.WebServices.dto.DTInscripcionesEdicionWS;
import ServidorCentral.WebServices.dto.DTMisCursosWS;
import ServidorCentral.WebServices.dto.DTNotaWS;
import ServidorCentral.WebServices.dto.DTPerfilWS;
import ServidorCentral.WebServices.dto.DTSeleccionWS;
import ServidorCentral.WebServices.dto.DTSesionWS;
import ServidorCentral.WebServices.dto.DTUsuarioWS;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Operaciones del Servidor Central expuestas por SOAP para el Servidor Web.
 * Delega en el mismo controller que usa la Estacion de Trabajo; los DataTypes viajan como los DTOs
 * de WebServices.dto, las fechas como texto ISO y las ReglaNegocioException como ReglaNegocioFault.
 */
@WebService(serviceName = "ServicioCentral", portName = "ServicioCentralPort")
public class ServicioCentral {

    private final IControllerV2 controller = Fabrica.getInstance().getControllerV2();

    /** Una llamada al controller que puede romper una regla de negocio. */
    private interface Llamada<T> {
        T ejecutar();
    }

    private static <T> T llamar(Llamada<T> llamada) throws ReglaNegocioFault {
        try {
            return llamada.ejecutar();
        } catch (ReglaNegocioException e) {
            throw ReglaNegocioFault.de(e);
        }
    }

    /** SOAP manda una lista vacia como ausente: del otro lado llega null. */
    private static List<String> lista(List<String> valores) {
        return valores == null ? List.of() : valores;
    }

    // ---------------- Sesion y usuarios ----------------

    @WebMethod
    public DTSesionWS iniciarSesion(@WebParam(name = "identificador") String identificador,
                                    @WebParam(name = "contrasena") String contrasena) throws ReglaNegocioFault {
        return llamar(() -> DTSesionWS.de(controller.iniciarSesion(identificador, contrasena)));
    }

    @WebMethod
    public void altaUsuario(@WebParam(name = "nickname") String nickname, @WebParam(name = "mail") String mail,
                            @WebParam(name = "nombre") String nombre, @WebParam(name = "apellido") String apellido,
                            @WebParam(name = "fechaNac") String fechaNac, @WebParam(name = "instituto") String instituto,
                            @WebParam(name = "imagen") DTImagenWS imagen,
                            @WebParam(name = "contrasena") String contrasena) throws ReglaNegocioFault {
        llamar(() -> {
            controller.altaUsuario(nickname, mail, nombre, apellido, Fechas.fecha(fechaNac), instituto,
                    imagen == null ? null : imagen.aDataType(), contrasena);
            return null;
        });
    }

    @WebMethod
    public boolean nicknameDisponible(@WebParam(name = "nickname") String nickname) {
        return controller.nicknameDisponible(nickname);
    }

    @WebMethod
    public boolean mailDisponible(@WebParam(name = "mail") String mail) {
        return controller.mailDisponible(mail);
    }

    @WebMethod
    public List<DTUsuarioWS> listarUsuarios() {
        return controller.listarUsuarios().stream().map(DTUsuarioWS::de).toList();
    }

    /** null si no existe. */
    @WebMethod
    public DTUsuarioWS obtenerUsuario(@WebParam(name = "nickname") String nickname) {
        return DTUsuarioWS.de(controller.obtenerUsuario(nickname));
    }

    @WebMethod
    public DTPerfilWS obtenerPerfil(@WebParam(name = "nickConsultado") String nickConsultado,
                                    @WebParam(name = "nickQueMira") String nickQueMira) throws ReglaNegocioFault {
        return llamar(() -> DTPerfilWS.de(controller.obtenerPerfil(nickConsultado, nickQueMira)));
    }

    @WebMethod
    public void modificarDatosUsuario(@WebParam(name = "nickname") String nickname, @WebParam(name = "nombre") String nombre,
                                      @WebParam(name = "apellido") String apellido,
                                      @WebParam(name = "fechaNac") String fechaNac) throws ReglaNegocioFault {
        llamar(() -> {
            controller.modificarDatosUsuario(nickname, nombre, apellido, Fechas.fecha(fechaNac));
            return null;
        });
    }

    @WebMethod
    public int seguirUsuario(@WebParam(name = "seguidor") String seguidor,
                             @WebParam(name = "seguido") String seguido) throws ReglaNegocioFault {
        return llamar(() -> controller.seguirUsuario(seguidor, seguido));
    }

    @WebMethod
    public int dejarDeSeguirUsuario(@WebParam(name = "seguidor") String seguidor,
                                    @WebParam(name = "seguido") String seguido) throws ReglaNegocioFault {
        return llamar(() -> controller.dejarDeSeguirUsuario(seguidor, seguido));
    }

    // ---------------- Institutos y cursos ----------------

    @WebMethod
    public List<String> listarNombresInstitutos() {
        return controller.listarNombresInstitutos();
    }

    @WebMethod
    public List<String> listarNombresCursos() {
        return controller.listarNombresCursos();
    }

    @WebMethod
    public List<DTDocenteResumenWS> listarDocentesPorInstituto(@WebParam(name = "instituto") String instituto)
            throws ReglaNegocioFault {
        return llamar(() -> controller.listarDocentesPorInstituto(instituto).stream().map(DTDocenteResumenWS::de).toList());
    }

    @WebMethod
    public List<DTCursoResumenWS> listarCursosInstituto(@WebParam(name = "instituto") String instituto)
            throws ReglaNegocioFault {
        return llamar(() -> controller.listarCursosInstituto(instituto).stream().map(DTCursoResumenWS::de).toList());
    }

    @WebMethod
    public List<DTCursoResumenWS> buscarCursos(@WebParam(name = "texto") String texto,
                                               @WebParam(name = "orden") OrdenBusqueda orden) throws ReglaNegocioFault {
        return llamar(() -> controller.buscarCursos(texto, orden).stream().map(DTCursoResumenWS::de).toList());
    }

    @WebMethod
    public DTCursoDetalleWS consultarCurso(@WebParam(name = "nombre") String nombre) throws ReglaNegocioFault {
        return llamar(() -> DTCursoDetalleWS.de(controller.consultarCurso(nombre)));
    }

    @WebMethod
    public void altaCurso(@WebParam(name = "nickDocente") String nickDocente, @WebParam(name = "nombre") String nombre,
                          @WebParam(name = "descripcion") String descripcion, @WebParam(name = "duracion") int duracion,
                          @WebParam(name = "cantHoras") float cantHoras, @WebParam(name = "cantCreditos") int cantCreditos,
                          @WebParam(name = "url") String url, @WebParam(name = "instituto") String instituto,
                          @WebParam(name = "previas") List<String> previas,
                          @WebParam(name = "imagen") DTImagenWS imagen) throws ReglaNegocioFault {
        llamar(() -> {
            controller.altaCurso(nickDocente, nombre, descripcion, duracion, cantHoras, cantCreditos, url, instituto,
                    lista(previas), imagen == null ? null : imagen.aDataType());
            return null;
        });
    }

    @WebMethod
    public DTMisCursosWS misCursosDocente(@WebParam(name = "nickDocente") String nickDocente) throws ReglaNegocioFault {
        return llamar(() -> DTMisCursosWS.de(controller.misCursosDocente(nickDocente)));
    }

    // ---------------- Ediciones e inscripciones ----------------

    @WebMethod
    public DTEdicionDetalleWS consultarEdicion(@WebParam(name = "nombre") String nombre,
                                               @WebParam(name = "nickQueMira") String nickQueMira) throws ReglaNegocioFault {
        return llamar(() -> DTEdicionDetalleWS.de(controller.consultarEdicion(nombre, nickQueMira)));
    }

    /** cupo: null = sin limite. */
    @WebMethod
    public void altaEdicion(@WebParam(name = "nickDocente") String nickDocente, @WebParam(name = "nombre") String nombre,
                            @WebParam(name = "curso") String curso, @WebParam(name = "fechaInicio") String fechaInicio,
                            @WebParam(name = "fechaFin") String fechaFin, @WebParam(name = "cupo") Integer cupo,
                            @WebParam(name = "docentes") List<String> docentes,
                            @WebParam(name = "imagen") DTImagenWS imagen) throws ReglaNegocioFault {
        llamar(() -> {
            controller.altaEdicion(nickDocente, nombre, curso, Fechas.fecha(fechaInicio), Fechas.fecha(fechaFin), cupo,
                    lista(docentes), imagen == null ? null : imagen.aDataType());
            return null;
        });
    }

    @WebMethod
    public void inscribirse(@WebParam(name = "nickEstudiante") String nickEstudiante,
                            @WebParam(name = "edicion") String edicion) throws ReglaNegocioFault {
        llamar(() -> {
            controller.inscribirse(nickEstudiante, edicion);
            return null;
        });
    }

    @WebMethod
    public DTInscripcionesEdicionWS consultarInscripcionesEdicion(@WebParam(name = "nickDocente") String nickDocente,
                                                                  @WebParam(name = "edicion") String edicion)
            throws ReglaNegocioFault {
        return llamar(() -> DTInscripcionesEdicionWS.de(controller.consultarInscripcionesEdicion(nickDocente, edicion)));
    }

    @WebMethod
    public DTInscripcionesEdicionWS consultarAceptadosEdicion(@WebParam(name = "nickDocente") String nickDocente,
                                                              @WebParam(name = "edicion") String edicion)
            throws ReglaNegocioFault {
        return llamar(() -> DTInscripcionesEdicionWS.de(controller.consultarAceptadosEdicion(nickDocente, edicion)));
    }

    @WebMethod
    public void seleccionarEstudiantes(@WebParam(name = "nickDocente") String nickDocente,
                                       @WebParam(name = "edicion") String edicion,
                                       @WebParam(name = "selecciones") List<DTSeleccionWS> selecciones) throws ReglaNegocioFault {
        llamar(() -> {
            controller.seleccionarEstudiantes(nickDocente, edicion, DTSeleccionWS.aMapa(selecciones));
            return null;
        });
    }

    @WebMethod
    public void registrarNotas(@WebParam(name = "nickDocente") String nickDocente,
                               @WebParam(name = "edicion") String edicion,
                               @WebParam(name = "notas") List<DTNotaWS> notas) throws ReglaNegocioFault {
        llamar(() -> {
            controller.registrarNotas(nickDocente, edicion, DTNotaWS.aMapa(notas));
            return null;
        });
    }

    @WebMethod
    public void cerrarEdicion(@WebParam(name = "nickDocente") String nickDocente,
                              @WebParam(name = "edicion") String edicion) throws ReglaNegocioFault {
        llamar(() -> {
            controller.cerrarEdicion(nickDocente, edicion);
            return null;
        });
    }

    @WebMethod
    public List<DTInscripcionEstudianteWS> resultadosInscripciones(@WebParam(name = "nickEstudiante") String nickEstudiante)
            throws ReglaNegocioFault {
        return llamar(() -> controller.resultadosInscripciones(nickEstudiante).stream()
                .map(DTInscripcionEstudianteWS::de).toList());
    }

    // ---------------- Imagenes ----------------

    /**
     * Imagen guardada en el central, por su ruta relativa (la de la BD o la de /imagenes/...).
     * null si no existe o la ruta se sale de la carpeta de imagenes.
     */
    @WebMethod
    public DTImagenWS obtenerImagen(@WebParam(name = "ruta") String ruta) {
        Path archivo = AlmacenImagenes.resolver(ruta);
        if (archivo == null) {
            return null;
        }
        String nombre = archivo.getFileName().toString();
        DTImagenWS imagen = new DTImagenWS();
        try {
            imagen.contenido = Files.readAllBytes(archivo);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        imagen.extension = nombre.substring(nombre.lastIndexOf('.') + 1);
        return imagen;
    }
}
