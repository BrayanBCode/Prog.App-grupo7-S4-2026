package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTInscripcionesEstudiante;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.EstudianteRepository;

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
}