package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ServidorCentral.Logica.repositories.Escenario.*;
import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de EdicionCursoRepository contra una base en memoria (datos en {@link Escenario}). */
class EdicionCursoRepositoryTest {

    private final Escenario e = new Escenario();

    @AfterEach
    void cerrar() {
        e.close();
    }

    @Test
    void edicion_seGuardaConSusDatos() {
        EdicionCurso ed = e.ediciones.obtenerPorNombres("MicroBit-2026");
        assertEquals("MicroBit", ed.getCurso().getNombreC());
        assertEquals(INICIO_2026, ed.getFechaInicio());
        assertEquals(FIN_2026, ed.getFechaFin());
        assertEquals(2, ed.getCupo());
        assertEquals(LocalDate.now(), ed.getFechaPublicacion());
        assertEquals("ediciones/mb.jpg", ed.getImagen());
        assertNull(e.ediciones.obtenerPorNombres("No existe"));
    }

    @Test
    void edicion_conDocenteInexistente_noSeGuarda() {
        assertThrows(RuntimeException.class, () -> e.ediciones.guardar("MicroBit-2027", "MicroBit",
                LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), 0, List.of("nadie")));
        assertNull(e.ediciones.obtenerPorNombres("MicroBit-2027"));
    }

    @Test
    void edicionesDelCurso_deLaMasNuevaALaMasVieja() {
        assertEquals(List.of("MicroBit-2025", "MicroBit-2026"), e.ediciones.obtenerNombres("MicroBit").stream().sorted().toList());
        assertEquals(List.of("MicroBit-2026", "MicroBit-2025"),
                e.ediciones.edicionesDeCurso("MicroBit").stream().map(EdicionCurso::getNombre).toList());
        assertTrue(e.ediciones.edicionesDeCurso("Komikan").isEmpty());
    }

    @Test
    void superpuestaYVigente() {
        assertEquals("MicroBit-2026", e.ediciones.edicionSuperpuesta("MicroBit", FIN_2026, LocalDate.of(2026, 9, 1)),
                "compartir un dia ya cuenta");
        assertNull(e.ediciones.edicionSuperpuesta("MicroBit", LocalDate.of(2026, 8, 1), LocalDate.of(2026, 9, 1)));
        assertEquals("MicroBit-2026", e.ediciones.obtenerNombreEdicionVigente("MicroBit", LocalDate.of(2026, 5, 1)));
        assertNull(e.ediciones.obtenerNombreEdicionVigente("MicroBit", LocalDate.of(2026, 1, 1)));
    }

    @Test
    void docentesYEstudiantesDeLaEdicion() {
        assertEquals(List.of("house", "wilson"),
                e.ediciones.docentesDeEdicion("MicroBit-2026").stream().map(Docente::getNickname).sorted().toList());
        assertEquals(List.of("roro", "weiss"),
                e.ediciones.estudiantesDeEdicion("MicroBit-2026").stream().map(Estudiante::getNickname).sorted().toList());
    }

    @Test
    void inscripciones_enOrdenDeFecha() {
        List<InscripcionEdicion> lista = e.ediciones.inscripcionesDeEdicion("MicroBit-2026");
        assertEquals(List.of("roro", "weiss"), lista.stream().map(i -> i.getEstudiante().getNickname()).toList());
        assertEquals(LocalDate.of(2026, 2, 10), lista.get(0).getFechaInscripcion());
        assertEquals(EstadoInscripcion.INSCRIPTO, lista.get(0).getEstado());
        assertEquals(2, e.ediciones.contarInscriptos("MicroBit-2026"));
        assertTrue(e.ediciones.existeInscripcion("weiss", "MicroBit-2026"));
        assertFalse(e.ediciones.existeInscripcion("weiss", "MicroBit-2025"));
    }

    @Test
    void actualizarEstados_soloLosDelMapa() {
        e.ediciones.actualizarEstados("MicroBit-2026", Map.of("roro", EstadoInscripcion.ACEPTADA));

        assertEquals(EstadoInscripcion.ACEPTADA, e.ediciones.estadoInscripcion("roro", "MicroBit-2026"));
        assertEquals(EstadoInscripcion.INSCRIPTO, e.ediciones.estadoInscripcion("weiss", "MicroBit-2026"));
        assertEquals(EstadoInscripcion.INSCRIPTO, e.ediciones.estadoInscripcion("roro", "MicroBit-2025"), "otra edicion");
        assertNull(e.ediciones.estadoInscripcion("weiss", "MicroBit-2025"));
    }

    @Test
    void inscribirEstudianteInexistente_falla() {
        assertThrows(RuntimeException.class,
                () -> e.ediciones.inscribir("nadie", "nadie@x.com", "MicroBit-2026", LocalDate.now()));
        assertEquals(2, e.ediciones.contarInscriptos("MicroBit-2026"));
    }
}
