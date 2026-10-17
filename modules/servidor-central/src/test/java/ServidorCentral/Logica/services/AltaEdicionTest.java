package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import ServidorCentral.Persistencia.AlmacenImagenes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos del Alta de Edicion desde el web (4.4), con las reglas D1 (sin superposicion
 * en el mismo curso) y D3 (el docente que da el alta siempre participa).
 * Datos: cursos Robotica y Komikan; Robotica ya tiene "Robotica 2026" del 01/03/2026 al 01/07/2026;
 * docentes benkenobi y waston.
 */
class AltaEdicionTest {

    private static final LocalDate INICIO_EXISTENTE = LocalDate.of(2026, 3, 1);
    private static final LocalDate FIN_EXISTENTE = LocalDate.of(2026, 7, 1);

    private final Instituto inco = new Instituto("INCO");
    private final Map<String, Curso> cursos = Map.of(
            "Robotica", new Curso("Robotica", "d", 4, 60f, 8, LocalDate.of(2024, 1, 1), "u", inco),
            "Komikan", new Curso("Komikan", "d", 2, 20f, 2, LocalDate.of(2023, 1, 1), "u", inco));
    private final List<EdicionCurso> ediciones = List.of(
            new EdicionCurso("Robotica 2026", cursos.get("Robotica"), INICIO_EXISTENTE, FIN_EXISTENTE, 20, LocalDate.of(2026, 1, 1)));
    private final List<String> docentesExistentes = List.of("benkenobi", "waston");

    /** Lo que se mando a guardar: nombre, curso, cupo, docentes, imagen. */
    private final List<Object> guardado = new ArrayList<>();

    @TempDir
    Path carpetaImagenes;

    @BeforeEach
    void imagenesEnCarpetaTemporal() {
        AlmacenImagenes.setCarpetaBase(carpetaImagenes);
    }

    @AfterEach
    void restaurarImagenes() {
        AlmacenImagenes.setCarpetaBase(null);
    }

    private EdicionCursoService servicio() {
        EdicionCursoRepository edicionRepo = new EdicionCursoRepository(null) {
            @Override
            public EdicionCurso obtenerPorNombres(String nombre) {
                return ediciones.stream().filter(e -> e.getNombre().equals(nombre)).findFirst().orElse(null);
            }

            @Override
            public String edicionSuperpuesta(String nombreCurso, LocalDate inicio, LocalDate fin) {
                // misma condicion que el JPQL: inicioOtra <= fin y finOtra >= inicio
                return ediciones.stream()
                        .filter(e -> e.getCurso().getNombreC().equals(nombreCurso))
                        .filter(e -> !e.getFechaInicio().isAfter(fin) && !e.getFechaFin().isBefore(inicio))
                        .map(EdicionCurso::getNombre).findFirst().orElse(null);
            }

            @Override
            public void guardar(String nombre, String curso, LocalDate inicio, LocalDate fin, int cupo,
                                List<String> docentes, String imagen) {
                guardado.addAll(List.of(nombre, curso, cupo, docentes, imagen == null ? "sin imagen" : imagen));
            }
        };
        CursoRepository cursoRepo = new CursoRepository(null) {
            @Override
            public Curso buscarPorNombre(String nombre) {
                return cursos.get(nombre);
            }
        };
        DocenteRepository docenteRepo = new DocenteRepository(null) {
            @Override
            public Docente buscarPorNickname(String nick) {
                return docentesExistentes.contains(nick)
                        ? new Docente(nick, nick + "@x.com", "N", "A", LocalDate.of(1980, 1, 1), null, "1") : null;
            }
        };
        return new EdicionCursoService(edicionRepo, cursoRepo, docenteRepo, null);
    }

    /** Alta de "Robotica 2027" (del 01/03/2027 al 01/07/2027) por benkenobi. */
    private void alta(String nombre, String curso, LocalDate inicio, LocalDate fin, Integer cupo, List<String> docentes) {
        servicio().altaEdicion("benkenobi", nombre, curso, inicio, fin, cupo, docentes, null);
    }

    private void alta2027(List<String> docentes) {
        alta("Robotica 2027", "Robotica", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 30, docentes);
    }

    private static String error(Runnable r) {
        return assertThrows(ReglaNegocioException.class, r::run).getMessage();
    }

    // ---- Casos felices y D3 ----

    @Test
    void altaFeliz_conElDocenteQueDaElAltaYLosElegidos() {
        alta2027(List.of("waston"));

        assertEquals(List.of("Robotica 2027", "Robotica", 30, List.of("benkenobi", "waston"), "sin imagen"), guardado);
    }

    @Test
    void d3_elQueDaElAltaQuedaIncluidoAunqueNoSeMarque() {
        alta2027(List.of());
        assertEquals(List.of("benkenobi"), guardado.get(3), "lista vacía: queda solo el que da el alta");
    }

