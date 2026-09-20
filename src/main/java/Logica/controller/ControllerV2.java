package Logica.controller;

import Logica.repositories.CursoRepository;
import Logica.repositories.DocenteRepository;
import Logica.repositories.EdicionCursoRepository;
import Logica.repositories.InstitutoRepository;
import Logica.services.CursoService;
import Logica.services.EdicionCursoService;
import Logica.services.InstitutoService;
import Persistencia.Conexion;

import java.time.LocalDate;
import java.util.List;

public class ControllerV2 implements IController {

    // Por ahora solo armamos la cadena de Curso (el resto de los services
    // se van a ir agregando a medida que se migre cada caso de uso).
    private final CursoService cursoService;
    private final InstitutoService instituoService;
    private final EdicionCursoService edicionCursoService;

    public ControllerV2() {
        Conexion conexion = Conexion.getInstancia();

        InstitutoRepository institutoRepository = new InstitutoRepository(conexion);
        CursoRepository cursoRepository = new CursoRepository(conexion);
        DocenteRepository docenteRepository = new DocenteRepository(conexion);
        EdicionCursoRepository edicionCursoRepository = new EdicionCursoRepository(conexion);

        this.cursoService = new CursoService(cursoRepository, institutoRepository, docenteRepository);
        this.instituoService = new InstitutoService(institutoRepository);
        this.edicionCursoService = new EdicionCursoService(edicionCursoRepository, cursoRepository, docenteRepository);
    }

    @Override
    public void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen) {

    }

    @Override
    public void altaInstituto(String nombre) throws Exception {

    }

    @Override
    public void modificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception {

    }

    @Override
    public void crearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) throws Exception {

    }

    @Override
    public List<String[]> listarUsuariosTabla() {
        return List.of();
    }

    @Override
    public String[] obtenerDataUsuario(String nickname, String mail) {
        return new String[0];
    }

    @Override
    public List<String> obtenerEdicionesYProgramas(String nickname) {
        return List.of();
    }

    @Override
    public boolean esDocente(String nickname) {
        return false;
    }

    @Override
    public List<String> obtenerDataDocente(String nickname) {
        return List.of();
    }

    @Override
    public List<String> listarNombreProgramas() {
        return List.of();
    }

    @Override
    public List<String> listarNombresCursos() {
        return List.of();
    }

    @Override
    public void agregarCursoPrograma(String nombreP, String nombreC) throws Exception {

    }

    @Override
    public List<String[]> listarDocentesTabla() {
        return List.of();
    }

    @Override
    public List<String[]> listarDocentesPorInstituto(String nombreInstituto) {
        return List.of();
    }

    @Override
    public List<String> listarNombresInstitutos() {
        return instituoService.obtenerNombres();
    }

    @Override
    public List<String[]> listarCursosTabla(String nombreInstituto) {
        return cursoService.obtenerCursosTabla(nombreInstituto);
    }

    @Override
    public void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception {

    }

    @Override
    public List<String> obtenerDataPrograma(String nombrePrograma) throws Exception {
        return List.of();
    }

    @Override
    public void modificarPorgrama(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) throws Exception {

    }

    @Override
    public List<String[]> listarEstudiantesTabla() {
        return List.of();
    }

    @Override
    public String obtenerEdicionVigente(String nombreCurso) {
        return "";
    }

    @Override
    public void inscribirEstudianteEdicion(String nickname, String mail, String nombreEdicion, LocalDate fechaInscripcion) throws Exception {

    }

    @Override
    public List<String> listarCursosPorInstituto(String nombreInstituto) {
        return List.of();
    }

    @Override
    public List<String> listarEdicionesCurso(String nombreCurso) {
        return List.of();
    }

    @Override
    public String[] obtenerDataCurso(String nombreCurso) throws Exception {
        return cursoService.obtenerCurso(nombreCurso);
    }

    @Override
    public List<String> listarProgramasPorCurso(String nombreCurso) {
        return List.of();
    }

    @Override
    public String[] obtenerEdicionCurso(String nombreEdicion) throws Exception {
        return new String[0];
    }

    @Override
    public void altaCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas) throws Exception {
        // Fijate el contraste: esto es TODO lo que hace ahora el Controller.
        // Ya no arma queries, ni abre EntityManager, ni valida nada -- solo
        // conecta la pantalla con el Service correspondiente.
        cursoService.registrar(nombre, descripcion, duracion, cantHoras, cantCreditos,
                url, nombreInstituto, nicknameDocente, previas);
    }

    @Override
    public boolean existePrograma(String nombre) {
        return false;
    }

    @Override
    public String[] obtenerDatosBasicosPrograma(String nombre) {
        return new String[0];
    }

    @Override
    public List<String[]> listarProgramasTabla() {
        return List.of();
    }

    @Override
    public List<String> listarInstitutos() {
        return List.of();
    }
}
