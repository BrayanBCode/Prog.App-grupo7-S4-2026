package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EdicionCursoService {
    private final EdicionCursoRepository edicionCursoRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;

    public EdicionCursoService(EdicionCursoRepository edicionCursoRepository, CursoRepository cursoRepository, DocenteRepository docenteRepository, EstudianteRepository estudianteRepository) {
        this.edicionCursoRepository = edicionCursoRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
    }

    public List<String> obtenerNombres(String nombreCurso) {
        return edicionCursoRepository.obtenerNombres(nombreCurso);
    }

    public DTEdicionCurso obtenerPorNombre(String nombreEdicion) throws Exception {
        EdicionCurso ed = edicionCursoRepository.obtenerPorNombres(nombreEdicion);

        if(ed == null) {
            throw new Exception("No se encontró la edición llamada: '" + nombreEdicion + "'");
        }

        return new DTEdicionCurso(
                ed.getNombre(),
                ed.getCurso().getNombreC(),
                ed.getFechaInicio(),
                ed.getFechaFin(),
                ed.getCupo(),
                ed.getFechaPublicacion()
        );
    }

    /**
     * Caso de uso "Alta de Edicion de Curso". Mismas reglas que tenia
     * ControllerV1: nombre unico, el curso debe existir, las fechas
     * tienen que ser coherentes y los docentes tienen que existir.
     */
    public void registrar(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception {

        // El nombre de la edicion es unico (es su @Id)
        EdicionCurso existente = edicionCursoRepository.obtenerPorNombres(nombreEdicion);
        if (existente != null) {
            throw new Exception("Ya existe una edición de curso registrada con el nombre: " + nombreEdicion);
        }

        Curso curso = cursoRepository.buscarPorNombre(nombreCurso);
        if (curso == null) {
            throw new Exception("El curso seleccionado no existe.");
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        if (nicknamesDocentes == null || nicknamesDocentes.isEmpty()) {
            throw new Exception("Debe seleccionar al menos un docente para la edición.");
        }

        for (String nickname : nicknamesDocentes) {
            if (docenteRepository.buscarPorNickname(nickname) == null) {
                throw new Exception("El docente seleccionado no existe: " + nickname);
            }
        }

        edicionCursoRepository.guardar(nombreEdicion, nombreCurso, fechaInicio, fechaFin, cupo, nicknamesDocentes);
    }

    /** Docentes de la edicion. */
    public List<DTDocenteResumen> listarDocentes(String nombreEdicion) {
        List<DTDocenteResumen> resultado = new ArrayList<>();
        for (Docente d : edicionCursoRepository.docentesDeEdicion(nombreEdicion)) {
            resultado.add(new DTDocenteResumen(d.getNickname(), d.getNombreU(), d.getApellido()));
        }
        return resultado;
    }

    /** Estudiantes inscriptos en la edicion. */
    public List<DTEstudianteResumen> listarEstudiantes(String nombreEdicion) {
        List<DTEstudianteResumen> resultado = new ArrayList<>();
        for (Estudiante e : edicionCursoRepository.estudiantesDeEdicion(nombreEdicion)) {
            resultado.add(new DTEstudianteResumen(e.getNickname(), e.getNombreU(), e.getApellido(), e.getMail()));
        }
        return resultado;
    }

    /**
     * @return el nombre de la edicion que esta en curso hoy, o null.
     */
    public String obtenerEdicionVigente(String nombreCurso) {
        return edicionCursoRepository.obtenerNombreEdicionVigente(nombreCurso, LocalDate.now());
    }

    /**
     * Caso de uso "Inscripcion a Edicion de Curso".
     * cupo == 0 se interpreta como "sin limite" (asi quedo definido en el
     * Alta de Edicion de Curso).
     */
    public void inscribirEstudiante(String nickname, String mail, String nombreEdicion, LocalDate fechaInscripcion) throws Exception {

        Estudiante estudiante = estudianteRepository.buscarPorNicknameYMail(nickname, mail);
        if (estudiante == null) {
            throw new Exception("El estudiante seleccionado no existe.");
        }

        EdicionCurso edicion = edicionCursoRepository.obtenerPorNombres(nombreEdicion);
        if (edicion == null) {
            throw new Exception("La edición de curso seleccionada no existe.");
        }

        if (edicionCursoRepository.existeInscripcion(nickname, nombreEdicion)) {
            throw new Exception("El estudiante ya está inscripto en esta edición del curso.");
        }

        if (edicion.getCupo() > 0) {
            long inscriptos = edicionCursoRepository.contarInscriptos(nombreEdicion);
            if (inscriptos >= edicion.getCupo()) {
                throw new Exception("No hay cupos disponibles para esta edición.");
            }
        }

        edicionCursoRepository.inscribir(nickname, mail, nombreEdicion, fechaInscripcion);
    }
}
