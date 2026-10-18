package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.repositories.DocenteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de Consulta de Docente y las tablas de docentes (Swing).
 * house es de Medicina y dicta Anatomia; benkenobi es de INCO y no tiene nada a cargo.
 */
class DocenteServiceTest {

    private final Docente house = new Docente("house", "house@x.com", "Gregory", "House", LocalDate.of(1970, 1, 1), null, "1234");
    private final Docente benkenobi = new Docente("benkenobi", "ben@x.com", "Obi-Wan", "Kenobi", LocalDate.of(1970, 1, 1), null, "1234");

    private DocenteService servicio() {
        return new DocenteService(new DocenteRepository(null) {
            @Override
            public List<Docente> listarTodos() {
                return List.of(house, benkenobi);
            }

            @Override
            public List<Docente> listarPorInstituto(String instituto) {
                return "Medicina".equals(instituto) ? List.of(house) : List.of();
            }

            @Override
            public List<String> nombresInstitutos(String nick) {
                return "house".equals(nick) ? List.of("Medicina") : List.of("INCO");
            }

            @Override
            public List<String> nombresCursos(String nick) {
                return "house".equals(nick) ? List.of("Anatomia") : List.of();
            }

            @Override
            public List<String> nombresEdiciones(String nick) {
                return "house".equals(nick) ? List.of("Anatomia-2026") : List.of();
            }

            @Override
            public List<String> nombresProgramas(String nick) {
                return null; // el DataType lo convierte en lista vacia
            }
        });
    }

    @Test
    void tablaDeDocentes_todosOPorInstituto() {
        assertEquals(List.of(new DTDocenteResumen("house", "Gregory", "House"),
                new DTDocenteResumen("benkenobi", "Obi-Wan", "Kenobi")), servicio().listarDocentesTabla());
        assertEquals(List.of(new DTDocenteResumen("house", "Gregory", "House")), servicio().porInstitutoTabla("Medicina"));
        assertTrue(servicio().porInstitutoTabla("Abogacia").isEmpty());
    }

    @Test
    void consultaDeDocente_institutosCursosEdicionesYProgramas() {
        DTDocenteDetalle d = servicio().obtenerData("house");
        assertEquals(List.of("Medicina"), d.institutos());
        assertEquals(List.of("Anatomia"), d.cursos());
        assertEquals(List.of("Anatomia-2026"), d.ediciones());
        assertEquals(List.of(), d.programas(), "nunca null");
    }

    @Test
    void docenteSinNadaACargo_listasVacias() {
        DTDocenteDetalle d = servicio().obtenerData("benkenobi");
        assertTrue(d.cursos().isEmpty());
        assertTrue(d.ediciones().isEmpty());
    }
}
