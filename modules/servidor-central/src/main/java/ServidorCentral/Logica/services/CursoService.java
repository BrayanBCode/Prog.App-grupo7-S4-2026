package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoDetalle;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import ServidorCentral.Logica.repositories.ProgramaFormacionRepository;
import ServidorCentral.Persistencia.AlmacenImagenes;

import java.util.ArrayList;
import java.util.List;

/**
 * Acá viven las reglas de negocio de Curso: qué es válido y qué no.
 * El Service NO sabe nada de EntityManager ni de JPQL/Criteria API — todo
 * eso se lo delega a los Repository. El Service solo decide "¿esto se
 * puede hacer o no?" y en qué orden.
 */
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstitutoRepository institutoRepository;
    private final DocenteRepository docenteRepository;
    private final EdicionCursoRepository edicionCursoRepository;
    private final ProgramaFormacionRepository programaRepository;

    /** Sin ediciones ni programas: alcanza para el alta y la consulta basica (Swing, tests viejos). */
    public CursoService(CursoRepository cursoRepository, InstitutoRepository institutoRepository, DocenteRepository docenteRepository) {
        this(cursoRepository, institutoRepository, docenteRepository, null, null);
    }

    public CursoService(CursoRepository cursoRepository, InstitutoRepository institutoRepository, DocenteRepository docenteRepository,
                        EdicionCursoRepository edicionCursoRepository, ProgramaFormacionRepository programaRepository) {
        this.cursoRepository = cursoRepository;
        this.institutoRepository = institutoRepository;
        this.docenteRepository = docenteRepository;
        this.edicionCursoRepository = edicionCursoRepository;
        this.programaRepository = programaRepository;
    }

    /**
     * Consulta de Curso (web), paso 1: los cursos del instituto, con descripcion e imagen.
     *
     * @throws ReglaNegocioException si el instituto no existe
     */
    public List<DTCursoResumen> listarCursosInstituto(String nombreInstituto) {
        if (nombreInstituto == null || institutoRepository.buscarPorNombre(nombreInstituto) == null) {
            throw new ReglaNegocioException("El instituto seleccionado no existe.");
        }
        return aTabla(cursoRepository.cursosPorInstituto(nombreInstituto));
    }

    /**
     * Consulta de Curso (web), paso 2 (Figura 3): todos los datos del curso, sus ediciones y
     * programas, en una sola llamada.
     *
     * @throws ReglaNegocioException si el curso no existe
     */
    public DTCursoDetalle consultarCurso(String nombreCurso) {
        Curso c = nombreCurso == null ? null : cursoRepository.buscarPorNombre(nombreCurso);
        if (c == null) {
            throw new ReglaNegocioException("No existe el curso \"" + (nombreCurso == null ? "" : nombreCurso) + "\".");
        }
        List<DTEdicionCurso> ediciones = new ArrayList<>();
        for (EdicionCurso ed : edicionCursoRepository.edicionesDeCurso(c.getNombreC())) {
            ediciones.add(new DTEdicionCurso(ed.getNombre(), ed.getCurso().getNombreC(), ed.getFechaInicio(),
                    ed.getFechaFin(), ed.getCupo(), ed.getFechaPublicacion(), ed.getImagen()));
        }
        return new DTCursoDetalle(aDT(c), ediciones, programaRepository.obtenerPorCurso(c.getNombreC()));
    }

    /**
     * Caso de uso "Alta de Curso". Mismas validaciones que tenía
     * ControllerV1, solo que ahora divididas: cada "¿existe esto?" es una
     * lectura rápida a través del repository correspondiente.
     */
    public void registrar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas) throws Exception {
        registrar(nombre, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, nicknameDocente, previas, null);
    }

    /**
     * Igual que el anterior, con imagen opcional (ruta de un archivo local JPG/PNG):
     * se copia a la carpeta de imagenes y en la BD queda la ruta relativa.
     */
    public void registrar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas, String imagen) throws Exception {

        Curso existente = cursoRepository.buscarPorNombre(nombre);
        if (existente != null) {
            throw new Exception("Ya existe un curso registrado con el nombre: " + nombre);
        }

        Instituto instituto = institutoRepository.buscarPorNombre(nombreInstituto);
        if (instituto == null) {
            throw new Exception("El instituto seleccionado no existe.");
        }

        Docente docente = docenteRepository.buscarPorNickname(nicknameDocente);
        if (docente == null) {
            throw new Exception("El docente seleccionado no existe.");
        }

        String rutaImagen = AlmacenImagenes.guardar(imagen, AlmacenImagenes.CURSOS, nombre);
        cursoRepository.guardar(nombre, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, nicknameDocente, previas, rutaImagen);
    }

    public List<DTCursoResumen> obtenerCursosTabla(String nombreInstituto) {
        return aTabla(cursoRepository.cursosPorInstituto(nombreInstituto));
    }

    /**
     * Caso de uso "Consulta de Curso". ControllerV1 validaba que el curso
     * existiera antes de armar la respuesta -- ese chequeo se había perdido
     * acá, y sin él esto tiraba NullPointerException en vez de un mensaje
     * claro cuando el nombre no correspondía a ningún curso.
     */
    public DTCurso obtenerCurso(String nombreCurso) throws Exception {
        Curso c = cursoRepository.buscarPorNombre(nombreCurso);

        if (c == null) {
            throw new Exception("No se encontró el curso llamado: '" + nombreCurso + "'");
        }
        return aDT(c);
    }

    private static DTCurso aDT(Curso c) {
        // Los textos "(Sin previas)" / "(Sin docente asignado)" los pone la presentacion.
        List<String> previas = new ArrayList<>();
        for (Curso previa : c.getPrevias()) {
            previas.add(previa.getNombreC());
        }

        DTDocenteResumen docente = null;
        if (c.getDocente() != null) {
            docente = new DTDocenteResumen(
                    c.getDocente().getNickname(),
                    c.getDocente().getNombreU(),
                    c.getDocente().getApellido());
        }

        return new DTCurso(
                c.getNombreC(),
                c.getDescripcion(),
                c.getDuracion(),
                c.getCanthoras(),
                c.getCantCreditos(),
                c.getUrl(),
                c.getFechaRegistro(),
                c.getInstituto() != null ? c.getInstituto().getNombre() : null,
                docente,
                previas,
                c.getImagen()
        );
    }

    public List<String> listarNombres() {
        return cursoRepository.nombres();
    }


    public List<String> listarNombresPorInstituto(String nombreInstituto) {
        return cursoRepository.nombresPorInstituto(nombreInstituto);
    }

    /**
     * Cada fila de la tabla de cursos: nombre + descripcion.
     */
    private List<DTCursoResumen> aTabla(List<Curso> lista) {
        List<DTCursoResumen> resultado = new ArrayList<>();
        for (Curso c : lista) {
            resultado.add(new DTCursoResumen(c.getNombreC(), c.getDescripcion(), c.getImagen()));
        }
        return resultado;
    }
}