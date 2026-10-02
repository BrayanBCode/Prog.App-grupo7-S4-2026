package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.repositories.DocenteRepository;

import java.util.ArrayList;
import java.util.List;

public class DocenteService {

    private final DocenteRepository docenteRepository;

    public DocenteService(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    public List<DTDocenteResumen> listarDocentesTabla() {
        return aTabla(docenteRepository.listarTodos());
    }

    public List<DTDocenteResumen> porInstitutoTabla(String nombreInstituto) {
        return aTabla(docenteRepository.listarPorInstituto(nombreInstituto));
    }

    /**
     * Caso de uso "Consulta de Docente": institutos, cursos, ediciones y
     * programas vinculados a ese docente.
     */
    public DTDocenteDetalle obtenerData(String nickname) {
        return new DTDocenteDetalle(
                docenteRepository.nombresInstitutos(nickname),
                docenteRepository.nombresCursos(nickname),
                docenteRepository.nombresEdiciones(nickname),
                docenteRepository.nombresProgramas(nickname)
        );
    }

    /**
     * Datos basicos del docente. El texto "Nombre Apellido (nickname)" que
     * antes armaba esta clase ahora lo arma la presentacion.
     */
    private List<DTDocenteResumen> aTabla(List<Docente> lista) {
        List<DTDocenteResumen> resultado = new ArrayList<>();
        for (Docente d : lista) {
            resultado.add(new DTDocenteResumen(d.getNickname(), d.getNombreU(), d.getApellido()));
        }
        return resultado;
    }
}