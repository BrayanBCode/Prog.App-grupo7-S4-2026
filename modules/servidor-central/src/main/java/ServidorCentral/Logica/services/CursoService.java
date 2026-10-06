package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;

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

    public CursoService(CursoRepository cursoRepository, InstitutoRepository institutoRepository, DocenteRepository docenteRepository) {
        this.cursoRepository = cursoRepository;
        this.institutoRepository = institutoRepository;
        this.docenteRepository = docenteRepository;
    }

    /**
     * Caso de uso "Alta de Curso". Mismas validaciones que tenía
     * ControllerV1, solo que ahora divididas: cada "¿existe esto?" es una
     * lectura rápida a través del repository correspondiente.
     */
    public void registrar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas) throws Exception {

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

        cursoRepository.guardar(nombre, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, nicknameDocente, previas);
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
                previas
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
            resultado.add(new DTCursoResumen(c.getNombreC(), c.getDescripcion()));
        }
        return resultado;
    }
}