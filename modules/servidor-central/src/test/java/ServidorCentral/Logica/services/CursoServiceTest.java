package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCurso;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos: Alta/Consulta de Curso y estado de
 * las inscripciones a ediciones (letra 6.2 y Tabla 1).
 */
class CursoServiceTest {

    private final Instituto instituto = new Instituto("INCO");
    private final Docente docente = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");

    /** Nombre del ultimo curso que se mando a guardar. */
    private String guardadoNombre;
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
                                List<String> previas, String imagen) {
                guardadoNombre = nombre;
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
        return new CursoService(cursos, institutos, docentes);
    }

    @Test
    void altaCurso_guardaElCurso() throws Exception {
        servicio().registrar("Robotica", "desc", 4, 60f, 8, "url", "INCO", "benkenobi", null);

        assertEquals("Robotica", guardadoNombre);
    }

    @Test
    void altaCursoRepetido_fallaYNoGuarda() {
        cursoExistente = new Curso("Robotica", "d", 1, 1f, 1, LocalDate.now(), "u", instituto, docente);

        assertThrows(Exception.class, () ->
                servicio().registrar("Robotica", "desc", 4, 60f, 8, "url", "INCO", "benkenobi", null));
        assertNull(guardadoNombre);
    }

    @Test
    void consultaCurso_devuelveSusDatos() throws Exception {
        cursoExistente = new Curso("Robotica", "d", 1, 1f, 1, LocalDate.now(), "u", instituto, docente);

        DTCurso dt = servicio().obtenerCurso("Robotica");

        assertEquals("Robotica", dt.nombre());
        assertEquals("INCO", dt.instituto());
        assertTrue(dt.previas().isEmpty());
    }

    @Test
    void dtCursoSinPreviasEnElConstructor_nuncaTieneNull() {
        DTCurso dt = new DTCurso("C", "d", 1, 1f, 1, "u", LocalDate.now(), "INCO", null, null);
        assertNotNull(dt.previas());
        assertTrue(dt.previas().isEmpty());
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
