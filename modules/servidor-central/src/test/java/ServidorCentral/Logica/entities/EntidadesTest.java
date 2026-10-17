package ServidorCentral.Logica.entities;

import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.programaFormacion.InscripcionPrograma;
import ServidorCentral.Logica.entities.programaFormacion.ProgramaFormacion;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Seguimiento;
import ServidorCentral.Logica.entities.usuarios.UsuarioID;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de las entidades: que la igualdad se base en la clave de cada una (lo necesitan las listas y combos de
 * Swing y JPA), los setters y la relacion Programa - Curso en memoria.
 */
class EntidadesTest {

    private static final LocalDate F = LocalDate.of(2026, 3, 1);

    @Test
    void usuarioID_igualSiCoincidenNicknameYCorreo() {
        UsuarioID a = new UsuarioID("roro", "roro@x.com");
        UsuarioID b = new UsuarioID();
        b.setNickname("roro");
        b.setMail("roro@x.com");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a, a);
        assertNotEquals(a, new UsuarioID("roro", "otro@x.com"));
        assertNotEquals(a, new UsuarioID("otro", "roro@x.com"));
        assertNotEquals(a, null);
        assertNotEquals(a, "roro");
        assertEquals("roro", b.getNickname());
        assertEquals("roro@x.com", b.getMail());
    }

    @Test
    void usuario_claveYDatos() {
        Estudiante roro = new Estudiante("roro", "roro@x.com", "Rodrigo", "Cotelo", F, null, "1234");
        assertEquals(new UsuarioID("roro", "roro@x.com"), roro.getID());
        assertEquals("Rodrigo", roro.getNombre());
        assertTrue(roro.getInscripciones().isEmpty());
        assertTrue(roro.getInscripcionesProg().isEmpty());

        Docente house = new Docente();
        house.setNombreU("Gregory");
        house.setImagen("usuarios/house.jpg");
        assertEquals("Gregory", house.getNombreU());
        assertEquals("usuarios/house.jpg", house.getImagen());
        assertTrue(house.getInstitutos().isEmpty());
        assertTrue(house.getEdicionesC().isEmpty());
    }

    @Test
    void seguimiento_quienSigueAQuien() {
        Seguimiento s = new Seguimiento("roro", "weiss");
        assertEquals("roro", s.getSeguidor());
        assertEquals("weiss", s.getSeguido());
        assertNull(s.getId(), "lo asigna la base");
        assertNull(new Seguimiento().getSeguidor());
    }

    @Test
    void edicion_igualdadPorNombre() {
        EdicionCurso a = new EdicionCurso("MicroBit-2026", null, F, F, 10, F);
        EdicionCurso b = new EdicionCurso();
        b.setNombre("MicroBit-2026");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new EdicionCurso("Otra", null, F, F, 10, F));
        assertNotEquals(new EdicionCurso(), a, "sin nombre no es igual a una con nombre");
        assertEquals(new EdicionCurso(), new EdicionCurso());
        assertEquals(0, new EdicionCurso().hashCode());
        assertNotEquals(a, "MicroBit-2026");
        assertTrue(a.toString().contains("MicroBit-2026"));
    }

    @Test
    void edicion_setters() {
        Curso microbit = new Curso();
        LocalDate fin = LocalDate.of(2026, 7, 1);
        EdicionCurso e = new EdicionCurso();
        e.setCurso(microbit);
        e.setFechaInicio(F);
        e.setFechaFin(fin);
        e.setCupo(20);
        e.setFechaPublicacion(F);
        e.setImagen("ediciones/e.jpg");

        assertSame(microbit, e.getCurso());
        assertEquals(F, e.getFechaInicio());
        assertEquals(fin, e.getFechaFin());
        assertEquals(20, e.getCupo());
        assertEquals(F, e.getFechaPublicacion());
        assertEquals("ediciones/e.jpg", e.getImagen());
        assertTrue(e.getDocentes().isEmpty());
    }

    @Test
    void inscripcionEdicion_igualdadPorId() {
        InscripcionEdicion a = new InscripcionEdicion();
        a.setId(7L);
        InscripcionEdicion b = new InscripcionEdicion();
        b.setId(7L);
        InscripcionEdicion c = new InscripcionEdicion();
        c.setId(8L);

        assertEquals(7L, a.getId());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(new InscripcionEdicion(), a);
        assertEquals(new InscripcionEdicion(), new InscripcionEdicion());
        assertNotEquals(a, "7");
        assertTrue(a.toString().contains("7"));
    }

    @Test
    void inscripcionPrograma_igualdadPorId() {
        InscripcionPrograma a = new InscripcionPrograma();
        a.setId(3L);
        InscripcionPrograma b = new InscripcionPrograma();
        b.setId(3L);
        InscripcionPrograma c = new InscripcionPrograma();
        c.setId(4L);

        assertEquals(3L, a.getId());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(new InscripcionPrograma(), a);
        assertEquals(new InscripcionPrograma(), new InscripcionPrograma());
        assertEquals(0, new InscripcionPrograma().hashCode());
        assertNotEquals(a, "3");
        assertTrue(a.toString().contains("3"));
    }

    @Test
    void programa_igualdadPorNombreYSetters() {
        ProgramaFormacion a = new ProgramaFormacion("EFI Robotica", "d", F, F, F);
        ProgramaFormacion b = new ProgramaFormacion();
        b.setNombre("EFI Robotica");
        b.setFechaAlta(F);
        b.setCursos(new java.util.ArrayList<>());
        b.setPInscripciones(List.of());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new ProgramaFormacion("Otro", "d", F, F, F));
        assertNotEquals(new ProgramaFormacion(), a);
        assertEquals(new ProgramaFormacion(), new ProgramaFormacion());
        assertNotEquals(a, "EFI Robotica");
        assertEquals("EFI Robotica", a.toString());
        assertEquals(F, b.getFechaAlta());
        assertTrue(b.getPInscripciones().isEmpty());
    }

    @Test
    void programa_agregarCurso_mantieneLasDosPuntas() {
        ProgramaFormacion efi = new ProgramaFormacion("EFI Robotica", "d", F, F, F);
        Curso microbit = new Curso("MicroBit", "d", 4, 40f, 4, F, "u", new Instituto("Medicina"));
        microbit.setProgramas(new java.util.ArrayList<>());

        efi.agregarCurso(microbit);

        assertEquals(List.of(microbit), efi.getCursos());
        assertEquals(List.of(efi), microbit.getProgramas());
        assertThrows(IllegalArgumentException.class, () -> efi.agregarCurso(microbit), "ya esta");
        assertThrows(IllegalArgumentException.class, () -> efi.agregarCurso(null));
    }

    @Test
    void instituto_listas() {
        Instituto medicina = new Instituto("Medicina");
        assertTrue(medicina.getDocentes().isEmpty());
        assertTrue(medicina.getCursos().isEmpty());
    }
}
