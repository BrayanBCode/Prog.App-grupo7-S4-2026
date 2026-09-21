package Logica.services;

import Logica.entities.cursos.Curso;
import Logica.entities.programaFormacion.ProgramaFormacion;
import Logica.repositories.CursoRepository;
import Logica.repositories.ProgramaFormacionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProgramaFormacionService {
    private final ProgramaFormacionRepository programaFormacionRepository;
    private final CursoRepository cursoRepository;

    public ProgramaFormacionService(ProgramaFormacionRepository programaFormacionRepository, CursoRepository cursoRepository) {
        this.programaFormacionRepository = programaFormacionRepository;
        this.cursoRepository = cursoRepository;
    }


    public List<String> obtenerPorCurso(String nombreCurso) {
        return programaFormacionRepository.obtenerPorCurso(nombreCurso);
    }

    public List<String> obtenerPorNombre(String nombrePrograma) throws Exception {
        ProgramaFormacion pf = programaFormacionRepository.obtenerPorNombre(nombrePrograma);

        if (pf == null) {
            throw new Exception("No existe un Programa de Formación con nombre: " + nombrePrograma);
        }

        // TODO: ESTO VA EN LA CAPA DE PRESENTACIÓN
        List<String> resultado = new ArrayList<>();
        resultado.add("Nombre: " + pf.getNombre());
        resultado.add("Descripcion: " + pf.getDescripcion());
        resultado.add("Fecha Inicio: " + pf.getFechaInicio());
        resultado.add("Fecha Fin: " + pf.getFechaFin());
        resultado.add("Fecha Alta: " + pf.getFechaAlta());
        resultado.add("Cursos");

        List<String> cursos = programaFormacionRepository.nombresCursosDelPrograma(nombrePrograma);
        if (cursos.isEmpty()) {
            resultado.add("Sin Cursos Registrados");
        } else {
            for (String nombreCurso : cursos) {
                resultado.add("- " + nombreCurso);
            }
        }
        return resultado;
    }

    /**
     * Caso de uso "Alta de Programa de Formacion". El nombre es la @Id:
     * no puede repetirse.
     */
    public void crear(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) throws Exception {
        if (programaFormacionRepository.existe(nombre)) {
            throw new Exception("Ya existe un programa de formación registrado con el nombre: " + nombre);
        }
        if (fechaFin != null && fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        programaFormacionRepository.guardar(nombre, descripcion, fechaInicio, fechaFin, fechaAlta);
    }

    /**
     * Caso de uso "Modificar Programa de Formacion". El nombre identifica
     * al programa y no se modifica.
     */
    public void modificar(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        if (!programaFormacionRepository.existe(nombre)) {
            throw new Exception("No existe un programa de formacion con nombre: " + nombre);
        }
        if (fechaFin != null && fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        programaFormacionRepository.actualizar(nombre, descripcion, fechaInicio, fechaFin);
    }

    /**
     * Caso de uso "Agregar Curso a Programa de Formacion".
     */
    public void agregarCurso(String nombrePrograma, String nombreCurso) throws Exception {
        if (!programaFormacionRepository.existe(nombrePrograma)) {
            throw new Exception("No existe un programa de formacion: " + nombrePrograma);
        }

        Curso curso = cursoRepository.buscarPorNombre(nombreCurso);
        if (curso == null) {
            throw new Exception("No existe un curso con ese nombre: " + nombreCurso);
        }

        List<String> cursosDelPrograma = programaFormacionRepository.nombresCursosDelPrograma(nombrePrograma);
        if (cursosDelPrograma.contains(nombreCurso)) {
            throw new Exception("El curso ya se encuentra en el programa de formacion seleccionado");
        }

        programaFormacionRepository.agregarCurso(nombrePrograma, nombreCurso);
    }

    public boolean existe(String nombre) {
        return programaFormacionRepository.existe(nombre);
    }

    public List<String> listarNombres() {
        return programaFormacionRepository.nombres();
    }

    public List<String[]> listarProgramasTabla() {
        return aTabla(programaFormacionRepository.listarTodos());
    }

    /**
     * @return {nombre, descripcion, fechaInicio, fechaFin, fechaAlta} o
     * null si el programa no existe.
     */
    public String[] obtenerDatosBasicos(String nombre) {
        ProgramaFormacion p = programaFormacionRepository.obtenerPorNombre(nombre);
        if (p == null) return null;
        return new String[]{
                p.getNombre(),
                p.getDescripcion(),
                p.getFechaInicio() != null ? p.getFechaInicio().toString() : "",
                p.getFechaFin() != null ? p.getFechaFin().toString() : "",
                p.getFechaAlta() != null ? p.getFechaAlta().toString() : ""
        };
    }

    /**
     * {0}=nombre, {1}=descripcion -- así queda cada fila de la tabla de programas.
     */
    private List<String[]> aTabla(List<ProgramaFormacion> lista) {
        List<String[]> resultado = new ArrayList<>();
        for (ProgramaFormacion p : lista) {
            resultado.add(new String[]{p.getNombre(), p.getDescripcion()});
        }
        return resultado;
    }
}