package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEdicionDetalle;
import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTInscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Persistencia.AlmacenImagenes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Alta de Edicion de Curso (web). Ademas de las reglas de siempre:
     * <ul>
     *   <li>D1: las fechas no pueden superponerse con otra edicion del mismo curso (asi hay como mucho una vigente).</li>
     *   <li>D3: el docente que da el alta siempre queda entre los docentes de la edicion.</li>
     * </ul>
     * cupo null o 0 = sin limite. La imagen se guarda recien cuando pasaron todas las validaciones.
     *
     * @throws ReglaNegocioException con el mensaje para el usuario si algo no es valido
     */
    public void altaEdicion(String nickDocente, String nombreEdicion, String nombreCurso, LocalDate fechaInicio,
                            LocalDate fechaFin, Integer cupo, List<String> nicknamesDocentes, DTImagen imagen) {
        String nombre = nombreEdicion == null ? "" : nombreEdicion.trim();
        if (nombre.isEmpty()) {
            throw new ReglaNegocioException("Indicá el nombre de la edición.");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new ReglaNegocioException("Indicá la fecha de inicio y la de fin.");
        }
        if (cupo != null && cupo < 0) {
            throw new ReglaNegocioException("El cupo no puede ser negativo.");
        }
        if (edicionCursoRepository.obtenerPorNombres(nombre) != null) {
            throw new ReglaNegocioException("Ya existe una edición de curso registrada con el nombre: " + nombre);
        }
        if (nombreCurso == null || cursoRepository.buscarPorNombre(nombreCurso) == null) {
            throw new ReglaNegocioException("El curso seleccionado no existe.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ReglaNegocioException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        validarSinSuperposicion(nombreCurso, fechaInicio, fechaFin);
        if (nickDocente == null || docenteRepository.buscarPorNickname(nickDocente) == null) {
            throw new ReglaNegocioException("Solo un docente puede dar de alta una edición.");
        }

        // D3: el que da el alta siempre participa (primero en la lista, sin repetidos)
        List<String> docentes = new ArrayList<>();
        docentes.add(nickDocente);
        if (nicknamesDocentes != null) {
            for (String nick : nicknamesDocentes) {
                if (nick != null && !nick.isBlank() && !docentes.contains(nick)) {
                    if (docenteRepository.buscarPorNickname(nick) == null) {
                        throw new ReglaNegocioException("El docente seleccionado no existe: " + nick);
                    }
                    docentes.add(nick);
                }
            }
        }

        String rutaImagen = AlmacenImagenes.guardar(imagen, AlmacenImagenes.EDICIONES, nombre);
        edicionCursoRepository.guardar(nombre, nombreCurso, fechaInicio, fechaFin, cupo == null ? 0 : cupo, docentes, rutaImagen);
    }

    /** Regla D1 (vale para el web y para Swing). */
    private void validarSinSuperposicion(String nombreCurso, LocalDate inicio, LocalDate fin) {
        String otra = edicionCursoRepository.edicionSuperpuesta(nombreCurso, inicio, fin);
        if (otra != null) {
            throw new ReglaNegocioException("Ya existe una edición de este curso en ese período: " + otra);
        }
    }

    /**
     * Consulta de Edicion de Curso (web, Figura 4): datos de la edicion, instituto, docentes y si es la vigente.
     *
     * @throws ReglaNegocioException si la edicion no existe
     */
    public DTEdicionDetalle consultarEdicion(String nombreEdicion) {
        return consultarEdicion(nombreEdicion, LocalDate.now());
    }

    /** Igual que el anterior, con la fecha de "hoy" como parametro (para los tests). */
    DTEdicionDetalle consultarEdicion(String nombreEdicion, LocalDate hoy) {
        EdicionCurso ed = nombreEdicion == null ? null : edicionCursoRepository.obtenerPorNombres(nombreEdicion);
        if (ed == null) {
            throw new ReglaNegocioException("No existe la edición \"" + (nombreEdicion == null ? "" : nombreEdicion) + "\".");
        }
        DTEdicionCurso datos = new DTEdicionCurso(ed.getNombre(), ed.getCurso().getNombreC(), ed.getFechaInicio(),
                ed.getFechaFin(), ed.getCupo(), ed.getFechaPublicacion(), ed.getImagen());
        String instituto = ed.getCurso().getInstituto() == null ? null : ed.getCurso().getInstituto().getNombre();
        boolean vigente = ed.getFechaInicio() != null && ed.getFechaFin() != null
                && !hoy.isBefore(ed.getFechaInicio()) && !hoy.isAfter(ed.getFechaFin());
        return new DTEdicionDetalle(datos, instituto, listarDocentes(ed.getNombre()), vigente);
    }

    public DTEdicionCurso obtenerPorNombre(String nombreEdicion) throws Exception {
        EdicionCurso ed = edicionCursoRepository.obtenerPorNombres(nombreEdicion);

        if(ed == null) {
            throw new Exception("No se encontró la edición llamada: '" + nombreEdicion + "'");
        }

        return new DTEdicionCurso(
                ed.getNombre(),
                ed.getCurso().getNombreC(),
                ed.getFechaInicio(),
                ed.getFechaFin(),
                ed.getCupo(),
                ed.getFechaPublicacion(),
                ed.getImagen()
        );
    }

    /**
     * Caso de uso "Alta de Edicion de Curso". Mismas reglas que tenia
     * ControllerV1: nombre unico, el curso debe existir, las fechas
     * tienen que ser coherentes y los docentes tienen que existir.
     */
    public void registrar(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception {
        registrar(nombreEdicion, nombreCurso, fechaInicio, fechaFin, cupo, nicknamesDocentes, null);
    }

    /** Igual que el anterior, con imagen opcional (ruta de un archivo local JPG/PNG). */
    public void registrar(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes, String imagen) throws Exception {

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

        validarSinSuperposicion(nombreCurso, fechaInicio, fechaFin);

        if (nicknamesDocentes == null || nicknamesDocentes.isEmpty()) {
            throw new Exception("Debe seleccionar al menos un docente para la edición.");
        }

        for (String nickname : nicknamesDocentes) {
            if (docenteRepository.buscarPorNickname(nickname) == null) {
                throw new Exception("El docente seleccionado no existe: " + nickname);
            }
        }

        String rutaImagen = AlmacenImagenes.guardar(imagen, AlmacenImagenes.EDICIONES, nombreEdicion);
        edicionCursoRepository.guardar(nombreEdicion, nombreCurso, fechaInicio, fechaFin, cupo, nicknamesDocentes, rutaImagen);
    }

    /** Docentes de la edicion. */
    public List<DTDocenteResumen> listarDocentes(String nombreEdicion) {
        List<DTDocenteResumen> resultado = new ArrayList<>();
        for (Docente d : edicionCursoRepository.docentesDeEdicion(nombreEdicion)) {
            resultado.add(new DTDocenteResumen(d.getNickname(), d.getNombreU(), d.getApellido()));
        }
        return resultado;
    }

    /** Estudiantes inscriptos en la edicion. */
    public List<DTEstudianteResumen> listarEstudiantes(String nombreEdicion) {
        List<DTEstudianteResumen> resultado = new ArrayList<>();
        for (Estudiante e : edicionCursoRepository.estudiantesDeEdicion(nombreEdicion)) {
            resultado.add(new DTEstudianteResumen(e.getNickname(), e.getNombreU(), e.getApellido(), e.getMail()));
        }
        return resultado;
    }

    /**
     * Caso de uso "Seleccionar estudiantes para una edicion de curso" (paso de consulta):
     * todas las inscripciones de la edicion con su estado. Solo un docente de esa edicion.
     */
    public List<DTInscripcionEdicion> listarInscripciones(String nicknameDocente, String nombreEdicion) throws Exception {
        verificarDocenteDeEdicion(nicknameDocente, nombreEdicion);
        return aDTInscripciones(edicionCursoRepository.inscripcionesDeEdicion(nombreEdicion), null);
    }

    /** Caso de uso "Listar aceptados a una edicion de curso": solo las inscripciones en estado Aceptada. */
    public List<DTInscripcionEdicion> listarAceptados(String nicknameDocente, String nombreEdicion) throws Exception {
        verificarDocenteDeEdicion(nicknameDocente, nombreEdicion);
        return aDTInscripciones(edicionCursoRepository.inscripcionesDeEdicion(nombreEdicion), EstadoInscripcion.ACEPTADA);
    }

    /**
     * Caso de uso "Seleccionar estudiantes para una edicion de curso" (guardar la seleccion).
     * Segun la Tabla 1 solo se puede pasar de Inscripto a Aceptada o Rechazada: no se puede volver
     * a Inscripto ni cambiar una inscripcion que ya fue resuelta. Si algo no es valido no se guarda nada.
     *
     * @param estados nickname del estudiante -> nuevo estado (Aceptada o Rechazada)
     */
    public void seleccionarEstudiantes(String nicknameDocente, String nombreEdicion, Map<String, EstadoInscripcion> estados) throws Exception {
        verificarDocenteDeEdicion(nicknameDocente, nombreEdicion);

        if (estados == null || estados.isEmpty()) {
            throw new Exception("No hay ninguna selección para guardar.");
        }

        Map<String, InscripcionEdicion> porNickname = new LinkedHashMap<>();
        for (InscripcionEdicion i : edicionCursoRepository.inscripcionesDeEdicion(nombreEdicion)) {
            porNickname.put(i.getEstudiante().getNickname(), i);
        }

        Map<String, EstadoInscripcion> cambios = new LinkedHashMap<>();
        for (Map.Entry<String, EstadoInscripcion> e : estados.entrySet()) {
            InscripcionEdicion insc = porNickname.get(e.getKey());
            if (insc == null) {
                throw new Exception("El estudiante " + e.getKey() + " no está inscripto en esta edición.");
            }
            EstadoInscripcion nuevo = e.getValue();
            if (nuevo != EstadoInscripcion.ACEPTADA && nuevo != EstadoInscripcion.RECHAZADA) {
                throw new Exception("El estado debe ser Aceptada o Rechazada.");
            }
            if (insc.getEstado() == nuevo) {
                continue; // ya estaba asi: no hay nada que cambiar
            }
            if (insc.getEstado() != EstadoInscripcion.INSCRIPTO) {
                throw new Exception("La inscripción de " + e.getKey() + " ya fue resuelta (" + insc.getEstado().getEtiqueta() + ").");
            }
            cambios.put(e.getKey(), nuevo);
        }

        if (!cambios.isEmpty()) {
            edicionCursoRepository.actualizarEstados(nombreEdicion, cambios);
        }
    }

    /** La edicion debe existir y el docente debe participar en ella (letra: "edicion de curso en que participa"). */
    private void verificarDocenteDeEdicion(String nicknameDocente, String nombreEdicion) throws Exception {
        EdicionCurso edicion = edicionCursoRepository.obtenerPorNombres(nombreEdicion);
        if (edicion == null) {
            throw new Exception("La edición de curso seleccionada no existe.");
        }
        for (Docente d : edicionCursoRepository.docentesDeEdicion(nombreEdicion)) {
            if (d.getNickname().equals(nicknameDocente)) {
                return;
            }
        }
        throw new Exception("El docente no participa en esta edición de curso.");
    }

    /** Convierte las inscripciones a DTO; si filtro no es null, deja solo las de ese estado. */
    private List<DTInscripcionEdicion> aDTInscripciones(List<InscripcionEdicion> lista, EstadoInscripcion filtro) {
        List<DTInscripcionEdicion> resultado = new ArrayList<>();
        for (InscripcionEdicion i : lista) {
            if (filtro != null && i.getEstado() != filtro) {
                continue;
            }
            Estudiante e = i.getEstudiante();
            resultado.add(new DTInscripcionEdicion(e.getNickname(), e.getMail(), e.getNombreU(), e.getApellido(),
                    i.getFechaInscripcion(), i.getEstado()));
        }
        return resultado;
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