    @Test
    void d3_siYaVeniaMarcadoNoSeDuplica() {
        alta2027(List.of("waston", "benkenobi", "waston"));
        assertEquals(List.of("benkenobi", "waston"), guardado.get(3));
    }

    @Test
    void cupoVacioEsSinLimite() {
        alta("Robotica 2027", "Robotica", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), null, null);
        assertEquals(0, guardado.get(2));
    }

    @Test
    void conImagen_laGuardaConElNombreDeLaEdicion() {
        servicio().altaEdicion("benkenobi", "Robotica 2027", "Robotica", LocalDate.of(2027, 3, 1),
                LocalDate.of(2027, 7, 1), 10, List.of(), new DTImagen(new byte[]{1, 2}, "jpg"));

        assertEquals("ediciones/Robotica_2027.jpg", guardado.get(4));
        assertTrue(Files.isRegularFile(carpetaImagenes.resolve("ediciones/Robotica_2027.jpg")));
    }

    // ---- D1: superposicion con otra edicion del mismo curso ----

    @Test
    void d1_superpuestaConOtraDelMismoCurso_falla() {
        assertEquals("Ya existe una edición de este curso en ese período: Robotica 2026",
                error(() -> alta("Robotica 2026-B", "Robotica", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 9, 1), 10, null)));
        assertTrue(guardado.isEmpty());
    }

    @Test
    void d1_compartirUnSoloDiaYaEsSuperposicion() {
        error(() -> alta("Robotica 2026-B", "Robotica", FIN_EXISTENTE, FIN_EXISTENTE.plusMonths(2), 10, null));
        error(() -> alta("Robotica 2026-A", "Robotica", INICIO_EXISTENTE.minusMonths(2), INICIO_EXISTENTE, 10, null));
        error(() -> alta("Robotica 2026-C", "Robotica", INICIO_EXISTENTE.plusDays(10), FIN_EXISTENTE.minusDays(10), 10, null));
    }

    @Test
    void d1_contiguaSeDaDeAlta() {
        alta("Robotica 2026-B", "Robotica", FIN_EXISTENTE.plusDays(1), FIN_EXISTENTE.plusMonths(3), 10, null);
        assertEquals("Robotica 2026-B", guardado.get(0));
    }

    @Test
    void d1_mismasFechasPeroOtroCursoSeDaDeAlta() {
        alta("Komikan 2026", "Komikan", INICIO_EXISTENTE, FIN_EXISTENTE, 10, null);
        assertEquals("Komikan 2026", guardado.get(0));
    }

    @Test
    void d1_tambienSeAplicaAlAltaDesdeSwing() {
        Exception e = assertThrows(Exception.class, () -> servicio().registrar("Robotica 2026-B", "Robotica",
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 9, 1), 10, List.of("benkenobi")));
        assertEquals("Ya existe una edición de este curso en ese período: Robotica 2026", e.getMessage());
    }

    // ---- Errores de siempre ----

    @Test
    void nombreRepetido_falla() {
        assertEquals("Ya existe una edición de curso registrada con el nombre: Robotica 2026",
                error(() -> alta("Robotica 2026", "Robotica", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 10, null)));
    }

    @Test
    void cursoInexistente_falla() {
        assertEquals("El curso seleccionado no existe.",
                error(() -> alta("Cocina 2027", "Cocina", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 10, null)));
    }

    @Test
    void finAnteriorAlInicio_falla() {
        assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio.",
                error(() -> alta("Robotica 2027", "Robotica", LocalDate.of(2027, 7, 1), LocalDate.of(2027, 3, 1), 10, null)));
    }

    @Test
    void docenteInexistente_falla() {
        assertEquals("El docente seleccionado no existe: nadie", error(() -> alta2027(List.of("nadie"))));
        assertTrue(guardado.isEmpty());
    }

    @Test
    void quienNoEsDocente_noPuedeDarDeAlta() {
        assertEquals("Solo un docente puede dar de alta una edición.", error(() -> servicio().altaEdicion("weiss",
                "Robotica 2027", "Robotica", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 10, null, null)));
    }

    @Test
    void datosObligatoriosYCupoNoNegativo() {
        assertEquals("Indicá el nombre de la edición.",
                error(() -> alta(" ", "Robotica", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 10, null)));
        assertEquals("Indicá la fecha de inicio y la de fin.",
                error(() -> alta("Robotica 2027", "Robotica", null, LocalDate.of(2027, 7, 1), 10, null)));
        assertEquals("El cupo no puede ser negativo.",
                error(() -> alta("Robotica 2027", "Robotica", LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), -1, null)));
    }

    @Test
    void siFallaUnaValidacion_noSeGuardaLaImagen() {
        error(() -> servicio().altaEdicion("benkenobi", "Robotica 2026", "Robotica", LocalDate.of(2027, 3, 1),
                LocalDate.of(2027, 7, 1), 10, List.of(), new DTImagen(new byte[]{1}, "png")));
        assertFalse(Files.exists(carpetaImagenes.resolve("ediciones/Robotica_2026.png")));
    }
}
