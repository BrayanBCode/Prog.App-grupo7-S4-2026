package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de lo que usa Swing de las ediciones: Alta de Edicion, Inscripcion a Edicion,
 * consulta y listados. Existe el curso MicroBit con la edicion "MicroBit-2026" (marzo a julio, cupo 2, docente
 * house, ya inscripto roro). Existen los estudiantes roro y weiss y los docentes house y wilson.
 */
class EdicionCursoServiceTest {

    private static final LocalDate MARZO = LocalDate.of(2026, 3, 1);
    private static final LocalDate JULIO = LocalDate.of(2026, 7, 1);
    private static final LocalDate AGOSTO = LocalDate.of(2026, 8, 1);
    private static final LocalDate DICIEMBRE = LocalDate.of(2026, 12, 1);

    private final Curso microbit = new Curso("MicroBit", "d", 4, 40f, 4, MARZO, null, null);
    private final EdicionCurso ed2026 = new EdicionCurso("MicroBit-2026", microbit, MARZO, JULIO, 2, MARZO);
    private final Docente house = new Docente("house", "house@x.com", "Gregory", "House", MARZO, null, "1234");
    private final Docente wilson = new Docente("wilson", "wilson@x.com", "James", "Wilson", MARZO, null, "1234");
    private final Estudiante roro = new Estudiante("roro", "roro@x.com", "Rodrigo", "Cotelo", MARZO, null, "1234");
    private final Estudiante weiss = new Estudiante("weiss", "weiss@x.com", "Alicia", "Weiss", MARZO, null, "1234");

    /** Inscriptos de MicroBit-2026 y altas guardadas. */
    private final List<String> inscriptos = new ArrayList<>(List.of("roro"));
    private final List<String> guardadas = new ArrayList<>();

    private EdicionCursoService servicio() {
        EdicionCursoRepository ediciones = new EdicionCursoRepository(null) {
            @Override
            public EdicionCurso obtenerPorNombres(String nombre) {
                return "MicroBit-2026".equals(nombre) ? ed2026 : null;
            }

            @Override
            public List<String> obtenerNombres(String curso) {
                return "MicroBit".equals(curso) ? List.of("MicroBit-2026") : List.of();
            }

            @Override
            public String edicionSuperpuesta(String curso, LocalDate inicio, LocalDate fin) {
                return "MicroBit".equals(curso) && !inicio.isAfter(JULIO) && !fin.isBefore(MARZO) ? "MicroBit-2026" : null;
            }

            @Override
            public String obtenerNombreEdicionVigente(String curso, LocalDate hoy) {
                return "MicroBit".equals(curso) ? "MicroBit-2026" : null;
            }

            @Override
            public List<Docente> docentesDeEdicion(String nombre) {
                return List.of(house);
            }

            @Override
            public List<Estudiante> estudiantesDeEdicion(String nombre) {
                return List.of(roro);
            }

            @Override
            public boolean existeInscripcion(String nick, String edicion) {
                return inscriptos.contains(nick);
            }

            @Override
            public long contarInscriptos(String edicion) {
                return inscriptos.size();
            }

            @Override
            public void inscribir(String nick, String mail, String edicion, LocalDate fecha) {
                inscriptos.add(nick);
            }

            @Override
            public void guardar(String n, String c, LocalDate fi, LocalDate ff, int cupo, List<String> docentes, String img) {
                guardadas.add(n + " " + c + " " + docentes + " " + img);
            }
        };
        CursoRepository cursos = new CursoRepository(null) {
            @Override
            public Curso buscarPorNombre(String nombre) {
                return "MicroBit".equals(nombre) ? microbit : null;
            }
        };
        DocenteRepository docentes = new DocenteRepository(null) {
            @Override
            public Docente buscarPorNickname(String nick) {
                return "house".equals(nick) ? house : "wilson".equals(nick) ? wilson : null;
            }
        };
        EstudianteRepository estudiantes = new EstudianteRepository(null) {
            @Override
            public Estudiante buscarPorNicknameYMail(String nick, String mail) {
                return List.of(roro, weiss).stream()
                        .filter(e -> e.getNickname().equals(nick) && e.getMail().equals(mail)).findFirst().orElse(null);
            }
        };
        return new EdicionCursoService(ediciones, cursos, docentes, estudiantes);
    }

