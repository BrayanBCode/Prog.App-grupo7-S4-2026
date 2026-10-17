package ServidorCentral.Logica.controller;
import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.datatypes.DTCursoDetalle;
import ServidorCentral.Logica.datatypes.DTEdicionDetalle;
import ServidorCentral.Logica.datatypes.DTInscripcionEdicion;
import ServidorCentral.Logica.datatypes.DTPerfil;
import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaInscripto;
import ServidorCentral.Logica.datatypes.DTInscripcionesEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaFormacion;
import ServidorCentral.Logica.datatypes.DTProgramaResumen;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.datatypes.DTUsuarioResumen;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.repositories.*;
import ServidorCentral.Logica.services.*;
import ServidorCentral.Persistencia.Conexion;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
/**
 * Controller en capas: NO abre EntityManager, no arma queries y no valida
 * nada. Su unico trabajo es conectar la pantalla (Swing / Servidor Web) con
 * el Service que corresponde. Devuelve DataTypes (paquete datatypes), nunca
 * String[] ni entidades JPA.
 * <p>
 * [ Controller ] -> [ Service ] -> [ Repository ] -> [ Base de Datos ]
 */
public class ControllerV2 implements IControllerV2 {
    private final UsuarioService usuarioService;
    private final DocenteService docenteService;
    private final EstudianteService estudianteService;
    private final CursoService cursoService;
    private final InstitutoService instituoService;
    private final EdicionCursoService edicionCursoService;
    private final ProgramaFormacionService programaFormacionService;
    private final SeguimientoService seguimientoService;
    private final PerfilService perfilService;
    public ControllerV2() {
        Conexion conexion = Conexion.getInstancia();
        // Capa de acceso a datos
        InstitutoRepository institutoRepository = new InstitutoRepository(conexion);
        CursoRepository cursoRepository = new CursoRepository(conexion);
        DocenteRepository docenteRepository = new DocenteRepository(conexion);
        EstudianteRepository estudianteRepository = new EstudianteRepository(conexion);
        UsuarioRepository usuarioRepository = new UsuarioRepository(conexion);
        EdicionCursoRepository edicionCursoRepository = new EdicionCursoRepository(conexion);
        ProgramaFormacionRepository programaFormacionRepository = new ProgramaFormacionRepository(conexion);
        SeguimientoRepository seguimientoRepository = new SeguimientoRepository(conexion);
        // Capa de reglas de negocio
        this.usuarioService = new UsuarioService(usuarioRepository, institutoRepository, docenteRepository);
        this.docenteService = new DocenteService(docenteRepository);
        this.estudianteService = new EstudianteService(estudianteRepository);
        this.cursoService = new CursoService(cursoRepository, institutoRepository, docenteRepository, edicionCursoRepository, programaFormacionRepository);
        this.instituoService = new InstitutoService(institutoRepository);
        this.edicionCursoService = new EdicionCursoService(edicionCursoRepository, cursoRepository, docenteRepository, estudianteRepository);
        this.programaFormacionService = new ProgramaFormacionService(programaFormacionRepository, cursoRepository, estudianteRepository);
        this.seguimientoService = new SeguimientoService(usuarioRepository, seguimientoRepository);
        this.perfilService = new PerfilService(usuarioRepository, docenteRepository, estudianteRepository, seguimientoRepository);
    }

    @Override
    public List<DTUsuario> listarUsuarios() {
        return perfilService.listarUsuarios();
    }

    @Override
    public DTPerfil obtenerPerfil(String nickConsultado, String nickQueMira) {
        return perfilService.obtenerPerfil(nickConsultado, nickQueMira);
    }

    @Override
    public List<DTCursoResumen> listarCursosInstituto(String nombreInstituto) {
        return cursoService.listarCursosInstituto(nombreInstituto);
    }

    @Override
    public DTCursoDetalle consultarCurso(String nombreCurso) {
        return cursoService.consultarCurso(nombreCurso);
    }

    @Override
    public DTEdicionDetalle consultarEdicion(String nombreEdicion, String nickQueMira) {
        return edicionCursoService.consultarEdicion(nombreEdicion, nickQueMira);
    }

    @Override
    public void inscribirse(String nickEstudiante, String nombreEdicion) {
        edicionCursoService.inscribirse(nickEstudiante, nombreEdicion);
    }

    @Override
    public void altaCurso(String nickDocente, String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos,
                          String url, String nombreInstituto, List<String> previas, DTImagen imagen) {
        cursoService.altaCurso(nickDocente, nombre, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, previas, imagen);
    }

