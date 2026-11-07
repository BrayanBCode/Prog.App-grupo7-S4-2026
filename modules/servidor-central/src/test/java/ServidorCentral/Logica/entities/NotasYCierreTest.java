package ServidorCentral.Logica.entities;

import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Reglas de "Ingresar resultados de evaluaciones": nota de 1 a 12 para aceptados y cierre de la edicion. */
class NotasYCierreTest {

    private static final LocalDate F = LocalDate.of(2026, 3, 1);

    private static InscripcionEdicion inscripcion(EdicionCurso edicion, EstadoInscripcion estado) {
        InscripcionEdicion i = new InscripcionEdicion(F, edicion, null);
        i.setEstado(estado);
        edicion.getInscripciones().add(i);
        return i;
    }

    @Test
    void aceptado_seCalificaEnLaEscala() {
        EdicionCurso ed = new EdicionCurso();
        InscripcionEdicion i = inscripcion(ed, EstadoInscripcion.ACEPTADA);
        assertNull(i.getNota());

        i.calificar(1);
        assertEquals(1, i.getNota());
        i.calificar(12);
        assertEquals(12, i.getNota(), "se puede corregir mientras la edicion esta abierta");
    }

    @Test
    void notaFueraDeEscala_seRechaza() {
        InscripcionEdicion i = inscripcion(new EdicionCurso(), EstadoInscripcion.ACEPTADA);
        assertThrows(ReglaNegocioException.class, () -> i.calificar(0));
        assertThrows(ReglaNegocioException.class, () -> i.calificar(13));
        assertNull(i.getNota());
    }

    @Test
    void noAceptado_noSeCalifica() {
        EdicionCurso ed = new EdicionCurso();
        assertThrows(ReglaNegocioException.class, () -> inscripcion(ed, EstadoInscripcion.INSCRIPTO).calificar(8));
        assertThrows(ReglaNegocioException.class, () -> inscripcion(ed, EstadoInscripcion.RECHAZADA).calificar(8));
    }

    @Test
    void cerrar_exigeNotaEnTodosLosAceptados() {
        EdicionCurso ed = new EdicionCurso();
        InscripcionEdicion a = inscripcion(ed, EstadoInscripcion.ACEPTADA);
        InscripcionEdicion b = inscripcion(ed, EstadoInscripcion.ACEPTADA);
        inscripcion(ed, EstadoInscripcion.RECHAZADA);
        a.calificar(9);

        assertThrows(ReglaNegocioException.class, ed::cerrar);
        assertFalse(ed.isCerrada());

        b.calificar(4);
        ed.cerrar();
        assertTrue(ed.isCerrada());
    }

    @Test
    void edicionCerrada_noAdmiteCambios() {
        EdicionCurso ed = new EdicionCurso();
        InscripcionEdicion a = inscripcion(ed, EstadoInscripcion.ACEPTADA);
        a.calificar(7);
        ed.cerrar();

        assertThrows(ReglaNegocioException.class, () -> a.calificar(10));
        assertEquals(7, a.getNota());
        assertThrows(ReglaNegocioException.class, ed::cerrar, "no se cierra dos veces");
    }

    @Test
    void edicionSinAceptados_seCierra() {
        EdicionCurso ed = new EdicionCurso();
        assertFalse(ed.isCerrada());
        ed.cerrar();
        assertTrue(ed.isCerrada());
    }
}
