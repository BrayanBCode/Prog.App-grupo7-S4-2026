package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTProgramaFormacion;
import ServidorCentral.Logica.datatypes.DTProgramaResumen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.programaFormacion.ProgramaFormacion;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Logica.repositories.ProgramaFormacionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProgramaFormacionService {
    private final ProgramaFormacionRepository programaFormacionRepository;
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;

    public ProgramaFormacionService(ProgramaFormacionRepository programaFormacionRepository, CursoRepository cursoRepository, EstudianteRepository estudianteRepository) {
        this.programaFormacionRepository = programaFormacionRepository;
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Caso de uso "Inscripcion a Programa de Formacion": el estudiante y el programa tienen que
     * existir y el estudiante no puede inscribirse dos veces al mismo programa.
     */
    public void inscribirEstudiante(String nickname, String mail, String nombrePrograma, LocalDate fechaInscripcion) throws Exception {
        Estudiante estudiante = estudianteRepository.buscarPorNicknameYMail(nickname, mail);
        if (estudiante == null) {
            throw new Exception("El estudiante seleccionado no existe.");
        }
        if (!programaFormacionRepository.existe(nombrePrograma)) {
            throw new Exception("No existe un Programa de Formación con nombre: " + nombrePrograma);
        }
        if (programaFormacionRepository.existeInscripcion(nickname, nombrePrograma)) {
            throw new Exception("El estudiante ya está inscripto en este programa de formación.");
        }
        programaFormacionRepository.inscribir(nickname, mail, nombrePrograma, fechaInscripcion);
    }


    public List<String> obtenerPorCurso(String nombreCurso) {
        return programaFormacionRepository.obtenerPorCurso(nombreCurso);
    }

    /**
     * Caso de uso "Consulta de Programa de Formacion".
     *
     * @throws Exception si el programa no existe.
     */
    public DTProgramaFormacion obtenerPorNombre(String nombrePrograma) throws Exception {
        ProgramaFormacion pf = programaFormacionRepository.obtenerPorNombre(nombrePrograma);

        if (pf == null) {
            throw new Exception("No existe un Programa de Formación con nombre: " + nombrePrograma);
        }

        return aDT(pf);
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

    public List<DTProgramaResumen> listarProgramasTabla() {
        return aTabla(programaFormacionRepository.listarTodos());
    }

    /**
     * Igual que obtenerPorNombre, pero devuelve null (en vez de lanzar
     * excepcion) si el programa no existe.
     */
    public DTProgramaFormacion obtenerDatosBasicos(String nombre) {
        ProgramaFormacion p = programaFormacionRepository.obtenerPorNombre(nombre);
        if (p == null) return null;
        return aDT(p);
    }

    private DTProgramaFormacion aDT(ProgramaFormacion pf) {
        return new DTProgramaFormacion(
                pf.getNombre(),
                pf.getDescripcion(),
                pf.getFechaInicio(),
                pf.getFechaFin(),
                pf.getFechaAlta(),
                programaFormacionRepository.nombresCursosDelPrograma(pf.getNombre())
        );
    }

    /**
     * Cada fila de la tabla de programas: nombre + descripcion.
     */
    private List<DTProgramaResumen> aTabla(List<ProgramaFormacion> lista) {
        List<DTProgramaResumen> resultado = new ArrayList<>();
        for (ProgramaFormacion p : lista) {
            resultado.add(new DTProgramaResumen(p.getNombre(), p.getDescripcion()));
        }
        return resultado;
    }
}