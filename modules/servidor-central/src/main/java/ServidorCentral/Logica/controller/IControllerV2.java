package ServidorCentral.Logica.controller;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTInscripcionEdicion;
import ServidorCentral.Logica.datatypes.DTInscripcionesEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaFormacion;
import ServidorCentral.Logica.datatypes.DTProgramaResumen;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.datatypes.DTUsuarioResumen;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Contrato del Controller en capas (ControllerV2).
 * <p>
 * Igual que IController, pero en vez de String[] y List&lt;String&gt; con
 * texto ya formateado devuelve DataTypes (ver paquete datatypes). El texto
 * que se muestra al usuario ("(Sin previas)", "Nombre Apellido (nick)", etc.)
 * lo arma la capa de presentacion.
 */
public interface IControllerV2 {
    void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen, String contraseña);
    /** Inicio de Sesion (Visitante): nickname o mail + contraseña. Tira IllegalArgumentException si no son validos. */
    DTSesion iniciarSesion(String identificador, String contraseña);
    /** Alta de Categoria (Administrador, Estacion de Trabajo). El nombre es unico. */
    void altaCategoria(String nombre) throws Exception;
    List<String> listarCategorias();
    void altaInstituto(String nombre) throws Exception;
    void modificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception;
    void crearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta)throws Exception;
    List<DTUsuarioResumen> listarUsuariosTabla();  
    DTUsuario obtenerDataUsuario(String nickname,String mail);
    DTInscripcionesEstudiante obtenerEdicionesYProgramas(String nickname);
    boolean esDocente(String nickname);
    DTDocenteDetalle obtenerDataDocente(String nickname);
    List<String> listarNombreProgramas();
    List<String> listarNombresCursos();
    void agregarCursoPrograma(String nombreP, String nombreC) throws Exception;
    List<DTDocenteResumen> listarDocentesTabla();
    List<DTDocenteResumen> listarDocentesPorInstituto(String nombreInstituto);
    List<String> listarNombresInstitutos();
    List<DTCursoResumen> listarCursosTabla(String nombreInstituto);
    void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception;
    DTProgramaFormacion obtenerDataPrograma(String nombrePrograma)throws Exception;
    void modificarPorgrama(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) throws Exception;
    List<DTEstudianteResumen> listarEstudiantesTabla();
    String obtenerEdicionVigente(String nombreCurso);
    void inscribirEstudianteEdicion(String nickname, String mail, String nombreEdicion, LocalDate fechaInscripcion) throws Exception;
    List<String> listarCursosPorInstituto(String nombreInstituto);
    List<String> listarEdicionesCurso(String nombreCurso);
    DTCurso obtenerDataCurso(String nombreCurso) throws Exception;
    List<String> listarProgramasPorCurso(String nombreCurso);
    DTEdicionCurso obtenerEdicionCurso(String nombreEdicion)throws Exception;
    void altaCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas) throws Exception;
    /** Alta de Curso eligiendo categorias (cada una debe existir). */
    void altaCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas, List<String> categorias) throws Exception;
    boolean existePrograma(String nombre);
    DTProgramaFormacion obtenerDatosBasicosPrograma(String nombre);
    List<DTProgramaResumen> listarProgramasTabla();
    List<String> listarInstitutos();
    List<DTDocenteResumen> listarDocentesEdicion(String nombreEdicion);
    List<DTEstudianteResumen> listarEstudiantesEdicion(String nombreEdicion);
    /** Inscripcion a Programa de Formacion (Estudiante). Falla si el programa no existe o ya esta inscripto. */
    void inscribirEstudiantePrograma(String nickname, String mail, String nombrePrograma, LocalDate fechaInscripcion) throws Exception;
    /** Seguir a un Usuario (Estudiante/Docente), por nickname. Falla si no existe, es uno mismo o ya lo sigue. */
    void seguirUsuario(String nicknameSeguidor, String nicknameSeguido) throws Exception;
    boolean estaSiguiendo(String nicknameSeguidor, String nicknameSeguido);
    /** Nicknames de los usuarios que sigue este usuario. */
    List<String> listarSeguidos(String nickname);
    /** Nicknames de los usuarios que siguen a este usuario. */
    List<String> listarSeguidores(String nickname);
    /** Seleccionar estudiantes (Docente): inscripciones de la edicion con su estado. El docente debe participar en la edicion. */
    List<DTInscripcionEdicion> listarInscripcionesEdicion(String nicknameDocente, String nombreEdicion) throws Exception;
    /** Listar aceptados (Docente): solo inscripciones en estado Aceptada. El docente debe participar en la edicion. */
    List<DTInscripcionEdicion> listarAceptadosEdicion(String nicknameDocente, String nombreEdicion) throws Exception;
    /** Guarda la seleccion: nickname del estudiante -> ACEPTADA o RECHAZADA (solo desde INSCRIPTO). Todo o nada. */
    void seleccionarEstudiantes(String nicknameDocente, String nombreEdicion, Map<String, EstadoInscripcion> estados) throws Exception;
}

