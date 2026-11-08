package ServidorCentral.Logica.services;

import ServidorCentral.DataTypes.DTValoracionCurso;
import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Logica.repositories.ValoracionCursoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ServidorCentral.Logica.entities.cursos.ValoracionCurso.PUNTAJE_MAXIMO;
import static ServidorCentral.Logica.entities.cursos.ValoracionCurso.PUNTAJE_MINIMO;

/**
 * Reglas del caso de uso "Valorar un Curso" (Tarea 3). La valoracion es del curso: el estudiante tiene que haber
 * participado (aceptado) en alguna de sus ediciones, el puntaje va de 1 a 5 y cada estudiante valora un curso una
 * sola vez. El promedio y el resumen por puntaje se calculan sobre todas las valoraciones del curso.
 */
public class ValoracionCursoService {

    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final ValoracionCursoRepository valoracionRepository;

    public ValoracionCursoService(EstudianteRepository estudianteRepository, CursoRepository cursoRepository,
                                  ValoracionCursoRepository valoracionRepository) {
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.valoracionRepository = valoracionRepository;
    }

    /**
     * Paso 1: los cursos en los que el estudiante participa (los que puede valorar), en orden alfabetico.
     *
     * @throws ReglaNegocioException si no es un estudiante
     */
    public List<String> cursosParaValorar(String nicknameEstudiante) {
        verificarEstudiante(nicknameEstudiante);
        return valoracionRepository.cursosDondeParticipa(nicknameEstudiante);
    }

    /**
     * Paso 2: promedio, cantidad y resumen por puntaje del curso, y el puntaje del estudiante si ya lo valoro.
     * {@code nicknameEstudiante} puede ser null (por ejemplo, un visitante mirando el curso).
     *
     * @throws ReglaNegocioException si el curso no existe
     */
    public DTValoracionCurso consultarValoracion(String nombreCurso, String nicknameEstudiante) {
        verificarCurso(nombreCurso);
        return resumen(nombreCurso, nicknameEstudiante);
    }

    /**
     * Paso 3: el estudiante le asigna al curso un puntaje de 1 a 5. Solo si participa y si todavia no lo valoro.
     *
     * @return la valoracion del curso ya actualizada
     * @throws ReglaNegocioException con el mensaje para el usuario
     */
    public DTValoracionCurso valorarCurso(String nicknameEstudiante, String nombreCurso, int puntaje) {
        verificarEstudiante(nicknameEstudiante);
        verificarCurso(nombreCurso);
        if (puntaje < PUNTAJE_MINIMO || puntaje > PUNTAJE_MAXIMO) {
            throw new ReglaNegocioException("El puntaje debe ser de " + PUNTAJE_MINIMO + " a " + PUNTAJE_MAXIMO + " estrellas.");
        }
        if (!valoracionRepository.participa(nicknameEstudiante, nombreCurso)) {
            throw new ReglaNegocioException("Solo podés valorar cursos en los que participaste.");
        }
        if (valoracionRepository.puntajeDe(nicknameEstudiante, nombreCurso) != null) {
            throw new ReglaNegocioException("Ya valoraste este curso: cada curso se valora una sola vez.");
        }
        try {
            valoracionRepository.guardar(nicknameEstudiante, nombreCurso, puntaje, LocalDate.now());
        } catch (RuntimeException e) {
            // dos pedidos casi a la vez: el segundo choca con la restriccion unica de la base
            if (valoracionRepository.puntajeDe(nicknameEstudiante, nombreCurso) != null) {
                throw new ReglaNegocioException("Ya valoraste este curso: cada curso se valora una sola vez.");
            }
            throw e;
        }
        return resumen(nombreCurso, nicknameEstudiante);
    }

    private DTValoracionCurso resumen(String nombreCurso, String nicknameEstudiante) {
        List<Integer> puntajes = valoracionRepository.puntajesDe(nombreCurso);
        List<Integer> porPuntaje = new ArrayList<>(List.of(0, 0, 0, 0, 0));
        int suma = 0;
        for (int p : puntajes) {
            porPuntaje.set(p - PUNTAJE_MINIMO, porPuntaje.get(p - PUNTAJE_MINIMO) + 1);
            suma += p;
        }
        double promedio = puntajes.isEmpty() ? 0 : (double) suma / puntajes.size();
        Integer mio = nicknameEstudiante == null ? null : valoracionRepository.puntajeDe(nicknameEstudiante, nombreCurso);
        return new DTValoracionCurso(nombreCurso, promedio, puntajes.size(), porPuntaje, mio);
    }

    private void verificarEstudiante(String nickname) {
        if (nickname == null || estudianteRepository.buscarPorNickname(nickname) == null) {
            throw new ReglaNegocioException("Solo un estudiante puede valorar cursos.");
        }
    }

    private void verificarCurso(String nombreCurso) {
        if (nombreCurso == null || cursoRepository.buscarPorNombre(nombreCurso) == null) {
            throw new ReglaNegocioException("No existe el curso \"" + (nombreCurso == null ? "" : nombreCurso) + "\".");
        }
    }
}
