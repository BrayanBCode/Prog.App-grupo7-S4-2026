package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTEstudianteResumen;
import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTInscripcionesEstudiante;
import ServidorCentral.Logica.datatypes.DTProgramaInscripto;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de las consultas del estudiante (Consulta de Usuario, Listar resultados de
 * inscripciones y las tablas de Swing). roro esta inscripto a MicroBit-2026 (Inscripto) y a Komikan - 2026
 * (Aceptada), y al programa EFI Robotica.
 */
class EstudianteServiceTest {

    private static final LocalDate F_INSC = LocalDate.of(2026, 8, 22);

    private final Estudiante roro = new Estudiante("roro", "roro@x.com", "Rodrigo", "Cotelo", LocalDate.of(1999, 1, 1), null, "1234");

    private EstudianteService servicio() {
        Curso microbit = new Curso("MicroBit", "d", 4, 40f, 4, F_INSC, null, null);
        Curso komikan = new Curso("Komikan", "d", 4, 40f, 4, F_INSC, null, null);
        InscripcionEdicion i1 = new InscripcionEdicion(F_INSC,
                new EdicionCurso("MicroBit-2026", microbit, null, null, 20, null), roro);
        InscripcionEdicion i2 = new InscripcionEdicion(F_INSC,
                new EdicionCurso("Komikan - 2026", komikan, null, null, 20, null), roro);
        i2.setEstado(EstadoInscripcion.ACEPTADA);

        return new EstudianteService(new EstudianteRepository(null) {
            @Override
            public List<Estudiante> listarTodos() {
                return List.of(roro);
            }

            @Override
            public List<String> nombresEdicionesInscriptas(String nickname) {
                return "roro".equals(nickname) ? List.of("MicroBit-2026", "Komikan - 2026") : List.of();
            }

            @Override
            public List<String> nombresProgramasInscriptos(String nickname) {
                return "roro".equals(nickname) ? List.of("EFI Robótica") : List.of();
            }

            @Override
            public List<InscripcionEdicion> inscripcionesDe(String nickname) {
                return "roro".equals(nickname) ? List.of(i1, i2) : List.of();
            }

            @Override
            public List<Object[]> programasConFecha(String nickname) {
                return "roro".equals(nickname) ? List.<Object[]>of(new Object[]{"EFI Robótica", F_INSC}) : List.of();
            }
        });
    }

    @Test
    void tabla_conNombreApellidoYCorreo() {
        assertEquals(List.of(new DTEstudianteResumen("roro", "Rodrigo", "Cotelo", "roro@x.com")),
                servicio().listarEstudiantesTabla());
    }

    @Test
    void edicionesYProgramas_enDosListas() {
        DTInscripcionesEstudiante d = servicio().obtenerEdicionesYProgramas("roro");
        assertEquals(List.of("MicroBit-2026", "Komikan - 2026"), d.ediciones());
        assertEquals(List.of("EFI Robótica"), d.programas());
    }

    @Test
    void inscripciones_conCursoFechaYEstado() {
        assertEquals(List.of(
                new DTInscripcionEstudiante("MicroBit-2026", "MicroBit", F_INSC, EstadoInscripcion.INSCRIPTO),
                new DTInscripcionEstudiante("Komikan - 2026", "Komikan", F_INSC, EstadoInscripcion.ACEPTADA)),
                servicio().listarInscripciones("roro"));
    }

    @Test
    void programas_conFechaDeInscripcion() {
        assertEquals(List.of(new DTProgramaInscripto("EFI Robótica", F_INSC)), servicio().listarProgramas("roro"));
    }

    @Test
    void estudianteSinInscripciones_listasVacias() {
        EstudianteService s = servicio();
        assertTrue(s.listarInscripciones("weiss").isEmpty());
        assertTrue(s.listarProgramas("weiss").isEmpty());
        assertTrue(s.obtenerEdicionesYProgramas("weiss").ediciones().isEmpty());
    }
}
