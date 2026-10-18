package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.programaFormacion.ProgramaFormacion;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static ServidorCentral.Logica.repositories.Escenario.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de EstudianteRepository, ProgramaFormacionRepository y SeguimientoRepository contra una base en memoria
 * (datos en {@link Escenario}).
 */
class EstudianteProgramaYSeguimientoRepositoryTest {

    private final Escenario e = new Escenario();

    @AfterEach
    void cerrar() {
        e.close();
    }

    @Test
    void estudiante_buscar() {
        assertEquals("Cotelo", e.estudiantes.buscarPorNickname("roro").getApellido());
        assertNull(e.estudiantes.buscarPorNickname("house"), "house es docente");
        assertEquals("roro", e.estudiantes.buscarPorNicknameYMail("roro", "roro@x.com").getNickname());
        assertNull(e.estudiantes.buscarPorNicknameYMail("roro", "otro@x.com"));
        assertEquals(List.of("roro", "weiss"),
                e.estudiantes.listarTodos().stream().map(Estudiante::getNickname).sorted().toList());
    }

    @Test
    void estudiante_edicionesYProgramas() {
        assertEquals(List.of("MicroBit-2025", "MicroBit-2026"),
                e.estudiantes.nombresEdicionesInscriptas("roro").stream().sorted().toList());
        assertEquals(List.of("EFI Robotica"), e.estudiantes.nombresProgramasInscriptos("roro"));
        assertTrue(e.estudiantes.nombresProgramasInscriptos("weiss").isEmpty());
    }

    @Test
    void estudiante_inscripcionesDeLaMasNuevaALaMasVieja() {
        List<InscripcionEdicion> lista = e.estudiantes.inscripcionesDe("roro");
        assertEquals(List.of("MicroBit-2026", "MicroBit-2025"),
                lista.stream().map(i -> i.getEdicionCurso().getNombre()).toList());
        assertEquals("MicroBit", lista.get(0).getEdicionCurso().getCurso().getNombreC());
    }

    @Test
    void estudiante_programasConFecha() {
        List<Object[]> filas = e.estudiantes.programasConFecha("roro");
        assertEquals(1, filas.size());
        assertEquals("EFI Robotica", filas.get(0)[0]);
        assertEquals(INSC_PROGRAMA, filas.get(0)[1]);
    }

    @Test
    void programa_consultaYListados() {
        ProgramaFormacion efi = e.programas.obtenerPorNombre("EFI Robotica");
        assertEquals("Robotica educativa", efi.getDescripcion());
        assertEquals(INICIO_2026, efi.getFechaInicio());
        assertTrue(e.programas.existe("EFI Robotica"));
        assertFalse(e.programas.existe("No existe"));
        assertNull(e.programas.obtenerPorNombre("No existe"));
        assertEquals(List.of("EFI Robotica"), e.programas.nombres());
        assertEquals(List.of("EFI Robotica"), e.programas.listarTodos().stream().map(ProgramaFormacion::getNombre).toList());
        assertEquals(List.of("EFI Robotica"), e.programas.obtenerPorCurso("MicroBit"));
        assertTrue(e.programas.obtenerPorCurso("Komikan").isEmpty());
    }

    @Test
    void programa_agregarCursoYActualizar() {
        e.programas.agregarCurso("EFI Robotica", "Komikan");
        assertEquals(List.of("Komikan", "MicroBit"), e.programas.nombresCursosDelPrograma("EFI Robotica"));

        LocalDate fin = LocalDate.of(2027, 1, 1);
        e.programas.actualizar("EFI Robotica", "Nueva", INICIO_2026, fin);
        ProgramaFormacion efi = e.programas.obtenerPorNombre("EFI Robotica");
        assertEquals("Nueva", efi.getDescripcion());
        assertEquals(fin, efi.getFechaFin());
    }

    @Test
    void programa_errores() {
        assertThrows(RuntimeException.class, () -> e.programas.actualizar("No existe", "d", INICIO_2026, FIN_2026));
        assertThrows(RuntimeException.class, () -> e.programas.agregarCurso("No existe", "MicroBit"));
        assertThrows(RuntimeException.class, () -> e.programas.guardar("EFI Robotica", "d", INICIO_2026, FIN_2026, INICIO_2026));
        assertThrows(RuntimeException.class, () -> e.programas.inscribir("nadie", "n@x.com", "EFI Robotica", INICIO_2026));
    }

    @Test
    void programa_inscripciones() {
        assertTrue(e.programas.existeInscripcion("roro", "EFI Robotica"));
        assertFalse(e.programas.existeInscripcion("weiss", "EFI Robotica"));
        e.programas.inscribir("weiss", "weiss@x.com", "EFI Robotica", INSC_PROGRAMA);
        assertTrue(e.programas.existeInscripcion("weiss", "EFI Robotica"));
    }

    @Test
    void seguimientos_seguirListarYDejar() {
        assertTrue(e.seguimientos.existe("roro", "weiss"));
        assertFalse(e.seguimientos.existe("weiss", "roro"));
        assertEquals(List.of("weiss"), e.seguimientos.seguidosDe("roro"));
        assertEquals(List.of("house"), e.seguimientos.seguidoresDe("roro"));

        e.seguimientos.eliminar("roro", "weiss");
        assertFalse(e.seguimientos.existe("roro", "weiss"));
        assertTrue(e.seguimientos.seguidosDe("roro").isEmpty());
    }

    @Test
    void seguimientos_repetido_falla() {
        assertThrows(RuntimeException.class, () -> e.seguimientos.guardar("roro", "weiss"));
        assertEquals(List.of("weiss"), e.seguimientos.seguidosDe("roro"));
    }
}
