package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTMisCursos;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.DocenteRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
     * "Mis cursos y ediciones" (web, acceso rapido del docente): los cursos que dicta y las ediciones en las que
     * participa, de la mas reciente a la mas vieja, con si cada una es la vigente.
     *
     * @throws ReglaNegocioException si el usuario no es docente
     */
    public DTMisCursos misCursos(String nickname) {
        return misCursos(nickname, LocalDate.now());
    }

    /** Igual que el anterior, con la fecha de "hoy" como parametro (para los tests). */
    DTMisCursos misCursos(String nickname, LocalDate hoy) {
        if (nickname == null || !docenteRepository.existe(nickname)) {
            throw new ReglaNegocioException("Solo los docentes tienen cursos y ediciones a cargo.");
        }
        List<DTCursoResumen> cursos = new ArrayList<>();
        for (Curso c : docenteRepository.cursosDe(nickname)) {
            cursos.add(new DTCursoResumen(c.getNombreC(), c.getDescripcion(), c.getImagen()));
        }
        List<DTMisCursos.Edicion> ediciones = new ArrayList<>();
        for (EdicionCurso ed : docenteRepository.edicionesDe(nickname)) {
            DTEdicionCurso datos = new DTEdicionCurso(ed.getNombre(), ed.getCurso().getNombreC(), ed.getFechaInicio(),
                    ed.getFechaFin(), ed.getCupo(), ed.getFechaPublicacion(), ed.getImagen());
            boolean vigente = ed.getFechaInicio() != null && ed.getFechaFin() != null
                    && !hoy.isBefore(ed.getFechaInicio()) && !hoy.isAfter(ed.getFechaFin());
            ediciones.add(new DTMisCursos.Edicion(datos, vigente));
        }
        ediciones.sort(Comparator.comparing((DTMisCursos.Edicion e) -> e.edicion().fechaInicio(),
                Comparator.nullsLast(Comparator.<LocalDate>reverseOrder())));
        return new DTMisCursos(cursos, ediciones);
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