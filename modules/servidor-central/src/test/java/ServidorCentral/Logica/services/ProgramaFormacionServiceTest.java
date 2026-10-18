package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTProgramaFormacion;
import ServidorCentral.Logica.datatypes.DTProgramaResumen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.programaFormacion.ProgramaFormacion;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.ProgramaFormacionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de Alta, Modificar, Consulta y Agregar Curso a Programa de Formacion (Swing).
 * Existe "EFI Robotica" con el curso MicroBit; tambien existe el curso Komikan.
 */
class ProgramaFormacionServiceTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 3, 1);
    private static final LocalDate FIN = LocalDate.of(2026, 12, 1);
    private static final LocalDate ALTA = LocalDate.of(2026, 1, 15);

    private final ProgramaFormacion efi = new ProgramaFormacion("EFI Robotica", "Robotica educativa", INICIO, FIN, ALTA);
    private final List<String> cursosDeEfi = new ArrayList<>(List.of("MicroBit"));
    /** Lo que el servicio le pidio guardar o actualizar al repositorio. */
    private final List<String> cambios = new ArrayList<>();

    private ProgramaFormacionService servicio() {
        ProgramaFormacionRepository repo = new ProgramaFormacionRepository(null) {
            @Override
            public boolean existe(String nombre) {
                return "EFI Robotica".equals(nombre);
            }

            @Override
            public ProgramaFormacion obtenerPorNombre(String nombre) {
                return existe(nombre) ? efi : null;
            }

            @Override
            public List<String> nombresCursosDelPrograma(String nombre) {
                return cursosDeEfi;
            }

            @Override
            public List<String> obtenerPorCurso(String curso) {
                return cursosDeEfi.contains(curso) ? List.of("EFI Robotica") : List.of();
            }

            @Override
            public List<String> nombres() {
                return List.of("EFI Robotica");
            }

            @Override
            public List<ProgramaFormacion> listarTodos() {
                return List.of(efi);
            }

            @Override
            public void guardar(String n, String d, LocalDate fi, LocalDate ff, LocalDate fa) {
                cambios.add("guardar " + n);
            }

            @Override
            public void actualizar(String n, String d, LocalDate fi, LocalDate ff) {
                cambios.add("actualizar " + n + " " + d);
            }

            @Override
            public void agregarCurso(String p, String c) {
                cambios.add("agregarCurso " + p + " " + c);
            }
        };
        CursoRepository cursos = new CursoRepository(null) {
            @Override
            public Curso buscarPorNombre(String nombre) {
                return List.of("MicroBit", "Komikan").contains(nombre)
                        ? new Curso(nombre, "d", 4, 40f, 4, ALTA, null, null) : null;
            }
        };
        return new ProgramaFormacionService(repo, cursos, null);
    }

    @Test
    void alta_programaNuevo_seGuarda() throws Exception {
        servicio().crear("EFI Mecanica", "d", INICIO, FIN, ALTA);
        assertEquals(List.of("guardar EFI Mecanica"), cambios);
    }

    @Test
    void alta_nombreRepetidoOFechasAlReves_falla() {
        ProgramaFormacionService s = servicio();
        assertThrows(Exception.class, () -> s.crear("EFI Robotica", "d", INICIO, FIN, ALTA));
        assertThrows(Exception.class, () -> s.crear("EFI Mecanica", "d", FIN, INICIO, ALTA));
        assertTrue(cambios.isEmpty());
    }

    @Test
    void modificar_programaExistente_seActualiza() throws Exception {
        servicio().modificar("EFI Robotica", "nueva", INICIO, FIN);
        assertEquals(List.of("actualizar EFI Robotica nueva"), cambios);
    }

    @Test
    void modificar_inexistenteOFechasAlReves_falla() {
        ProgramaFormacionService s = servicio();
        assertThrows(Exception.class, () -> s.modificar("No existe", "d", INICIO, FIN));
        assertThrows(Exception.class, () -> s.modificar("EFI Robotica", "d", FIN, INICIO));
        assertTrue(cambios.isEmpty());
    }

    @Test
    void agregarCurso_cursoNuevoEnElPrograma() throws Exception {
        servicio().agregarCurso("EFI Robotica", "Komikan");
        assertEquals(List.of("agregarCurso EFI Robotica Komikan"), cambios);
    }

    @Test
    void agregarCurso_programaOCursoInexistenteOYaAgregado_falla() {
        ProgramaFormacionService s = servicio();
        assertThrows(Exception.class, () -> s.agregarCurso("No existe", "Komikan"));
        assertThrows(Exception.class, () -> s.agregarCurso("EFI Robotica", "No existe"));
        assertThrows(Exception.class, () -> s.agregarCurso("EFI Robotica", "MicroBit"));
        assertTrue(cambios.isEmpty());
    }

    @Test
    void consulta_conSusCursos() throws Exception {
        DTProgramaFormacion d = servicio().obtenerPorNombre("EFI Robotica");
        assertEquals(new DTProgramaFormacion("EFI Robotica", "Robotica educativa", INICIO, FIN, ALTA, List.of("MicroBit")), d);
        assertEquals(d, servicio().obtenerDatosBasicos("EFI Robotica"));
    }

    @Test
    void consulta_programaInexistente() {
        assertThrows(Exception.class, () -> servicio().obtenerPorNombre("No existe"));
        assertNull(servicio().obtenerDatosBasicos("No existe"));
    }

    @Test
    void listados() {
        ProgramaFormacionService s = servicio();
        assertTrue(s.existe("EFI Robotica"));
        assertEquals(List.of("EFI Robotica"), s.listarNombres());
        assertEquals(List.of("EFI Robotica"), s.obtenerPorCurso("MicroBit"));
        assertEquals(List.of(new DTProgramaResumen("EFI Robotica", "Robotica educativa")), s.listarProgramasTabla());
    }
}
