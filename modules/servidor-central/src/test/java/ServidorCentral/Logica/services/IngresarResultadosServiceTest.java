package ServidorCentral.Logica.services;

import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.EdicionCursoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de "Ingresar resultados de evaluaciones" (Tarea 3): quien puede cargar notas y cerrar.
 * Las reglas de la nota y del cierre (1 a 12, solo aceptados, todas antes de cerrar) se prueban en NotasYCierreTest.
 */
class IngresarResultadosServiceTest {

    private static final String EDICION = "Robotica 2024";

    private final Docente participa = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");

    private final List<InscripcionEdicion> inscripciones = List.of(
            insc("chechi", EstadoInscripcion.ACEPTADA),
            insc("roro", EstadoInscripcion.RECHAZADA));

    /** Lo que el servicio mando al repositorio (null = nada). */
    private Map<String, Integer> notasGuardadas;
    private String edicionCerrada;

    private static InscripcionEdicion insc(String nick, EstadoInscripcion estado) {
        Estudiante e = new Estudiante(nick, nick + "@x.com", nick, "x", LocalDate.of(1990, 1, 1), null, "x");
        InscripcionEdicion i = new InscripcionEdicion(LocalDate.of(2024, 2, 1), null, e);
        i.setEstado(estado);
        return i;
    }

    private EdicionCursoService servicio() {
        EdicionCursoRepository repo = new EdicionCursoRepository(null) {
            @Override
            public EdicionCurso obtenerPorNombres(String nombreEdicion) {
                // Ya terminada: las notas no se limitan a la edicion vigente
                return EDICION.equals(nombreEdicion)
                        ? new EdicionCurso(EDICION, new Curso("Robotica", "d", 4, 60f, 8, LocalDate.of(2023, 1, 1), "u", new Instituto("INCO")),
                                LocalDate.of(2024, 3, 1), LocalDate.of(2024, 5, 10), 10, LocalDate.of(2024, 1, 1))
                        : null;
            }

            @Override
            public List<Docente> docentesDeEdicion(String nombreEdicion) {
                return List.of(participa);
            }

            @Override
            public List<InscripcionEdicion> inscripcionesDeEdicion(String nombreEdicion) {
                return inscripciones;
            }

            @Override
            public void registrarNotas(String nombreEdicion, Map<String, Integer> notas) {
                notasGuardadas = notas;
            }

            @Override
            public void cerrarEdicion(String nombreEdicion) {
                edicionCerrada = nombreEdicion;
            }
        };
        return new EdicionCursoService(repo, null, null, null);
    }

    @Test
    void docenteQueParticipa_guardaNotasDeUnaEdicionTerminada() {
        servicio().registrarNotas("benkenobi", EDICION, Map.of("chechi", 9));
        assertEquals(Map.of("chechi", 9), notasGuardadas);
    }

    @Test
    void docenteAjeno_noGuardaNiCierra() {
        EdicionCursoService s = servicio();
        assertThrows(ReglaNegocioException.class, () -> s.registrarNotas("waston", EDICION, Map.of("chechi", 9)));
        assertThrows(ReglaNegocioException.class, () -> s.cerrarEdicion("waston", EDICION));
        assertNull(notasGuardadas);
        assertNull(edicionCerrada);
    }

    @Test
    void edicionInexistente_falla() {
        assertThrows(ReglaNegocioException.class, () -> servicio().registrarNotas("benkenobi", "No existe", Map.of("chechi", 9)));
    }

    @Test
    void sinNotas_oEstudianteNoInscripto_oNotaVacia_noGuardaNada() {
        EdicionCursoService s = servicio();
        assertThrows(ReglaNegocioException.class, () -> s.registrarNotas("benkenobi", EDICION, Map.of()));
        assertThrows(ReglaNegocioException.class, () -> s.registrarNotas("benkenobi", EDICION, null));
        assertThrows(ReglaNegocioException.class, () -> s.registrarNotas("benkenobi", EDICION, Map.of("chechi", 9, "nadie", 7)));
        Map<String, Integer> conVacia = new HashMap<>();
        conVacia.put("chechi", null);
        assertThrows(ReglaNegocioException.class, () -> s.registrarNotas("benkenobi", EDICION, conVacia));
        assertNull(notasGuardadas);
    }

    @Test
    void docenteQueParticipa_cierra() {
        servicio().cerrarEdicion("benkenobi", EDICION);
        assertEquals(EDICION, edicionCerrada);
    }
}
