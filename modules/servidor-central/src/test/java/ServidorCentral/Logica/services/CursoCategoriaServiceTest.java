package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.entities.cursos.Categoria;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.CategoriaRepository;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos: categorias en Alta/Consulta de Curso y estado de
 * las inscripciones a ediciones (letra 6.1, 6.2 y Tabla 1).
 */
class CursoCategoriaServiceTest {

    private final Instituto instituto = new Instituto("INCO");
    private final Docente docente = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");

    /** Categorias "existentes" en este escenario. */
    private final List<String> categoriasExistentes = List.of("Social", "Educativos");
    /** Lo ultimo que se mando a guardar (nombre -> categorias). */
    private String guardadoNombre;
    private List<String> guardadoCategorias;
    private Curso cursoExistente;

    private CursoService servicio() {
        CursoRepository cursos = new CursoRepository(null) {
            @Override
            public Curso buscarPorNombre(String nombre) {
                return cursoExistente != null && cursoExistente.getNombreC().equals(nombre) ? cursoExistente : null;
            }

            @Override
            public void guardar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos,
                                String url, String nombreInstituto, String nicknameDocente,
                                List<String> previas, List<String> categorias) {
                guardadoNombre = nombre;
                guardadoCategorias = categorias;
            }
        };
        InstitutoRepository institutos = new InstitutoRepository(null) {
            @Override
            public Instituto buscarPorNombre(String nombre) {
                return "INCO".equals(nombre) ? instituto : null;
            }
        };
        DocenteRepository docentes = new DocenteRepository(null) {
            @Override
            public Docente buscarPorNickname(String nickname) {
                return "benkenobi".equals(nickname) ? docente : null;
            }
        };
        CategoriaRepository categorias = new CategoriaRepository(null) {
            @Override
            public Categoria buscarPorNombre(String nombre) {
                return categoriasExistentes.contains(nombre) ? new Categoria(nombre) : null;
            }
        };
        return new CursoService(cursos, institutos, docentes, categorias);
    }

    @Test
    void altaCursoConCategoriasExistentes_guardaLasCategorias() throws Exception {
        servicio().registrar("Robotica", "desc", 4, 60f, 8, "url", "INCO", "benkenobi", null,
                List.of("Social", "Educativos"));

        assertEquals("Robotica", guardadoNombre);
        assertEquals(List.of("Social", "Educativos"), guardadoCategorias);
    }

    @Test
    void altaCursoConCategoriaInexistente_fallaYNoGuarda() {
        Exception e = assertThrows(Exception.class, () ->
                servicio().registrar("Robotica", "desc", 4, 60f, 8, "url", "INCO", "benkenobi", null,
                        List.of("Social", "Inventada")));

        assertTrue(e.getMessage().contains("Inventada"));
        assertNull(guardadoNombre);
    }

    @Test
    void altaCursoSinCategorias_siguePermitido() throws Exception {
        servicio().registrar("Robotica", "desc", 4, 60f, 8, "url", "INCO", "benkenobi", null);

        assertEquals("Robotica", guardadoNombre);
        assertNull(guardadoCategorias);
    }

    @Test
    void altaCursoRepetido_fallaAunqueTengaCategorias() {
        cursoExistente = new Curso("Robotica", "d", 1, 1f, 1, LocalDate.now(), "u", instituto, docente);

        assertThrows(Exception.class, () ->
                servicio().registrar("Robotica", "desc", 4, 60f, 8, "url", "INCO", "benkenobi", null,
                        List.of("Social")));
        assertNull(guardadoNombre);
    }

    @Test
    void consultaCurso_devuelveSusCategorias() throws Exception {
        cursoExistente = new Curso("Robotica", "d", 1, 1f, 1, LocalDate.now(), "u", instituto, docente);
        cursoExistente.getCategorias().add(new Categoria("Social"));
        cursoExistente.getCategorias().add(new Categoria("Educativos"));

        DTCurso dt = servicio().obtenerCurso("Robotica");

        assertEquals(List.of("Social", "Educativos"), dt.categorias());
    }

    @Test
    void consultaCursoSinCategorias_devuelveListaVacia() throws Exception {
        cursoExistente = new Curso("Robotica", "d", 1, 1f, 1, LocalDate.now(), "u", instituto, docente);

        assertTrue(servicio().obtenerCurso("Robotica").categorias().isEmpty());
    }

    @Test
    void dtCursoSinCategoriasEnElConstructor_nuncaTieneNull() {
        DTCurso dt = new DTCurso("C", "d", 1, 1f, 1, "u", LocalDate.now(), "INCO", null, new ArrayList<>());
        assertNotNull(dt.categorias());
        assertTrue(dt.categorias().isEmpty());
    }

    // ---------------- Estado de las inscripciones (Tabla 1) ----------------

    @Test
    void inscripcionNuevaEmpiezaEnInscripto() {
        Estudiante est = new Estudiante("weiss", "aweiss@hotmail.com", "Adrian", "Weiss",
                LocalDate.of(1978, 12, 23), null, "abcd");
        EdicionCurso ed = new EdicionCurso("Ed 2026", null, LocalDate.now(), LocalDate.now().plusDays(30), 10, LocalDate.now());

        InscripcionEdicion insc = new InscripcionEdicion(LocalDate.now(), ed, est);

        assertEquals(EstadoInscripcion.INSCRIPTO, insc.getEstado());
    }

    @Test
    void inscripcionPuedePasarAAceptadaORechazada() {
        InscripcionEdicion insc = new InscripcionEdicion();
        insc.setEstado(EstadoInscripcion.ACEPTADA);
        assertEquals(EstadoInscripcion.ACEPTADA, insc.getEstado());
        insc.setEstado(EstadoInscripcion.RECHAZADA);
        assertEquals(EstadoInscripcion.RECHAZADA, insc.getEstado());
    }

    @Test
    void etiquetasDeEstadoSonLasDeLaLetra() {
        assertEquals("Inscripto", EstadoInscripcion.INSCRIPTO.getEtiqueta());
        assertEquals("Aceptada", EstadoInscripcion.ACEPTADA.getEtiqueta());
        assertEquals("Rechazada", EstadoInscripcion.RECHAZADA.getEtiqueta());
    }
}
