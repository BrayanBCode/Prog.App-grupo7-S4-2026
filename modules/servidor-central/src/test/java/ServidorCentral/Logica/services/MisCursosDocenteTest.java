package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTMisCursos;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.DocenteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de "Mis cursos y ediciones" del docente (acceso rapido web). "Hoy" es el 01/05/2026.
 * benkenobi dicta Komikan y Robotica y participa en Robotica 2025 (terminada) y Robotica 2026 (vigente);
 * waston es docente pero no tiene cursos ni ediciones.
 */
class MisCursosDocenteTest {

    private static final LocalDate HOY = LocalDate.of(2026, 5, 1);

    private final Instituto inco = new Instituto("INCO");
    private final Curso komikan = new Curso("Komikan", "Juego", 2, 20f, 2, LocalDate.of(2023, 1, 1), "u", inco);
    private final Curso robotica = new Curso("Robotica", "Butia", 4, 60f, 8, LocalDate.of(2024, 1, 1), "u", inco);
    private final EdicionCurso ed2025 = new EdicionCurso("Robotica 2025", robotica,
            LocalDate.of(2025, 3, 1), LocalDate.of(2025, 7, 1), 10, LocalDate.of(2025, 1, 1));
    private final EdicionCurso ed2026 = new EdicionCurso("Robotica 2026", robotica,
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 1), 10, LocalDate.of(2026, 1, 1));

    {
        robotica.setImagen("cursos/Robotica.jpg");
    }

    private DocenteService servicio() {
        DocenteRepository repo = new DocenteRepository(null) {
            @Override
            public boolean existe(String nick) {
                return List.of("benkenobi", "waston").contains(nick);
            }

            @Override
            public List<Curso> cursosDe(String nick) {
                return nick.equals("benkenobi") ? List.of(komikan, robotica) : List.of();
            }

            @Override
            public List<EdicionCurso> edicionesDe(String nick) {
                return nick.equals("benkenobi") ? List.of(ed2025, ed2026) : List.of();
            }
        };
        return new DocenteService(repo);
    }

    @Test
    void cursosQueDictaConDescripcionEImagen() {
        DTMisCursos m = servicio().misCursos("benkenobi", HOY);

        assertEquals(List.of("Komikan", "Robotica"), m.cursos().stream().map(DTCursoResumen::nombre).toList());
        assertEquals("Butia", m.cursos().get(1).descripcion());
        assertEquals("cursos/Robotica.jpg", m.cursos().get(1).imagen());
    }

    @Test
    void edicionesDeLaMasRecienteYCualEsLaVigente() {
        DTMisCursos m = servicio().misCursos("benkenobi", HOY);

        assertEquals(List.of("Robotica 2026", "Robotica 2025"), m.ediciones().stream().map(e -> e.edicion().nombre()).toList());
        assertTrue(m.ediciones().get(0).vigente());
        assertFalse(m.ediciones().get(1).vigente());
        assertEquals("Robotica", m.ediciones().get(0).edicion().nombreCurso());
    }

    @Test
    void docenteSinCursosNiEdiciones_listasVacias() {
        DTMisCursos m = servicio().misCursos("waston", HOY);

        assertTrue(m.cursos().isEmpty());
        assertTrue(m.ediciones().isEmpty());
    }

    @Test
    void quienNoEsDocente_falla() {
        assertEquals("Solo los docentes tienen cursos y ediciones a cargo.",
                assertThrows(ReglaNegocioException.class, () -> servicio().misCursos("weiss")).getMessage());
        assertThrows(ReglaNegocioException.class, () -> servicio().misCursos(null));
    }
}
