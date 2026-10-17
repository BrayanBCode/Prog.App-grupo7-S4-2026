package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCursoDetalle;
import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import ServidorCentral.Logica.repositories.ProgramaFormacionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de la Consulta de Curso (web, Figura 3). Datos: INCO con dos cursos
 * (Robotica, con dos ediciones, un programa y una previa; Komikan sin ediciones) e IMERL sin cursos.
 */
class ConsultaCursoTest {

    private final Instituto inco = new Instituto("INCO");
    private final Docente docente = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");
    private final Curso komikan = new Curso("Komikan", "Juego", 2, 20f, 2, LocalDate.of(2023, 5, 1), "https://k", inco, docente);
    private final Curso robotica = new Curso("Robotica", "Taller con el robot Butia", 4, 60f, 8,
            LocalDate.of(2024, 1, 1), "https://eva", inco, docente);
    private final EdicionCurso ed2025 = new EdicionCurso("Robotica 2025", robotica,
            LocalDate.of(2025, 3, 1), LocalDate.of(2025, 7, 1), 10, LocalDate.of(2025, 1, 1));
    private final EdicionCurso ed2026 = new EdicionCurso("Robotica 2026", robotica,
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 1), 0, LocalDate.of(2026, 1, 1));

    private final Map<String, List<Curso>> cursosPorInstituto = Map.of("INCO", List.of(komikan, robotica), "IMERL", List.of());

    {
        robotica.setImagen("cursos/Robotica.jpg");
        robotica.getPrevias().add(komikan);
        ed2026.setImagen("ediciones/Robotica_2026.jpg");
    }

    private CursoService servicio() {
        CursoRepository cursos = new CursoRepository(null) {
            @Override
            public Curso buscarPorNombre(String nombre) {
                return nombre.equals("Robotica") ? robotica : nombre.equals("Komikan") ? komikan : null;
            }

            @Override
            public List<Curso> cursosPorInstituto(String nombreInstituto) {
                return cursosPorInstituto.getOrDefault(nombreInstituto, List.of());
            }
        };
        InstitutoRepository institutos = new InstitutoRepository(null) {
            @Override
            public Instituto buscarPorNombre(String nombre) {
                return cursosPorInstituto.containsKey(nombre) ? new Instituto(nombre) : null;
            }
        };
        EdicionCursoRepository ediciones = new EdicionCursoRepository(null) {
            @Override
            public List<EdicionCurso> edicionesDeCurso(String nombreCurso) {
                // el repositorio ya las devuelve de la mas reciente a la mas vieja
                return nombreCurso.equals("Robotica") ? List.of(ed2026, ed2025) : List.of();
            }
        };
        ProgramaFormacionRepository programas = new ProgramaFormacionRepository(null) {
            @Override
            public List<String> obtenerPorCurso(String nombreCurso) {
                return nombreCurso.equals("Robotica") ? List.of("EFI Robotica") : List.of();
            }
        };
        return new CursoService(cursos, institutos, null, ediciones, programas);
    }

    // ---- Paso 1: cursos del instituto ----

    @Test
    void cursosDelInstituto_conDescripcionEImagen() {
        List<DTCursoResumen> lista = servicio().listarCursosInstituto("INCO");

        assertEquals(List.of("Komikan", "Robotica"), lista.stream().map(DTCursoResumen::nombre).toList());
        assertEquals("Taller con el robot Butia", lista.get(1).descripcion());
        assertEquals("cursos/Robotica.jpg", lista.get(1).imagen());
        assertNull(lista.get(0).imagen(), "Komikan no tiene imagen");
    }

    @Test
    void institutoSinCursos_listaVacia() {
        assertTrue(servicio().listarCursosInstituto("IMERL").isEmpty());
    }

    @Test
    void institutoInexistente_falla() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> servicio().listarCursosInstituto("FING"));
        assertEquals("El instituto seleccionado no existe.", e.getMessage());
        assertThrows(ReglaNegocioException.class, () -> servicio().listarCursosInstituto(null));
    }

    // ---- Paso 2: datos del curso (Figura 3) ----

    @Test
    void cursoConEdicionesYProgramas_todoEnUnaLlamada() {
        DTCursoDetalle d = servicio().consultarCurso("Robotica");

        assertEquals("Robotica", d.curso().nombre());
        assertEquals("INCO", d.curso().instituto());
        assertEquals("https://eva", d.curso().url());
        assertEquals(4, d.curso().duracion());
        assertEquals(8, d.curso().cantCreditos());
        assertEquals("cursos/Robotica.jpg", d.curso().imagen());
        assertEquals("benkenobi", d.curso().docente().nickname());
        assertEquals(List.of("Komikan"), d.curso().previas());
        assertEquals(List.of("Robotica 2026", "Robotica 2025"), d.ediciones().stream().map(DTEdicionCurso::nombre).toList());
        assertEquals("ediciones/Robotica_2026.jpg", d.ediciones().get(0).imagen());
        assertEquals(List.of("EFI Robotica"), d.programas());
    }

    @Test
    void cursoSinEdicionesNiProgramas() {
        DTCursoDetalle d = servicio().consultarCurso("Komikan");

        assertTrue(d.ediciones().isEmpty());
        assertTrue(d.programas().isEmpty());
        assertTrue(d.curso().previas().isEmpty());
    }

    @Test
    void cursoInexistente_falla() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> servicio().consultarCurso("Cocina"));
        assertEquals("No existe el curso \"Cocina\".", e.getMessage());
        assertThrows(ReglaNegocioException.class, () -> servicio().consultarCurso(null));
    }
}
