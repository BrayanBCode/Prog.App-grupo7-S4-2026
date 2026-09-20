package Logica.services;

import Logica.entities.cursos.Curso;
import Logica.entities.cursos.Instituto;
import Logica.entities.usuarios.Docente;
import Logica.repositories.CursoRepository;
import Logica.repositories.DocenteRepository;
import Logica.repositories.InstitutoRepository;

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
    public void altaCurso(String nombre, String descripcion, int duracion, float cantHoras,
                          int cantCreditos, String url, String nombreInstituto,
                          String nicknameDocente, List<String> previas) throws Exception {

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
}