    @Override
    public void altaEdicion(String nickDocente, String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin,
                            Integer cupo, List<String> nicknamesDocentes, DTImagen imagen) {
        edicionCursoService.altaEdicion(nickDocente, nombreEdicion, nombreCurso, fechaInicio, fechaFin, cupo, nicknamesDocentes, imagen);
    }

    // ---------------------------------------------------------------
    // USUARIOS
    // ---------------------------------------------------------------

    @Override
    public void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen, String contraseña) {
        usuarioService.registrar(nickname, mail, nombre, apellido, fechaNac, instituto, imagen,contraseña);
    }

    @Override
    public void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, DTImagen imagen, String contraseña) {
        usuarioService.registrar(nickname, mail, nombre, apellido, fechaNac, instituto, imagen, contraseña);
    }

    @Override
    public void modificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception {
        usuarioService.modificar(nickname, mail, nombre, apellido, fechaNac);
    }

    @Override
    public List<DTDocenteResumen> listarDocentesEdicion(String nombreEdicion) {
        return edicionCursoService.listarDocentes(nombreEdicion);
    }

    @Override
    public List<DTEstudianteResumen> listarEstudiantesEdicion(String nombreEdicion) {
        return edicionCursoService.listarEstudiantes(nombreEdicion);
    }

    @Override
    public List<DTUsuarioResumen> listarUsuariosTabla() {
        return usuarioService.listarUsuariosTabla();
    }

    @Override
    public DTUsuario obtenerDataUsuario(String nickname, String mail) {
        return usuarioService.obtenerDataUsuario(nickname, mail);
    }

    @Override
    public boolean esDocente(String nickname) {
        return usuarioService.esDocente(nickname);
    }

    @Override
    public DTInscripcionesEstudiante obtenerEdicionesYProgramas(String nickname) {
        return estudianteService.obtenerEdicionesYProgramas(nickname);
    }

    @Override
    public DTDocenteDetalle obtenerDataDocente(String nickname) {
        return docenteService.obtenerData(nickname);
    }

    @Override
    public List<DTDocenteResumen> listarDocentesTabla() {
        return docenteService.listarDocentesTabla();
    }

    @Override
    public List<DTDocenteResumen> listarDocentesPorInstituto(String nombreInstituto) {
        return docenteService.porInstitutoTabla(nombreInstituto);
    }

    @Override
    public List<DTEstudianteResumen> listarEstudiantesTabla() {
        return estudianteService.listarEstudiantesTabla();
    }

    @Override
    public DTSesion iniciarSesion(String identificador, String contraseña) {
        return usuarioService.autenticar(identificador, contraseña);
    }

    // ---------------------------------------------------------------
    // INSTITUTOS
    // ---------------------------------------------------------------

    @Override
    public void altaInstituto(String nombre) throws Exception {
        instituoService.registrar(nombre);
    }

    @Override
    public List<String> listarNombresInstitutos() {
        return instituoService.obtenerNombres();
    }

    @Override
    public List<String> listarInstitutos() {
        return instituoService.obtenerNombres();
    }

    // ---------------------------------------------------------------
    // CURSOS
    // ---------------------------------------------------------------

    @Override
    public void altaCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas) throws Exception {
        cursoService.registrar(nombre, descripcion, duracion, cantHoras, cantCreditos,
                url, nombreInstituto, nicknameDocente, previas);
    }

    @Override
    public List<DTCursoResumen> listarCursosTabla(String nombreInstituto) {
        return cursoService.obtenerCursosTabla(nombreInstituto);
    }

    @Override
    public List<String> listarNombresCursos() {
        return cursoService.listarNombres();
    }

    @Override
    public List<String> listarCursosPorInstituto(String nombreInstituto) {
        return cursoService.listarNombresPorInstituto(nombreInstituto);
    }

    @Override
    public DTCurso obtenerDataCurso(String nombreCurso) throws Exception {
        return cursoService.obtenerCurso(nombreCurso);
    }

    // ---------------------------------------------------------------
    // EDICIONES DE CURSO
    // ---------------------------------------------------------------

    @Override
    public void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception {
        edicionCursoService.registrar(nombreEdicion, nombreCurso, fechaInicio, fechaFin, cupo, nicknamesDocentes);
    }

    @Override
    public List<String> listarEdicionesCurso(String nombreCurso) {
        return edicionCursoService.obtenerNombres(nombreCurso);
    }

    @Override
    public DTEdicionCurso obtenerEdicionCurso(String nombreEdicion) throws Exception {
        return edicionCursoService.obtenerPorNombre(nombreEdicion);
    }

    @Override
    public String obtenerEdicionVigente(String nombreCurso) {
        return edicionCursoService.obtenerEdicionVigente(nombreCurso);
    }

    @Override
    public void inscribirEstudianteEdicion(String nickname, String mail, String nombreEdicion, LocalDate fechaInscripcion) throws Exception {
        edicionCursoService.inscribirEstudiante(nickname, mail, nombreEdicion, fechaInscripcion);
    }

    @Override
    public void inscribirEstudiantePrograma(String nickname, String mail, String nombrePrograma, LocalDate fechaInscripcion) throws Exception {
        programaFormacionService.inscribirEstudiante(nickname, mail, nombrePrograma, fechaInscripcion);
    }

    @Override
    public int seguirUsuario(String nicknameSeguidor, String nicknameSeguido) {
        return seguimientoService.seguir(nicknameSeguidor, nicknameSeguido);
    }

    @Override
    public int dejarDeSeguirUsuario(String nicknameSeguidor, String nicknameSeguido) {
        return seguimientoService.dejarDeSeguir(nicknameSeguidor, nicknameSeguido);
    }

    @Override
    public boolean estaSiguiendo(String nicknameSeguidor, String nicknameSeguido) {
        return seguimientoService.estaSiguiendo(nicknameSeguidor, nicknameSeguido);
    }

    @Override
    public List<String> listarSeguidos(String nickname) {
        return seguimientoService.listarSeguidos(nickname);
    }

    @Override
    public List<String> listarSeguidores(String nickname) {
        return seguimientoService.listarSeguidores(nickname);
    }

    @Override
    public List<DTInscripcionEdicion> listarInscripcionesEdicion(String nicknameDocente, String nombreEdicion) throws Exception {
        return edicionCursoService.listarInscripciones(nicknameDocente, nombreEdicion);
    }

    @Override
    public List<DTInscripcionEdicion> listarAceptadosEdicion(String nicknameDocente, String nombreEdicion) throws Exception {
        return edicionCursoService.listarAceptados(nicknameDocente, nombreEdicion);
    }

    @Override
    public void seleccionarEstudiantes(String nicknameDocente, String nombreEdicion, Map<String, EstadoInscripcion> estados) throws Exception {
        edicionCursoService.seleccionarEstudiantes(nicknameDocente, nombreEdicion, estados);
    }

    // ---------------------------------------------------------------
    // PROGRAMAS DE FORMACION
    // ---------------------------------------------------------------

    @Override
    public void crearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) throws Exception {
        programaFormacionService.crear(nombre, descripcion, fechaInicio, fechaFin, fechaAlta);
    }

    @Override
    public void modificarPorgrama(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        programaFormacionService.modificar(nombre, descripcion, fechaInicio, fechaFin);
    }

    @Override
    public void agregarCursoPrograma(String nombreP, String nombreC) throws Exception {
        programaFormacionService.agregarCurso(nombreP, nombreC);
    }

    @Override
    public DTProgramaFormacion obtenerDataPrograma(String nombrePrograma) throws Exception {
        return programaFormacionService.obtenerPorNombre(nombrePrograma);
    }

    @Override
    public List<String> listarNombreProgramas() {
        return programaFormacionService.listarNombres();
    }

    @Override
    public List<String> listarProgramasPorCurso(String nombreCurso) {
        return programaFormacionService.obtenerPorCurso(nombreCurso);
    }

    @Override
    public boolean existePrograma(String nombre) {
        return programaFormacionService.existe(nombre);
    }

    @Override
    public DTProgramaFormacion obtenerDatosBasicosPrograma(String nombre) {
        return programaFormacionService.obtenerDatosBasicos(nombre);
    }

    @Override
    public List<DTProgramaResumen> listarProgramasTabla() {
        return programaFormacionService.listarProgramasTabla();
    }

    // ---------------------------------------------------------------
    // SPRINT 2 - SERVIDOR WEB
    // ---------------------------------------------------------------

    @Override
    public DTUsuario obtenerUsuario(String nickname) {
        return usuarioService.obtenerPorNickname(nickname);
    }

    @Override
    public List<DTInscripcionEstudiante> listarInscripcionesEstudiante(String nickname) {
        return estudianteService.listarInscripciones(nickname);
    }

    @Override
    public List<DTProgramaInscripto> listarProgramasEstudiante(String nickname) {
        return estudianteService.listarProgramas(nickname);
    }

    @Override
    public void altaCursoConImagen(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas, String imagen) throws Exception {
        cursoService.registrar(nombre, descripcion, duracion, cantHoras, cantCreditos,
                url, nombreInstituto, nicknameDocente, previas, imagen);
    }

    @Override
    public void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes, String imagen) throws Exception {
        edicionCursoService.registrar(nombreEdicion, nombreCurso, fechaInicio, fechaFin, cupo, nicknamesDocentes, imagen);
    }
}