    @Test
    void alta_edicionQueNoSeSuperpone_seGuarda() throws Exception {
        servicio().registrar("MicroBit-2026-2", "MicroBit", AGOSTO, DICIEMBRE, 0, List.of("house", "wilson"));
        assertEquals(List.of("MicroBit-2026-2 MicroBit [house, wilson] null"), guardadas, "sin imagen no se guarda ruta");
    }

    @Test
    void alta_nombreRepetidoOCursoInexistente_falla() {
        EdicionCursoService s = servicio();
        assertThrows(Exception.class, () -> s.registrar("MicroBit-2026", "MicroBit", AGOSTO, DICIEMBRE, 0, List.of("house")));
        assertThrows(Exception.class, () -> s.registrar("Nueva", "No existe", AGOSTO, DICIEMBRE, 0, List.of("house")));
        assertTrue(guardadas.isEmpty());
    }

    @Test
    void alta_fechasAlRevesOSuperpuestas_falla() {
        EdicionCursoService s = servicio();
        assertThrows(Exception.class, () -> s.registrar("Nueva", "MicroBit", DICIEMBRE, AGOSTO, 0, List.of("house")));
        assertThrows(Exception.class, () -> s.registrar("Nueva", "MicroBit", JULIO, DICIEMBRE, 0, List.of("house")),
                "D1: no puede superponerse con MicroBit-2026");
        assertTrue(guardadas.isEmpty());
    }

    @Test
    void alta_sinDocentesODocenteInexistente_falla() {
        EdicionCursoService s = servicio();
        assertThrows(Exception.class, () -> s.registrar("Nueva", "MicroBit", AGOSTO, DICIEMBRE, 0, List.of()));
        assertThrows(Exception.class, () -> s.registrar("Nueva", "MicroBit", AGOSTO, DICIEMBRE, 0, null));
        assertThrows(Exception.class, () -> s.registrar("Nueva", "MicroBit", AGOSTO, DICIEMBRE, 0, List.of("house", "nadie")));
        assertTrue(guardadas.isEmpty());
    }

    @Test
    void inscripcion_conCupoLibre_seInscribe() throws Exception {
        servicio().inscribirEstudiante("weiss", "weiss@x.com", "MicroBit-2026", MARZO);
        assertEquals(List.of("roro", "weiss"), inscriptos);
    }

    @Test
    void inscripcion_estudianteOEdicionInexistente_falla() {
        EdicionCursoService s = servicio();
        assertThrows(Exception.class, () -> s.inscribirEstudiante("weiss", "otro@x.com", "MicroBit-2026", MARZO));
        assertThrows(Exception.class, () -> s.inscribirEstudiante("weiss", "weiss@x.com", "No existe", MARZO));
        assertEquals(List.of("roro"), inscriptos);
    }

    @Test
    void inscripcion_repetidaOSinCupo_falla() throws Exception {
        EdicionCursoService s = servicio();
        assertThrows(Exception.class, () -> s.inscribirEstudiante("roro", "roro@x.com", "MicroBit-2026", MARZO));
        inscriptos.add("chechi"); // cupo 2 lleno
        assertThrows(Exception.class, () -> s.inscribirEstudiante("weiss", "weiss@x.com", "MicroBit-2026", MARZO));
        assertEquals(List.of("roro", "chechi"), inscriptos);
    }

    @Test
    void consulta_porNombre() throws Exception {
        assertEquals(new DTEdicionCurso("MicroBit-2026", "MicroBit", MARZO, JULIO, 2, MARZO, null),
                servicio().obtenerPorNombre("MicroBit-2026"));
        assertThrows(Exception.class, () -> servicio().obtenerPorNombre("No existe"));
    }

    @Test
    void listados() {
        EdicionCursoService s = servicio();
        assertEquals(List.of("MicroBit-2026"), s.obtenerNombres("MicroBit"));
        assertEquals("MicroBit-2026", s.obtenerEdicionVigente("MicroBit"));
        assertEquals(List.of(new DTDocenteResumen("house", "Gregory", "House")), s.listarDocentes("MicroBit-2026"));
        assertEquals(List.of(new DTEstudianteResumen("roro", "Rodrigo", "Cotelo", "roro@x.com")),
                s.listarEstudiantes("MicroBit-2026"));
    }
}
