package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.datatypes.DTCursoDetalle;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.datatypes.OrdenBusqueda;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import ServidorCentral.Logica.repositories.ProgramaFormacionRepository;
import ServidorCentral.Persistencia.AlmacenImagenes;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
    private final EdicionCursoRepository edicionCursoRepository;
    private final ProgramaFormacionRepository programaRepository;

    /** Sin ediciones ni programas: alcanza para el alta y la consulta basica (Swing, tests viejos). */
    public CursoService(CursoRepository cursoRepository, InstitutoRepository institutoRepository, DocenteRepository docenteRepository) {
        this(cursoRepository, institutoRepository, docenteRepository, null, null);
    }

    public CursoService(CursoRepository cursoRepository, InstitutoRepository institutoRepository, DocenteRepository docenteRepository,
                        EdicionCursoRepository edicionCursoRepository, ProgramaFormacionRepository programaRepository) {
        this.cursoRepository = cursoRepository;
        this.institutoRepository = institutoRepository;
        this.docenteRepository = docenteRepository;
        this.edicionCursoRepository = edicionCursoRepository;
        this.programaRepository = programaRepository;
    }

    /**
     * Alta de Curso (web). El docente del curso es el que da el alta (D4); el curso queda con la fecha de hoy.
     * La imagen se guarda recien cuando pasaron todas las validaciones.
     *
     * @throws ReglaNegocioException con el mensaje para el usuario si algo no es valido
     */
    public void altaCurso(String nickDocente, String nombre, String descripcion, int duracion, float cantHoras,
                          int cantCreditos, String url, String nombreInstituto, List<String> previas, DTImagen imagen) {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        if (nombreLimpio.isEmpty()) {
            throw new ReglaNegocioException("Indicá el nombre del curso.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new ReglaNegocioException("Indicá la descripción del curso.");
        }
        if (duracion <= 0 || cantHoras <= 0 || cantCreditos <= 0) {
            throw new ReglaNegocioException("La duración, la cantidad de horas y los créditos deben ser mayores que cero.");
        }
        if (cursoRepository.buscarPorNombre(nombreLimpio) != null) {
            throw new ReglaNegocioException("Ya existe un curso registrado con el nombre: " + nombreLimpio);
        }
        if (nombreInstituto == null || institutoRepository.buscarPorNombre(nombreInstituto) == null) {
            throw new ReglaNegocioException("El instituto seleccionado no existe.");
        }
        if (nickDocente == null || docenteRepository.buscarPorNickname(nickDocente) == null) {
            throw new ReglaNegocioException("Solo un docente puede dar de alta un curso.");
        }
        List<String> previasSinRepetir = previas == null ? List.of() : previas.stream().distinct().toList();
        for (String previa : previasSinRepetir) {
            if (cursoRepository.buscarPorNombre(previa) == null) {
                throw new ReglaNegocioException("La previa seleccionada no existe: " + previa);
            }
        }

        String rutaImagen = AlmacenImagenes.guardar(imagen, AlmacenImagenes.CURSOS, nombreLimpio);
        cursoRepository.guardar(nombreLimpio, descripcion.trim(), duracion, cantHoras, cantCreditos,
                url == null || url.isBlank() ? null : url.trim(), nombreInstituto, nickDocente, previasSinRepetir, rutaImagen);
    }

    /**
     * Consulta de Curso (web), paso 1: los cursos del instituto, con descripcion e imagen.
     *
     * @throws ReglaNegocioException si el instituto no existe
     */
    public List<DTCursoResumen> listarCursosInstituto(String nombreInstituto) {
        if (nombreInstituto == null || institutoRepository.buscarPorNombre(nombreInstituto) == null) {
            throw new ReglaNegocioException("El instituto seleccionado no existe.");
        }
        return aTabla(cursoRepository.cursosPorInstituto(nombreInstituto));
    }

    /**
     * Consulta de Curso (web), paso 2 (Figura 3): todos los datos del curso, sus ediciones y
     * programas, en una sola llamada.
     *
     * @throws ReglaNegocioException si el curso no existe
     */
    public DTCursoDetalle consultarCurso(String nombreCurso) {
        Curso c = nombreCurso == null ? null : cursoRepository.buscarPorNombre(nombreCurso);
        if (c == null) {
            throw new ReglaNegocioException("No existe el curso \"" + (nombreCurso == null ? "" : nombreCurso) + "\".");
        }
        List<DTEdicionCurso> ediciones = new ArrayList<>();
        for (EdicionCurso ed : edicionCursoRepository.edicionesDeCurso(c.getNombreC())) {
            ediciones.add(new DTEdicionCurso(ed.getNombre(), ed.getCurso().getNombreC(), ed.getFechaInicio(),
                    ed.getFechaFin(), ed.getCupo(), ed.getFechaPublicacion(), ed.getImagen()));
        }
        return new DTCursoDetalle(aDT(c), ediciones, programaRepository.obtenerPorCurso(c.getNombreC()));
    }

    /**
     * Caso de uso "Alta de Curso". Mismas validaciones que tenía
     * ControllerV1, solo que ahora divididas: cada "¿existe esto?" es una
     * lectura rápida a través del repository correspondiente.
     */
    public void registrar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas) throws Exception {
        registrar(nombre, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, nicknameDocente, previas, null);
    }

    /**
     * Igual que el anterior, con imagen opcional (ruta de un archivo local JPG/PNG):
     * se copia a la carpeta de imagenes y en la BD queda la ruta relativa.
     */
    public void registrar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, String nombreInstituto, String nicknameDocente, List<String> previas, String imagen) throws Exception {

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

        String rutaImagen = AlmacenImagenes.guardar(imagen, AlmacenImagenes.CURSOS, nombre);
        cursoRepository.guardar(nombre, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, nicknameDocente, previas, rutaImagen);
    }

    /**
     * Busqueda de cursos (requerimiento 7.5, cuadro del cabezal): cursos cuyo nombre o descripcion contienen el
     * texto, sin distinguir mayusculas. Texto vacio (o null) = todos. orden null = alfabetico.
     */
    public List<DTCursoResumen> buscarCursos(String texto, OrdenBusqueda orden) {
        List<Curso> cursos = new ArrayList<>(cursoRepository.buscar(texto == null ? "" : texto.trim()));
        Collator alfabetico = Collator.getInstance(Locale.forLanguageTag("es"));
        alfabetico.setStrength(Collator.PRIMARY); // ignora mayusculas y tildes
        Comparator<Curso> porNombre = Comparator.comparing(Curso::getNombreC, alfabetico);
        Comparator<Integer> anios = Comparator.naturalOrder();
        cursos.sort(switch (orden == null ? OrdenBusqueda.ALFABETICO_ASC : orden) {
            case ALFABETICO_ASC -> porNombre;
            case ALFABETICO_DESC -> porNombre.reversed();
            case ANIO_ASC -> porAnio(anios).thenComparing(porNombre);
            case ANIO_DESC -> porAnio(anios.reversed()).thenComparing(porNombre);
        });
        return aTabla(cursos);
    }

    /** Por año de registro en el sentido dado; los cursos sin fecha, al final. */
    private static Comparator<Curso> porAnio(Comparator<Integer> sentido) {
        return Comparator.comparing((Curso c) -> c.getFechaRegistro() == null ? null : c.getFechaRegistro().getYear(),
                Comparator.nullsLast(sentido));
    }

    public List<DTCursoResumen> obtenerCursosTabla(String nombreInstituto) {
        return aTabla(cursoRepository.cursosPorInstituto(nombreInstituto));
    }

    /**
     * Caso de uso "Consulta de Curso". ControllerV1 validaba que el curso
     * existiera antes de armar la respuesta -- ese chequeo se había perdido
     * acá, y sin él esto tiraba NullPointerException en vez de un mensaje
     * claro cuando el nombre no correspondía a ningún curso.
     */
    public DTCurso obtenerCurso(String nombreCurso) throws Exception {
        Curso c = cursoRepository.buscarPorNombre(nombreCurso);

        if (c == null) {
            throw new Exception("No se encontró el curso llamado: '" + nombreCurso + "'");
        }
        return aDT(c);
    }

    private static DTCurso aDT(Curso c) {
        // Los textos "(Sin previas)" / "(Sin docente asignado)" los pone la presentacion.
        List<String> previas = new ArrayList<>();
        for (Curso previa : c.getPrevias()) {
            previas.add(previa.getNombreC());
        }

        DTDocenteResumen docente = null;
        if (c.getDocente() != null) {
            docente = new DTDocenteResumen(
                    c.getDocente().getNickname(),
                    c.getDocente().getNombreU(),
                    c.getDocente().getApellido());
        }

        return new DTCurso(
                c.getNombreC(),
                c.getDescripcion(),
                c.getDuracion(),
                c.getCanthoras(),
                c.getCantCreditos(),
                c.getUrl(),
                c.getFechaRegistro(),
                c.getInstituto() != null ? c.getInstituto().getNombre() : null,
                docente,
                previas,
                c.getImagen()
        );
    }

    public List<String> listarNombres() {
        return cursoRepository.nombres();
    }


    public List<String> listarNombresPorInstituto(String nombreInstituto) {
        return cursoRepository.nombresPorInstituto(nombreInstituto);
    }

    /**
     * Cada fila de la tabla de cursos: nombre + descripcion.
     */
    private List<DTCursoResumen> aTabla(List<Curso> lista) {
        List<DTCursoResumen> resultado = new ArrayList<>();
        for (Curso c : lista) {
            resultado.add(new DTCursoResumen(c.getNombreC(), c.getDescripcion(), c.getImagen()));
        }
        return resultado;
    }
}