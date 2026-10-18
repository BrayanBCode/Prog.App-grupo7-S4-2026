package ServidorCentral.Logica.controller;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.datatypes.DTCursoDetalle;
import ServidorCentral.Logica.datatypes.DTMisCursos;
import ServidorCentral.Logica.datatypes.DTEdicionDetalle;
import ServidorCentral.Logica.datatypes.DTInscripcionesEdicion;
import ServidorCentral.Logica.datatypes.DTPerfil;
import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaInscripto;
import ServidorCentral.Logica.datatypes.DTInscripcionesEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaFormacion;
import ServidorCentral.Logica.datatypes.DTProgramaResumen;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.datatypes.DTUsuarioResumen;
import ServidorCentral.Logica.datatypes.OrdenBusqueda;
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
    /** Alta de Usuario desde Swing: imagen = ruta de un archivo local (o null). */
    void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen, String contraseña);
    /** Alta de Usuario desde el Servidor Web: imagen subida (o null). Tira IllegalArgumentException con el mensaje para el usuario. */
    void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, DTImagen imagen, String contraseña);
    /** Alta de Usuario (aviso Ajax): true si el nickname no esta en uso. */
    boolean nicknameDisponible(String nickname);
    /** Alta de Usuario (aviso Ajax): true si el correo no esta en uso. */
    boolean mailDisponible(String mail);
    /** Inicio de Sesion (Visitante): nickname o mail + contraseña. Tira IllegalArgumentException si no son validos. */
    DTSesion iniciarSesion(String identificador, String contraseña);
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
    boolean existePrograma(String nombre);
    DTProgramaFormacion obtenerDatosBasicosPrograma(String nombre);
    List<DTProgramaResumen> listarProgramasTabla();
    List<String> listarInstitutos();
    List<DTDocenteResumen> listarDocentesEdicion(String nombreEdicion);
    List<DTEstudianteResumen> listarEstudiantesEdicion(String nombreEdicion);
    /** Inscripcion a Programa de Formacion (Estudiante). Falla si el programa no existe o ya esta inscripto. */
    void inscribirEstudiantePrograma(String nickname, String mail, String nombrePrograma, LocalDate fechaInscripcion) throws Exception;
    /**
     * Seguir a un Usuario (Estudiante/Docente), por nickname. Devuelve la nueva cantidad de seguidores del seguido.
     * ReglaNegocioException si no existe, es uno mismo o ya lo sigue.
     */
    int seguirUsuario(String nicknameSeguidor, String nicknameSeguido);
    /** Dejar de seguir a un Usuario. Devuelve la nueva cantidad de seguidores. ReglaNegocioException si no lo seguia. */
    int dejarDeSeguirUsuario(String nicknameSeguidor, String nicknameSeguido);
    boolean estaSiguiendo(String nicknameSeguidor, String nicknameSeguido);
    /** Nicknames de los usuarios que sigue este usuario. */
    List<String> listarSeguidos(String nickname);
    /** Nicknames de los usuarios que siguen a este usuario. */
    List<String> listarSeguidores(String nickname);
    /**
     * Seleccionar estudiantes (Docente, Figura 5): encabezado de la edicion y todas sus inscripciones con su estado.
     * ReglaNegocioException si la edicion no existe o el docente no participa en ella.
     */
    DTInscripcionesEdicion consultarInscripcionesEdicion(String nicknameDocente, String nombreEdicion);
    /** Listar aceptados (Docente): como el anterior, solo las inscripciones en estado Aceptada. */
    DTInscripcionesEdicion consultarAceptadosEdicion(String nicknameDocente, String nombreEdicion);
    /**
     * Guarda la seleccion: nickname del estudiante -> ACEPTADA o RECHAZADA (solo desde INSCRIPTO), solo en la
     * edicion vigente (D2). Todo o nada. ReglaNegocioException con el mensaje para el usuario.
     */
    void seleccionarEstudiantes(String nicknameDocente, String nombreEdicion, Map<String, EstadoInscripcion> estados);

    // ---------------- Sprint 2: Servidor Web ----------------
    /** Consulta de Usuario: todos los usuarios con sus datos basicos, ordenados por nickname. */
    List<DTUsuario> listarUsuarios();
    /**
     * Consulta de Usuario: el perfil de {@code nickConsultado} con lo que puede ver {@code nickQueMira}
     * (null = Visitante). Tira ReglaNegocioException si el usuario no existe.
     */
    DTPerfil obtenerPerfil(String nickConsultado, String nickQueMira);
    /** Consulta de Curso: cursos del instituto con descripcion e imagen. ReglaNegocioException si el instituto no existe. */
    List<DTCursoResumen> listarCursosInstituto(String nombreInstituto);
    /** Consulta de Curso (Figura 3): curso + ediciones + programas. ReglaNegocioException si el curso no existe. */
    DTCursoDetalle consultarCurso(String nombreCurso);
    /** "Mis cursos y ediciones" del docente (acceso rapido web). ReglaNegocioException si no es docente. */
    DTMisCursos misCursosDocente(String nickDocente);
    /** Consulta de Edicion (Figura 4): edicion + instituto + docentes + vigente + estado de la inscripcion de quien mira (null = Visitante). ReglaNegocioException si no existe. */
    DTEdicionDetalle consultarEdicion(String nombreEdicion, String nickQueMira);
    /** Inscripcion a Edicion (web): el estudiante se inscribe a la edicion vigente, hoy, en estado Inscripto. ReglaNegocioException si no puede. */
    void inscribirse(String nickEstudiante, String nombreEdicion);
    /**
     * Alta de Curso (web): el docente del curso es el que da el alta; previas e imagen opcionales.
     * Tira ReglaNegocioException con el mensaje para el usuario.
     */
    void altaCurso(String nickDocente, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos,
                   String url, String nombreInstituto, List<String> previas, DTImagen imagen);
    /**
     * Alta de Edicion (web): sin superposicion con otra edicion del curso (D1); el docente que da el alta
     * siempre participa (D3). cupo null = sin limite. Tira ReglaNegocioException con el mensaje para el usuario.
     */
    void altaEdicion(String nickDocente, String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin,
                     Integer cupo, List<String> nicknamesDocentes, DTImagen imagen);
    /** Consulta de Usuario por nickname (el sitio web navega por nickname). null si no existe. */
    DTUsuario obtenerUsuario(String nickname);
    /** Inscripciones a ediciones del estudiante, con su estado (Inscripto / Aceptada / Rechazada). */
    List<DTInscripcionEstudiante> listarInscripcionesEstudiante(String nickname);
    /**
     * Listar resultados de inscripciones (4.9, Estudiante): sus inscripciones a ediciones con su estado, de la
     * mas nueva a la mas vieja. ReglaNegocioException si no es un estudiante.
     */
    List<DTInscripcionEstudiante> resultadosInscripciones(String nickEstudiante);
    /**
     * Modificar Datos de Usuario (4.11, web): nombre, apellido y fecha de nacimiento (nickname y correo no se
     * cambian). ReglaNegocioException con el mensaje para el usuario.
     */
    void modificarDatosUsuario(String nickname, String nombre, String apellido, LocalDate fechaNac);
    /** Busqueda (7.5): cursos cuyo nombre o descripcion contienen el texto (vacio = todos), en el orden elegido (null = alfabetico). */
    List<DTCursoResumen> buscarCursos(String texto, OrdenBusqueda orden);
    /** Programas de formacion a los que se inscribio el estudiante. */
    List<DTProgramaInscripto> listarProgramasEstudiante(String nickname);
    /** Alta de Curso (Servidor Web) con imagen opcional (ruta de un archivo local JPG/PNG, o null). */
    void altaCursoConImagen(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas, String imagen) throws Exception;
    /** Alta de Edicion de Curso con imagen opcional. */
    void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes, String imagen) throws Exception;
}
