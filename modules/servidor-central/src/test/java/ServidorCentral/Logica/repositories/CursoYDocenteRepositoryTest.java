package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.usuarios.Docente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de CursoRepository y DocenteRepository contra una base en memoria (datos en {@link Escenario}). */
class CursoYDocenteRepositoryTest {

    private final Escenario e = new Escenario();

    @AfterEach
    void cerrar() {
        e.close();
    }

    @Test
    void curso_seGuardaConInstitutoDocenteImagenYFechaDeHoy() {
        Curso microbit = e.cursos.buscarPorNombre("MicroBit");
        assertEquals("Placas", microbit.getDescripcion());
        assertEquals(4, microbit.getDuracion());
        assertEquals(40f, microbit.getCanthoras());
        assertEquals(4, microbit.getCantCreditos());
        assertEquals("http://microbit", microbit.getUrl());
        assertEquals("cursos/MicroBit.jpg", microbit.getImagen());
        assertEquals(LocalDate.now(), microbit.getFechaRegistro());
        assertEquals("Medicina", microbit.getInstituto().getNombre());
        assertEquals("house", microbit.getDocente().getNickname());
        assertNull(e.cursos.buscarPorNombre("No existe"));
    }

    @Test
    void curso_previasQueExisten() {
        assertEquals(List.of("MicroBit"),
                e.cursos.buscarPorNombre("Komikan").getPrevias().stream().map(Curso::getNombreC).toList(),
                "la previa que no existe se ignora");
    }

    @Test
    void curso_conDocenteInexistente_noSeGuarda() {
        assertThrows(RuntimeException.class,
                () -> e.cursos.guardar("Otro", "d", 1, 1f, 1, "u", "Medicina", "nadie", null));
        assertNull(e.cursos.buscarPorNombre("Otro"));
    }

    @Test
    void cursos_listados() {
        assertEquals(List.of("Komikan", "MicroBit"), e.cursos.nombres());
        assertEquals(List.of("Komikan", "MicroBit"), e.cursos.nombresPorInstituto("Medicina"));
        assertEquals(List.of("Komikan", "MicroBit"),
                e.cursos.cursosPorInstituto("Medicina").stream().map(Curso::getNombreC).toList());
        assertTrue(e.cursos.nombresPorInstituto("INCO").isEmpty());
    }

    @Test
    void docente_buscarYExiste() {
        assertEquals("Wilson", e.docentes.buscarPorNickname("wilson").getApellido());
        assertNull(e.docentes.buscarPorNickname("roro"), "roro es estudiante");
        assertTrue(e.docentes.existe("house"));
        assertFalse(e.docentes.existe("roro"));
    }

    @Test
    void docente_listados() {
        assertEquals(List.of("house", "wilson"),
                e.docentes.listarTodos().stream().map(Docente::getNickname).sorted().toList());
        assertEquals(List.of("house", "wilson"),
                e.docentes.listarPorInstituto("Medicina").stream().map(Docente::getNickname).sorted().toList());
        assertTrue(e.docentes.listarPorInstituto("INCO").isEmpty());
    }

    @Test
    void docente_cursosEdicionesYProgramas() {
        assertEquals(List.of("Medicina"), e.docentes.nombresInstitutos("house"));
        assertEquals(List.of("Komikan", "MicroBit"), e.docentes.cursosDe("house").stream().map(Curso::getNombreC).toList());
        assertEquals(List.of("Komikan", "MicroBit"), e.docentes.nombresCursos("house").stream().sorted().toList());
        assertEquals(List.of("MicroBit-2025", "MicroBit-2026"), e.docentes.nombresEdiciones("house").stream().sorted().toList());
        assertEquals(List.of("MicroBit-2026"), e.docentes.nombresEdiciones("wilson"));
        List<EdicionCurso> deWilson = e.docentes.edicionesDe("wilson");
        assertEquals(1, deWilson.size());
        assertEquals("MicroBit", deWilson.get(0).getCurso().getNombreC());
        assertEquals(List.of("EFI Robotica"), e.docentes.nombresProgramas("house"));
        assertTrue(e.docentes.nombresProgramas("wilson").isEmpty());
    }
}
