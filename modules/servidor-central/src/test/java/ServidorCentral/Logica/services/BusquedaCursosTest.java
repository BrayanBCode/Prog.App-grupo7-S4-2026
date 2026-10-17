package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.repositories.CursoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de la Busqueda de cursos (requerimiento 7.5). El filtro lo hace la consulta del repositorio
 * (probada contra H2 en CursoYDocenteRepositoryTest); aca se prueba que el servicio le pasa el texto limpio y
 * arma las tarjetas.
 */
class BusquedaCursosTest {

    private final List<String> buscados = new ArrayList<>();

    private final CursoService servicio = new CursoService(new CursoRepository(null) {
        @Override
        public List<Curso> buscar(String texto) {
            buscados.add(texto);
            Curso c = new Curso("MicroBit", "Placas", 4, 40f, 4, LocalDate.of(2026, 1, 1), null, null);
            c.setImagen("cursos/MicroBit.jpg");
            return List.of(c);
        }
    }, null, null);

    @Test
    void devuelveTarjetasConNombreDescripcionEImagen() {
        assertEquals(List.of(new DTCursoResumen("MicroBit", "Placas", "cursos/MicroBit.jpg")), servicio.buscarCursos("placa"));
    }

    @Test
    void textoSinEspaciosYVacioSiNoVino() {
        servicio.buscarCursos("  micro  ");
        servicio.buscarCursos(null);
        servicio.buscarCursos("");
        assertEquals(List.of("micro", "", ""), buscados);
    }
}
