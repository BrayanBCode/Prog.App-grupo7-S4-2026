package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de ValoracionCursoRepository contra una base en memoria (datos en {@link Escenario}). */
class ValoracionCursoRepositoryTest {

    private final Escenario e = new Escenario();
    private final ValoracionCursoRepository valoraciones = new ValoracionCursoRepository(e.conexion);

    @AfterEach
    void cerrar() {
        e.close();
    }

    @Test
    void participa_soloSiFueAceptadoEnAlgunaEdicion() {
        assertFalse(valoraciones.participa("roro", "MicroBit"), "inscripto pero sin aceptar");
        assertTrue(valoraciones.cursosDondeParticipa("roro").isEmpty());

        e.ediciones.actualizarEstados("MicroBit-2025", Map.of("roro", EstadoInscripcion.ACEPTADA));
        e.ediciones.actualizarEstados("MicroBit-2026", Map.of("roro", EstadoInscripcion.ACEPTADA,
                "weiss", EstadoInscripcion.RECHAZADA));

        assertTrue(valoraciones.participa("roro", "MicroBit"));
        assertEquals(List.of("MicroBit"), valoraciones.cursosDondeParticipa("roro"), "una vez aunque este en dos ediciones");
        assertFalse(valoraciones.participa("roro", "Komikan"));
        assertFalse(valoraciones.participa("weiss", "MicroBit"), "rechazada");
    }

    @Test
    void guardarYConsultar() {
        assertNull(valoraciones.puntajeDe("roro", "MicroBit"));
        assertTrue(valoraciones.puntajesDe("MicroBit").isEmpty());

        valoraciones.guardar("roro", "MicroBit", 4, LocalDate.now());
        valoraciones.guardar("weiss", "MicroBit", 2, LocalDate.now());
        valoraciones.guardar("roro", "Komikan", 5, LocalDate.now());

        assertEquals(4, valoraciones.puntajeDe("roro", "MicroBit"));
        assertEquals(List.of(2, 4), valoraciones.puntajesDe("MicroBit").stream().sorted().toList());
        assertEquals(List.of(5), valoraciones.puntajesDe("Komikan"));
    }

    @Test
    void dosVecesElMismoCurso_laBaseLoRechaza() {
        valoraciones.guardar("roro", "MicroBit", 4, LocalDate.now());
        assertThrows(RuntimeException.class, () -> valoraciones.guardar("roro", "MicroBit", 1, LocalDate.now()));
        assertEquals(List.of(4), valoraciones.puntajesDe("MicroBit"));
    }
}
