package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos del Alta de Curso desde el web (4.3). Existen el instituto INCO, el docente
 * benkenobi y el curso Komikan (para usar como previa o como nombre repetido).
 */
class AltaCursoTest {

    private final Instituto inco = new Instituto("INCO");
    private final Docente docente = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");
    private final Curso komikan = new Curso("Komikan", "Juego", 2, 20f, 2, LocalDate.of(2023, 5, 1), "u", inco, docente);

    /** Lo que se mando a guardar: nombre, docente, url, previas, imagen. */
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

    private CursoService servicio() {
        CursoRepository cursos = new CursoRepository(null) {
            @Override
            public Curso buscarPorNombre(String nombre) {
                return komikan.getNombreC().equals(nombre) ? komikan : null;
            }

            @Override
            public void guardar(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos,
                                String url, String nombreInstituto, String nicknameDocente, List<String> previas, String imagen) {
                guardado.addAll(List.of(nombre, nicknameDocente, url == null ? "sin url" : url, previas,
                        imagen == null ? "sin imagen" : imagen));
            }
        };
        InstitutoRepository institutos = new InstitutoRepository(null) {
            @Override
            public Instituto buscarPorNombre(String nombre) {
                return "INCO".equals(nombre) ? inco : null;
            }
        };
        DocenteRepository docentes = new DocenteRepository(null) {
            @Override
            public Docente buscarPorNickname(String nickname) {
                return "benkenobi".equals(nickname) ? docente : null;
            }
        };
        return new CursoService(cursos, institutos, docentes);
    }

    private void alta(String nombre, int duracion, String instituto, List<String> previas, DTImagen imagen) {
        servicio().altaCurso("benkenobi", nombre, "Taller con el robot Butia", duracion, 60f, 8,
                "https://eva", instituto, previas, imagen);
    }

    private static ReglaNegocioException falla(Runnable r) {
        return assertThrows(ReglaNegocioException.class, r::run);
    }

    // ---- Casos felices ----

    @Test
    void altaFeliz_elDocenteEsElQueDaElAltaConSusPrevias() {
        alta("  Robotica  ", 4, "INCO", List.of("Komikan", "Komikan"), null);

        assertEquals(List.of("Robotica", "benkenobi", "https://eva", List.of("Komikan"), "sin imagen"), guardado,
                "nombre sin espacios, docente = el que da el alta, previas sin repetir");
    }

    @Test
    void altaSinPreviasNiUrl() {
        servicio().altaCurso("benkenobi", "Robotica", "d", 4, 60f, 8, " ", "INCO", null, null);

        assertEquals(List.of("Robotica", "benkenobi", "sin url", List.of(), "sin imagen"), guardado);
    }

    @Test
    void altaConImagen_laGuardaConElNombreDelCurso() {
        alta("Robotica", 4, "INCO", List.of(), new DTImagen(new byte[]{1, 2, 3}, "png"));

        assertEquals("cursos/Robotica.png", guardado.get(4));
        assertTrue(Files.isRegularFile(carpetaImagenes.resolve("cursos/Robotica.png")));
    }

    // ---- Errores ----

    @Test
    void nombreRepetido_falla() {
        ReglaNegocioException e = falla(() -> alta("Komikan", 4, "INCO", List.of(), null));
        assertEquals("Ya existe un curso registrado con el nombre: Komikan", e.getMessage());
        assertTrue(guardado.isEmpty());
    }

    @Test
    void institutoInexistente_falla() {
        assertEquals("El instituto seleccionado no existe.",
                falla(() -> alta("Robotica", 4, "FING", List.of(), null)).getMessage());
        assertEquals("El instituto seleccionado no existe.",
                falla(() -> alta("Robotica", 4, null, List.of(), null)).getMessage());
    }

    @Test
    void previaInexistente_falla() {
        assertEquals("La previa seleccionada no existe: Cocina",
                falla(() -> alta("Robotica", 4, "INCO", List.of("Komikan", "Cocina"), null)).getMessage());
        assertTrue(guardado.isEmpty());
    }

    @Test
    void quienNoEsDocente_noPuedeDarDeAlta() {
        ReglaNegocioException e = falla(() -> servicio().altaCurso("weiss", "Robotica", "d", 4, 60f, 8,
                "u", "INCO", List.of(), null));
        assertEquals("Solo un docente puede dar de alta un curso.", e.getMessage());
    }

    @Test
    void camposObligatoriosYNumerosPositivos() {
        assertEquals("Indicá el nombre del curso.", falla(() -> alta("  ", 4, "INCO", List.of(), null)).getMessage());
        assertEquals("Indicá la descripción del curso.", falla(() -> servicio().altaCurso("benkenobi", "Robotica", " ",
                4, 60f, 8, "u", "INCO", List.of(), null)).getMessage());
        assertEquals("La duración, la cantidad de horas y los créditos deben ser mayores que cero.",
                falla(() -> alta("Robotica", 0, "INCO", List.of(), null)).getMessage());
        assertTrue(guardado.isEmpty());
    }

    @Test
    void imagenInvalida_fallaYNoGuardaElCurso() {
        ReglaNegocioException e = falla(() -> alta("Robotica", 4, "INCO", List.of(), new DTImagen(new byte[]{1}, "gif")));
        assertEquals("Formato de imagen no soportado. Use JPG o PNG.", e.getMessage());
        assertTrue(guardado.isEmpty());
    }

    @Test
    void siFallaUnaValidacion_noSeGuardaLaImagen() {
        falla(() -> alta("Komikan", 4, "INCO", List.of(), new DTImagen(new byte[]{1, 2}, "png")));

        assertFalse(Files.exists(carpetaImagenes.resolve("cursos/Komikan.png")));
    }
}
