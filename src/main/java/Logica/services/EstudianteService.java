package Logica.services;

import Logica.entities.usuarios.Estudiante;
import Logica.repositories.EstudianteRepository;

import java.util.ArrayList;
import java.util.List;

public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public List<String[]> listarEstudiantesTabla() {
        List<String[]> resultado = new ArrayList<>();
        for (Estudiante e : estudianteRepository.listarTodos()) {
            resultado.add(new String[]{e.getNickname(), e.getNombreU(), e.getApellido(), e.getMail()});
        }
        return resultado;
    }

    /**
     * Ediciones de curso + programas de formacion en los que esta
     * inscripto el estudiante, todo en una sola lista (como lo esperaba
     * la vista de consulta de usuario).
     */
    public List<String> obtenerEdicionesYProgramas(String nickname) {
        List<String> resultado = new ArrayList<>(estudianteRepository.nombresEdicionesInscriptas(nickname));
        resultado.addAll(estudianteRepository.nombresProgramasInscriptos(nickname));
        return resultado;
    }
}
