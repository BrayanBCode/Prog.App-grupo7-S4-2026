package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionDetalle;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de la Consulta de Edicion de Curso (web, Figura 4).
 * Datos: "Robotica 2026" (INCO) del 01/03/2026 al 01/07/2026, con dos docentes.
 */
class ConsultaEdicionTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 3, 1);
    private static final LocalDate FIN = LocalDate.of(2026, 7, 1);

    private final Curso robotica = new Curso("Robotica", "d", 4, 60f, 8, LocalDate.of(2024, 1, 1), "u", new Instituto("INCO"));
    private final EdicionCurso ed2026 = new EdicionCurso("Robotica 2026", robotica, INICIO, FIN, 20, LocalDate.of(2026, 1, 15));
    private final List<Docente> docentes = List.of(
            new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi", LocalDate.of(1914, 4, 2), null, "1"),
            new Docente("waston", "e.watson@gmail.com", "Emma", "Watson", LocalDate.of(1990, 4, 15), null, "1"));

    {
        ed2026.setImagen("ediciones/Robotica_2026.jpg");
    }

    private EdicionCursoService servicio() {
        EdicionCursoRepository repo = new EdicionCursoRepository(null) {
            @Override
            public EdicionCurso obtenerPorNombres(String nombreEdicion) {
                return ed2026.getNombre().equals(nombreEdicion) ? ed2026 : null;
            }

            @Override
            public List<Docente> docentesDeEdicion(String nombreEdicion) {
                return ed2026.getNombre().equals(nombreEdicion) ? docentes : List.of();
            }
        };
        return new EdicionCursoService(repo, null, null, null);
    }

    @Test
    void edicionConTodosSusDatosYDocentes() {
        DTEdicionDetalle d = servicio().consultarEdicion("Robotica 2026", null, LocalDate.of(2026, 5, 1));

        assertEquals("Robotica 2026", d.edicion().nombre());
        assertEquals("Robotica", d.edicion().nombreCurso());
        assertEquals("INCO", d.instituto());
        assertEquals(INICIO, d.edicion().fechaInicio());
        assertEquals(FIN, d.edicion().fechaFin());
        assertEquals(20, d.edicion().cupo());
        assertEquals(LocalDate.of(2026, 1, 15), d.edicion().fechaPublicacion());
        assertEquals("ediciones/Robotica_2026.jpg", d.edicion().imagen());
        assertEquals(List.of("benkenobi", "waston"), d.docentes().stream().map(DTDocenteResumen::nickname).toList());
        assertEquals("Emma", d.docentes().get(1).nombre());
    }

    @Test
    void vigenteEntreInicioYFin_incluidosLosBordes() {
        EdicionCursoService s = servicio();

        assertTrue(s.consultarEdicion("Robotica 2026", null, LocalDate.of(2026, 5, 1)).vigente());
        assertTrue(s.consultarEdicion("Robotica 2026", null, INICIO).vigente(), "el primer dia ya es vigente");
        assertTrue(s.consultarEdicion("Robotica 2026", null, FIN).vigente(), "el ultimo dia todavia es vigente");
    }

    @Test
    void noVigenteAntesDeEmpezarNiDespuesDeTerminar() {
        EdicionCursoService s = servicio();

        assertFalse(s.consultarEdicion("Robotica 2026", null, INICIO.minusDays(1)).vigente());
        assertFalse(s.consultarEdicion("Robotica 2026", null, FIN.plusDays(1)).vigente());
    }

    @Test
    void edicionInexistente_falla() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> servicio().consultarEdicion("Cocina 2026", null));
        assertEquals("No existe la edición \"Cocina 2026\".", e.getMessage());
        assertThrows(ReglaNegocioException.class, () -> servicio().consultarEdicion(null, null));
    }

    @Test
    void sinFechaDeHoy_usaLaFechaActual() {
        // la version sin fecha calcula "vigente" con la fecha de hoy
        DTEdicionDetalle d = servicio().consultarEdicion("Robotica 2026", null);
        assertEquals(!LocalDate.now().isBefore(INICIO) && !LocalDate.now().isAfter(FIN), d.vigente());
    }
}
