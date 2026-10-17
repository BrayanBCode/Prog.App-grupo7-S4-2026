package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTInscripcionesEstudiante;
import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaInscripto;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.EstudianteRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public List<DTEstudianteResumen> listarEstudiantesTabla() {
        return aTabla(estudianteRepository.listarTodos());
    }

    /**
     * Ediciones de curso y programas de formacion en los que esta
     * inscripto el estudiante, separados en dos listas. Si la vista los
     * quiere en una sola lista, los junta ella.
     */
    public DTInscripcionesEstudiante obtenerEdicionesYProgramas(String nickname) {
        return new DTInscripcionesEstudiante(
                estudianteRepository.nombresEdicionesInscriptas(nickname),
                estudianteRepository.nombresProgramasInscriptos(nickname)
        );
    }

    private List<DTEstudianteResumen> aTabla(List<Estudiante> lista) {
        List<DTEstudianteResumen> resultado = new ArrayList<>();
        for (Estudiante e : lista) {
            resultado.add(new DTEstudianteResumen(e.getNickname(), e.getNombreU(), e.getApellido(), e.getMail()));
        }
        return resultado;
    }

    /**
     * Todas las inscripciones a ediciones del estudiante con su estado
     * (Consulta de Usuario y Listar resultados de inscripciones). Que estados
     * mostrar (ej. Rechazada solo en el perfil propio) lo decide la presentacion.
     */
    public List<DTInscripcionEstudiante> listarInscripciones(String nickname) {
        List<DTInscripcionEstudiante> resultado = new ArrayList<>();
        for (InscripcionEdicion i : estudianteRepository.inscripcionesDe(nickname)) {
            resultado.add(new DTInscripcionEstudiante(i.getEdicionCurso().getNombre(),
                    i.getEdicionCurso().getCurso().getNombreC(), i.getFechaInscripcion(), i.getEstado()));
        }
        return resultado;
    }

    /**
     * Caso de uso "Listar resultados de inscripciones" (4.9, Estudiante): todas sus inscripciones a ediciones con
     * su estado, de la mas nueva a la mas vieja (incluidas las Rechazadas: las ve el propio estudiante).
     *
     * @throws ReglaNegocioException si el nickname no es de un estudiante
     */
    public List<DTInscripcionEstudiante> resultadosInscripciones(String nickEstudiante) {
        if (nickEstudiante == null || estudianteRepository.buscarPorNickname(nickEstudiante) == null) {
            throw new ReglaNegocioException("Solo un estudiante puede ver los resultados de sus inscripciones.");
        }
        return listarInscripciones(nickEstudiante);
    }

    /** Programas de formacion en los que se inscribio el estudiante. */
    public List<DTProgramaInscripto> listarProgramas(String nickname) {
        List<DTProgramaInscripto> resultado = new ArrayList<>();
        for (Object[] fila : estudianteRepository.programasConFecha(nickname)) {
            resultado.add(new DTProgramaInscripto((String) fila[0], (LocalDate) fila[1]));
        }
        return resultado;
    }
}
