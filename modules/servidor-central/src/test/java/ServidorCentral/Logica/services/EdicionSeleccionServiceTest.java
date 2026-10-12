package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTInscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de "Seleccionar estudiantes para una edicion de curso"
 * y "Listar aceptados a una edicion de curso" (letra 6.2, Tabla 1).
 */
class EdicionSeleccionServiceTest {

    private static final String EDICION = "Robotica 2026";

    private final Docente participa = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");
    private final Docente ajeno = new Docente("waston", "waston@gmail.com", "Emma", "Watson",
            LocalDate.of(1990, 4, 15), null, "1234");

    private final InscripcionEdicion inscWeiss = insc("weiss", "aweiss@hotmail.com", "Adrian", "Weiss", EstadoInscripcion.INSCRIPTO);
    private final InscripcionEdicion inscGarrido = insc("chechi", "cgarrido@hotmail.com", "Cecilia", "Garrido", EstadoInscripcion.ACEPTADA);
    private final InscripcionEdicion inscCotelo = insc("roro", "rcotelo@yahoo.com", "Rodrigo", "Cotelo", EstadoInscripcion.RECHAZADA);

    /** Lo que el servicio mando a guardar (null = no se guardo nada). */
    private Map<String, EstadoInscripcion> guardado;
    private boolean edicionExiste = true;

    private static InscripcionEdicion insc(String nick, String mail, String nombre, String apellido, EstadoInscripcion estado) {
        Estudiante e = new Estudiante(nick, mail, nombre, apellido, LocalDate.of(1990, 1, 1), null, "x");
        InscripcionEdicion i = new InscripcionEdicion(LocalDate.of(2026, 8, 1), null, e);
        i.setEstado(estado);
        return i;
    }

    private EdicionCursoService servicio() {
        EdicionCursoRepository repo = new EdicionCursoRepository(null) {
            @Override
            public EdicionCurso obtenerPorNombres(String nombreEdicion) {
                return edicionExiste && EDICION.equals(nombreEdicion)
                        ? new EdicionCurso(EDICION, null, LocalDate.now(), LocalDate.now().plusDays(30), 10, LocalDate.now())
                        : null;
            }

            @Override
            public List<Docente> docentesDeEdicion(String nombreEdicion) {
                return List.of(participa);
            }

            @Override
            public List<InscripcionEdicion> inscripcionesDeEdicion(String nombreEdicion) {
                return new ArrayList<>(List.of(inscWeiss, inscGarrido, inscCotelo));
            }

            @Override
            public void actualizarEstados(String nombreEdicion, Map<String, EstadoInscripcion> estados) {
                guardado = estados;
            }
        };
        return new EdicionCursoService(repo, null, null, null);
    }

    private static Map<String, EstadoInscripcion> mapa(Object... paresNickEstado) {
        Map<String, EstadoInscripcion> m = new LinkedHashMap<>();
        for (int i = 0; i < paresNickEstado.length; i += 2) {
            m.put((String) paresNickEstado[i], (EstadoInscripcion) paresNickEstado[i + 1]);
        }
        return m;
    }

    // ---------------- Listados ----------------

    @Test
    void listarInscripciones_devuelveTodasConSuEstado() throws Exception {
        List<DTInscripcionEdicion> lista = servicio().listarInscripciones("benkenobi", EDICION);

        assertEquals(3, lista.size());
        assertEquals("weiss", lista.get(0).nickname());
        assertEquals(EstadoInscripcion.INSCRIPTO, lista.get(0).estado());
        assertEquals(EstadoInscripcion.ACEPTADA, lista.get(1).estado());
        assertEquals(EstadoInscripcion.RECHAZADA, lista.get(2).estado());
        assertEquals("Adrian", lista.get(0).nombre());
        assertEquals(LocalDate.of(2026, 8, 1), lista.get(0).fechaInscripcion());
    }

    @Test
    void listarAceptados_devuelveSoloLasAceptadas() throws Exception {
        List<DTInscripcionEdicion> lista = servicio().listarAceptados("benkenobi", EDICION);

        assertEquals(1, lista.size());
        assertEquals("chechi", lista.get(0).nickname());
    }

    @Test
    void docenteQueNoParticipa_noPuedeListarNiSeleccionar() {
        assertThrows(Exception.class, () -> servicio().listarInscripciones("waston", EDICION));
        assertThrows(Exception.class, () -> servicio().listarAceptados("waston", EDICION));
        assertThrows(Exception.class, () ->
                servicio().seleccionarEstudiantes("waston", EDICION, mapa("weiss", EstadoInscripcion.ACEPTADA)));
        assertNull(guardado);
    }

    @Test
    void edicionInexistente_falla() {
        edicionExiste = false;
        assertThrows(Exception.class, () -> servicio().listarInscripciones("benkenobi", EDICION));
        assertThrows(Exception.class, () ->
                servicio().seleccionarEstudiantes("benkenobi", EDICION, mapa("weiss", EstadoInscripcion.ACEPTADA)));
    }

    // ---------------- Seleccion (Tabla 1) ----------------

    @Test
    void inscriptoPasaAAceptada() throws Exception {
        servicio().seleccionarEstudiantes("benkenobi", EDICION, mapa("weiss", EstadoInscripcion.ACEPTADA));

        assertEquals(mapa("weiss", EstadoInscripcion.ACEPTADA), guardado);
    }

    @Test
    void inscriptoPasaARechazada() throws Exception {
        servicio().seleccionarEstudiantes("benkenobi", EDICION, mapa("weiss", EstadoInscripcion.RECHAZADA));

        assertEquals(mapa("weiss", EstadoInscripcion.RECHAZADA), guardado);
    }

    @Test
    void noSePuedeVolverAInscripto() {
        assertThrows(Exception.class, () ->
                servicio().seleccionarEstudiantes("benkenobi", EDICION, mapa("weiss", EstadoInscripcion.INSCRIPTO)));
        assertNull(guardado);
    }

    @Test
    void noSePuedeCambiarUnaInscripcionYaResuelta() {
        Exception e = assertThrows(Exception.class, () ->
                servicio().seleccionarEstudiantes("benkenobi", EDICION, mapa("chechi", EstadoInscripcion.RECHAZADA)));

        assertTrue(e.getMessage().contains("chechi"));
        assertNull(guardado);
    }

    @Test
    void repetirElEstadoYaGuardado_noHaceNadaYNoFalla() throws Exception {
        servicio().seleccionarEstudiantes("benkenobi", EDICION, mapa("chechi", EstadoInscripcion.ACEPTADA));

        assertNull(guardado);
    }

    @Test
    void estudianteSinInscripcion_fallaYNoGuardaNada() {
        assertThrows(Exception.class, () ->
                servicio().seleccionarEstudiantes("benkenobi", EDICION,
                        mapa("weiss", EstadoInscripcion.ACEPTADA, "fantasma", EstadoInscripcion.ACEPTADA)));
        assertNull(guardado);
    }

    @Test
    void seleccionVaciaONula_falla() {
        assertThrows(Exception.class, () -> servicio().seleccionarEstudiantes("benkenobi", EDICION, null));
        assertThrows(Exception.class, () -> servicio().seleccionarEstudiantes("benkenobi", EDICION, new LinkedHashMap<>()));
    }

    @Test
    void seleccionMixta_guardaSoloLosCambiosReales() throws Exception {
        servicio().seleccionarEstudiantes("benkenobi", EDICION,
                mapa("weiss", EstadoInscripcion.RECHAZADA, "chechi", EstadoInscripcion.ACEPTADA));

        assertEquals(mapa("weiss", EstadoInscripcion.RECHAZADA), guardado);
    }
}
