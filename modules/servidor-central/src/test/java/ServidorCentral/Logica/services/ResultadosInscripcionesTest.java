package ServidorCentral.Logica.services;

import ServidorCentral.DataTypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de "Listar resultados de inscripciones" (4.9). roro tiene tres inscripciones, una en cada estado;
 * nuevo es estudiante pero no se inscribio a nada; house no es estudiante.
 */
class ResultadosInscripcionesTest {

    private static final LocalDate NAC = LocalDate.of(1999, 1, 1);

    private final Estudiante roro = new Estudiante("roro", "roro@x.com", "Rodrigo", "Cotelo", NAC, null, "1234");
    private final Estudiante nuevo = new Estudiante("nuevo", "nuevo@x.com", "Nuevo", "Estudiante", NAC, null, "1234");

    private EstudianteService servicio() {
        Curso microbit = new Curso("MicroBit", "d", 4, 40f, 4, NAC, null, null);
        InscripcionEdicion inscripto = inscripcion("MicroBit-2026", microbit, LocalDate.of(2026, 8, 22), EstadoInscripcion.INSCRIPTO);
        InscripcionEdicion aceptada = inscripcion("MicroBit-2025", microbit, LocalDate.of(2025, 2, 10), EstadoInscripcion.ACEPTADA);
        InscripcionEdicion rechazada = inscripcion("MicroBit-2024", microbit, LocalDate.of(2024, 2, 10), EstadoInscripcion.RECHAZADA);

        return new EstudianteService(new EstudianteRepository(null) {
            @Override
            public Estudiante buscarPorNickname(String nickname) {
                return "roro".equals(nickname) ? roro : "nuevo".equals(nickname) ? nuevo : null;
            }

            @Override
            public List<InscripcionEdicion> inscripcionesDe(String nickname) {
                // el repositorio ya las devuelve de la mas nueva a la mas vieja
                return "roro".equals(nickname) ? List.of(inscripto, aceptada, rechazada) : List.of();
            }
        });
    }

    private InscripcionEdicion inscripcion(String edicion, Curso curso, LocalDate fecha, EstadoInscripcion estado) {
        InscripcionEdicion i = new InscripcionEdicion(fecha, new EdicionCurso(edicion, curso, null, null, 20, null), roro);
        i.setEstado(estado);
        return i;
    }

    @Test
    void estudianteConVariasInscripciones_veTodasConSuEstado() {
        assertEquals(List.of(
                new DTInscripcionEstudiante("MicroBit-2026", "MicroBit", LocalDate.of(2026, 8, 22), EstadoInscripcion.INSCRIPTO, false, null),
                new DTInscripcionEstudiante("MicroBit-2025", "MicroBit", LocalDate.of(2025, 2, 10), EstadoInscripcion.ACEPTADA, false, null),
                new DTInscripcionEstudiante("MicroBit-2024", "MicroBit", LocalDate.of(2024, 2, 10), EstadoInscripcion.RECHAZADA, false, null)),
                servicio().resultadosInscripciones("roro"));
    }

    @Test
    void notaSoloConLaEdicionCerrada() {
        Curso microbit = new Curso("MicroBit", "d", 4, 40f, 4, NAC, null, null);
        InscripcionEdicion cerrada = inscripcion("MicroBit-2025", microbit, LocalDate.of(2025, 2, 10), EstadoInscripcion.ACEPTADA);
        cerrada.calificar(9);
        cerrada.getEdicionCurso().getInscripciones().add(cerrada);
        cerrada.getEdicionCurso().cerrar();
        InscripcionEdicion abierta = inscripcion("MicroBit-2026", microbit, LocalDate.of(2026, 2, 10), EstadoInscripcion.ACEPTADA);
        abierta.calificar(4);   // cargada pero la edicion sigue abierta: el estudiante todavia no la ve

        EstudianteService s = new EstudianteService(new EstudianteRepository(null) {
            @Override
            public Estudiante buscarPorNickname(String nickname) {
                return roro;
            }

            @Override
            public List<InscripcionEdicion> inscripcionesDe(String nickname) {
                return List.of(abierta, cerrada);
            }
        });

        assertEquals(List.of(
                new DTInscripcionEstudiante("MicroBit-2026", "MicroBit", LocalDate.of(2026, 2, 10), EstadoInscripcion.ACEPTADA, false, null),
                new DTInscripcionEstudiante("MicroBit-2025", "MicroBit", LocalDate.of(2025, 2, 10), EstadoInscripcion.ACEPTADA, true, 9)),
                s.resultadosInscripciones("roro"));
    }

    @Test
    void estudianteSinInscripciones_listaVacia() {
        assertTrue(servicio().resultadosInscripciones("nuevo").isEmpty());
    }

    @Test
    void quienNoEsEstudiante_noPuede() {
        EstudianteService s = servicio();
        for (String nick : new String[]{"house", "no-existe", null}) {
            ReglaNegocioException e = assertThrows(ReglaNegocioException.class, () -> s.resultadosInscripciones(nick));
            assertEquals("Solo un estudiante puede ver los resultados de sus inscripciones.", e.getMessage());
        }
    }
}
