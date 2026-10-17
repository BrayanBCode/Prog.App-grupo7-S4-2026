package ServidorCentral.Logica.repositories;

import ServidorCentral.Persistencia.Conexion;

import java.time.LocalDate;
import java.util.List;

/**
 * Datos de prueba cargados en una base en memoria nueva, a traves de los mismos repositories (no hay SQL a mano).
 * <ul>
 *   <li>Instituto Medicina, con los docentes house y wilson. Estudiantes roro y weiss.</li>
 *   <li>Cursos MicroBit (de house) y Komikan (de house, con previa MicroBit).</li>
 *   <li>Ediciones de MicroBit: MicroBit-2025 (marzo a julio 2025, house) y MicroBit-2026 (marzo a julio 2026, cupo 2,
 *       house y wilson).</li>
 *   <li>Inscripciones: roro a MicroBit-2025 (01/02/2025), roro a MicroBit-2026 (10/02/2026), weiss a MicroBit-2026
 *       (15/02/2026).</li>
 *   <li>Programa EFI Robotica con el curso MicroBit; roro inscripto el 22/08/2026.</li>
 *   <li>Seguimientos: roro sigue a weiss, house sigue a roro.</li>
 * </ul>
 */
final class Escenario implements AutoCloseable {

    static final LocalDate NAC = LocalDate.of(1990, 5, 20);
    static final LocalDate INICIO_2025 = LocalDate.of(2025, 3, 1);
    static final LocalDate FIN_2025 = LocalDate.of(2025, 7, 1);
    static final LocalDate INICIO_2026 = LocalDate.of(2026, 3, 1);
    static final LocalDate FIN_2026 = LocalDate.of(2026, 7, 1);
    static final LocalDate INSC_PROGRAMA = LocalDate.of(2026, 8, 22);

    final Conexion conexion = BaseEnMemoria.nueva();
    final InstitutoRepository institutos = new InstitutoRepository(conexion);
    final UsuarioRepository usuarios = new UsuarioRepository(conexion);
    final DocenteRepository docentes = new DocenteRepository(conexion);
    final EstudianteRepository estudiantes = new EstudianteRepository(conexion);
    final CursoRepository cursos = new CursoRepository(conexion);
    final EdicionCursoRepository ediciones = new EdicionCursoRepository(conexion);
    final ProgramaFormacionRepository programas = new ProgramaFormacionRepository(conexion);
    final SeguimientoRepository seguimientos = new SeguimientoRepository(conexion);

    Escenario() {
        institutos.guardar("Medicina");
        usuarios.guardarDocente("house", "house@x.com", "Gregory", "House", NAC, null, "Medicina", "1234");
        usuarios.guardarDocente("wilson", "wilson@x.com", "James", "Wilson", NAC, null, "Medicina", "1234");
        usuarios.guardarEstudiante("roro", "roro@x.com", "Rodrigo", "Cotelo", NAC, null, "1234");
        usuarios.guardarEstudiante("weiss", "weiss@x.com", "Alicia", "Weiss", NAC, null, "1234");

        cursos.guardar("MicroBit", "Placas", 4, 40f, 4, "http://microbit", "Medicina", "house", List.of(), "cursos/MicroBit.jpg");
        cursos.guardar("Komikan", "Juego", 2, 20f, 2, "http://komikan", "Medicina", "house", List.of("MicroBit", "No existe"));

        ediciones.guardar("MicroBit-2025", "MicroBit", INICIO_2025, FIN_2025, 0, List.of("house"));
        ediciones.guardar("MicroBit-2026", "MicroBit", INICIO_2026, FIN_2026, 2, List.of("house", "wilson"), "ediciones/mb.jpg");

        ediciones.inscribir("roro", "roro@x.com", "MicroBit-2025", LocalDate.of(2025, 2, 1));
        ediciones.inscribir("roro", "roro@x.com", "MicroBit-2026", LocalDate.of(2026, 2, 10));
        ediciones.inscribir("weiss", "weiss@x.com", "MicroBit-2026", LocalDate.of(2026, 2, 15));

        programas.guardar("EFI Robotica", "Robotica educativa", INICIO_2026, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 1, 1));
        programas.agregarCurso("EFI Robotica", "MicroBit");
        programas.inscribir("roro", "roro@x.com", "EFI Robotica", INSC_PROGRAMA);

        seguimientos.guardar("roro", "weiss");
        seguimientos.guardar("house", "roro");
    }

    @Override
    public void close() {
        conexion.cerrar();
    }
}
