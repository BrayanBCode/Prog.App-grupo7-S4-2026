package Logica.controller;

import Logica.repositories.*;
import Logica.services.*;
import Persistencia.Conexion;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller en capas: NO abre EntityManager, no arma queries y no valida
 * nada. Su unico trabajo es conectar la pantalla (Swing) con el Service
 * que corresponde.
 * <p>
 * [ Controller ] -> [ Service ] -> [ Repository ] -> [ Base de Datos ]
 */
public class ControllerV2 implements IController {

    private final UsuarioService usuarioService;
    private final DocenteService docenteService;
    private final EstudianteService estudianteService;
    private final CursoService cursoService;
    private final InstitutoService instituoService;
    private final EdicionCursoService edicionCursoService;
    private final ProgramaFormacionService programaFormacionService;

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

        // Capa de reglas de negocio
        this.usuarioService = new UsuarioService(usuarioRepository, institutoRepository, docenteRepository);
        this.docenteService = new DocenteService(docenteRepository);
        this.estudianteService = new EstudianteService(estudianteRepository);
        this.cursoService = new CursoService(cursoRepository, institutoRepository, docenteRepository);
        this.instituoService = new InstitutoService(institutoRepository);
        this.edicionCursoService = new EdicionCursoService(edicionCursoRepository, cursoRepository, docenteRepository, estudianteRepository);
        this.programaFormacionService = new ProgramaFormacionService(programaFormacionRepository, cursoRepository);
    }

    // ---------------------------------------------------------------
    // USUARIOS
    // ---------------------------------------------------------------

    @Override
    public void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen) {
        usuarioService.registrar(nickname, mail, nombre, apellido, fechaNac, instituto, imagen);
    }

    @Override
    public void modificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception {
        usuarioService.modificar(nickname, mail, nombre, apellido, fechaNac);
    }

    @Override
    public List<String[]> listarUsuariosTabla() {
        return usuarioService.listarUsuariosTabla();
    }

    @Override
    public String[] obtenerDataUsuario(String nickname, String mail) {
        return usuarioService.obtenerDataUsuario(nickname, mail);
    }

    @Override
    public boolean esDocente(String nickname) {
        return usuarioService.esDocente(nickname);
    }

    @Override
    public List<String> obtenerEdicionesYProgramas(String nickname) {
        return estudianteService.obtenerEdicionesYProgramas(nickname);
    }

    @Override
    public List<String> obtenerDataDocente(String nickname) {
        return docenteService.obtenerData(nickname);
    }

    @Override
    public List<String[]> listarDocentesTabla() {
        return docenteService.listarDocentesTabla();
    }

    @Override
    public List<String[]> listarDocentesPorInstituto(String nombreInstituto) {
        return docenteService.listarPorInstituto(nombreInstituto);
    }

    @Override
    public List<String[]> listarEstudiantesTabla() {
        return estudianteService.listarEstudiantesTabla();
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
    public List<String[]> listarCursosTabla(String nombreInstituto) {
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
    public String[] obtenerDataCurso(String nombreCurso) throws Exception {
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
    public String[] obtenerEdicionCurso(String nombreEdicion) throws Exception {
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
    public List<String> obtenerDataPrograma(String nombrePrograma) throws Exception {
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
    public String[] obtenerDatosBasicosPrograma(String nombre) {
        return programaFormacionService.obtenerDatosBasicos(nombre);
    }

    @Override
    public List<String[]> listarProgramasTabla() {
        return programaFormacionService.listarProgramasTabla();
    }
}
