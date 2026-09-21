package Logica.services;

import Logica.entities.cursos.Curso;
import Logica.entities.cursos.EdicionCurso;
import Logica.entities.usuarios.Estudiante;
import Logica.repositories.CursoRepository;
import Logica.repositories.DocenteRepository;
import Logica.repositories.EdicionCursoRepository;
import Logica.repositories.EstudianteRepository;

import java.time.LocalDate;
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

    public String[] obtenerPorNombre(String nombreEdicion) throws Exception {
        EdicionCurso ed = edicionCursoRepository.obtenerPorNombres(nombreEdicion);

        if(ed == null) {
            throw new Exception("No se encontró la edición llamada: '" + nombreEdicion + "'");
        }

        return new String[]{
                ed.getNombre(),
                ed.getCurso().getNombreC(),
                String.valueOf(ed.getFechaInicio()),
                String.valueOf(ed.getFechaFin()),
                String.valueOf(ed.getCupo()),
                String.valueOf(ed.getFechaPublicacion())
        };
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
