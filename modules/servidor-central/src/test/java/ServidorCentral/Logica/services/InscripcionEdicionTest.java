package ServidorCentral.Logica.services;

import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de la Inscripcion a Edicion de Curso (web, 4.5) y del estado de la inscripcion
 * que ve el estudiante en la Consulta de Edicion. "Hoy" es el 01/05/2026.
 * Ediciones: "Robotica 2026" vigente (01/03 al 01/07/2026, cupo 2), "Robotica 2027" futura y
 * "Komikan 2026" vigente sin limite de cupo (cupo 0).
 */
class InscripcionEdicionTest {

    private static final LocalDate HOY = LocalDate.of(2026, 5, 1);

    private final Curso robotica = new Curso("Robotica", "d", 4, 60f, 8, LocalDate.of(2024, 1, 1), "u", new Instituto("INCO"));
    private final Map<String, EdicionCurso> ediciones = Map.of(
            "Robotica 2026", new EdicionCurso("Robotica 2026", robotica, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 1), 2, HOY),
            "Robotica 2027", new EdicionCurso("Robotica 2027", robotica, LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 2, HOY),
            "Komikan 2026", new EdicionCurso("Komikan 2026", robotica, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 1), 0, HOY));
    private final List<String> estudiantes = List.of("weiss", "chechi", "roro");

    /** Inscripciones guardadas: "nick>edicion" -> estado. */
    private final Map<String, EstadoInscripcion> inscripciones = new HashMap<>();
    /** Lo que se mando a guardar en la ultima inscripcion: nick, mail, edicion, fecha. */
    private final List<Object> guardado = new ArrayList<>();

    private EdicionCursoService servicio() {
        EdicionCursoRepository edicionRepo = new EdicionCursoRepository(null) {
            @Override
            public EdicionCurso obtenerPorNombres(String nombre) {
                return ediciones.get(nombre);
            }

            @Override
            public boolean existeInscripcion(String nick, String edicion) {
                return inscripciones.containsKey(nick + ">" + edicion);
            }

            @Override
            public EstadoInscripcion estadoInscripcion(String nick, String edicion) {
                return inscripciones.get(nick + ">" + edicion);
            }

            @Override
            public long contarInscriptos(String edicion) {
                return inscripciones.keySet().stream().filter(k -> k.endsWith(">" + edicion)).count();
            }

            @Override
            public void inscribir(String nick, String mail, String edicion, LocalDate fecha) {
                guardado.clear();
                guardado.addAll(List.of(nick, mail, edicion, fecha));
                inscripciones.put(nick + ">" + edicion, EstadoInscripcion.INSCRIPTO);
            }

            @Override
            public List<ServidorCentral.Logica.entities.usuarios.Docente> docentesDeEdicion(String nombreEdicion) {
                return List.of();
            }
        };
        EstudianteRepository estudianteRepo = new EstudianteRepository(null) {
            @Override
            public Estudiante buscarPorNickname(String nick) {
                return estudiantes.contains(nick)
                        ? new Estudiante(nick, nick + "@mail.com", "N", "A", LocalDate.of(2000, 1, 1), null, "1") : null;
            }
        };
        return new EdicionCursoService(edicionRepo, null, null, estudianteRepo);
    }

    private static String error(Runnable r) {
        return assertThrows(ReglaNegocioException.class, r::run).getMessage();
    }

    // ---- Inscripcion ----

    @Test
    void inscripcionFeliz_conFechaDeHoyYElMailDelEstudiante() {
        EdicionCursoService s = servicio();
        s.inscribirse("weiss", "Robotica 2026", HOY);

        assertEquals(List.of("weiss", "weiss@mail.com", "Robotica 2026", HOY), guardado);
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripciones.get("weiss>Robotica 2026"));
    }

    @Test
    void dobleInscripcion_falla() {
        EdicionCursoService s = servicio();
        s.inscribirse("weiss", "Robotica 2026", HOY);

        assertEquals("Ya estás inscripto en esta edición del curso.", error(() -> s.inscribirse("weiss", "Robotica 2026", HOY)));
    }

    @Test
    void sinCupo_falla() {
        EdicionCursoService s = servicio();
        s.inscribirse("weiss", "Robotica 2026", HOY);
        s.inscribirse("chechi", "Robotica 2026", HOY);

        assertEquals("No hay cupos disponibles para esta edición.", error(() -> s.inscribirse("roro", "Robotica 2026", HOY)));
    }

    @Test
    void cupoCeroEsSinLimite() {
        EdicionCursoService s = servicio();
        for (String nick : estudiantes) {
            s.inscribirse(nick, "Komikan 2026", HOY);
        }
        assertEquals(3, inscripciones.size());
    }

    @Test
    void edicionNoVigente_falla() {
        assertEquals("Solo podés inscribirte a la edición vigente del curso.",
                error(() -> servicio().inscribirse("weiss", "Robotica 2027", HOY)), "todavía no empezó");
        assertEquals("Solo podés inscribirte a la edición vigente del curso.",
                error(() -> servicio().inscribirse("weiss", "Robotica 2026", LocalDate.of(2026, 7, 2))), "ya terminó");
        assertTrue(guardado.isEmpty());
    }

    @Test
    void edicionInexistente_falla() {
        assertEquals("No existe la edición \"Cocina\".", error(() -> servicio().inscribirse("weiss", "Cocina", HOY)));
    }

    @Test
    void quienNoEsEstudiante_noPuedeInscribirse() {
        assertEquals("Solo un estudiante puede inscribirse a una edición.",
                error(() -> servicio().inscribirse("benkenobi", "Robotica 2026", HOY)));
        assertEquals("Solo un estudiante puede inscribirse a una edición.",
                error(() -> servicio().inscribirse(null, "Robotica 2026", HOY)));
    }

    // ---- Estado de la inscripcion que ve quien mira la edicion ----

    @Test
    void consultaDeEdicion_muestraElEstadoDeLaInscripcionDeQuienMira() {
        EdicionCursoService s = servicio();
        s.inscribirse("weiss", "Robotica 2026", HOY);
        inscripciones.put("chechi>Robotica 2026", EstadoInscripcion.ACEPTADA);

        assertEquals(EstadoInscripcion.INSCRIPTO, s.consultarEdicion("Robotica 2026", "weiss", HOY).miInscripcion());
        assertEquals(EstadoInscripcion.ACEPTADA, s.consultarEdicion("Robotica 2026", "chechi", HOY).miInscripcion());
        assertNull(s.consultarEdicion("Robotica 2026", "roro", HOY).miInscripcion(), "no inscripto");
        assertNull(s.consultarEdicion("Robotica 2026", null, HOY).miInscripcion(), "visitante");
    }
}
