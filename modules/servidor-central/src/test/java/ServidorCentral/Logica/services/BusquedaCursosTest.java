package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTCursoResumen;
import ServidorCentral.Logica.datatypes.OrdenBusqueda;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.repositories.CursoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de la Busqueda de cursos (requerimiento 7.5) y su "Ordenar por". El filtro lo hace la consulta
 * del repositorio (probada contra H2 en CursoYDocenteRepositoryTest); aca se prueba que el servicio le pasa el texto
 * limpio, ordena y arma las tarjetas. El repositorio devuelve los cursos desordenados a proposito.
 */
class BusquedaCursosTest {

    private final List<String> buscados = new ArrayList<>();

    private final CursoService servicio = new CursoService(new CursoRepository(null) {
        @Override
        public List<Curso> buscar(String texto) {
            buscados.add(texto);
            Curso microbit = curso("MicroBit", 2024);
            microbit.setImagen("cursos/MicroBit.jpg");
            return List.of(curso("taller", 2026), microbit, curso("Ábaco", 2024), curso("komikan", 2026),
                    curso("Sin fecha", null), curso("Dalavuelta", 2025));
        }
    }, null, null);

    private static Curso curso(String nombre, Integer anio) {
        return new Curso(nombre, "d", 4, 40f, 4, anio == null ? null : LocalDate.of(anio, 6, 1), null, null);
    }

    private List<String> nombres(OrdenBusqueda orden) {
        return servicio.buscarCursos("x", orden).stream().map(DTCursoResumen::nombre).toList();
    }

    @Test
    void alfabetico_sinDistinguirMayusculasNiTildes() {
        List<String> esperado = List.of("Ábaco", "Dalavuelta", "komikan", "MicroBit", "Sin fecha", "taller");
        assertEquals(esperado, nombres(OrdenBusqueda.ALFABETICO_ASC));
        assertEquals(esperado, nombres(null), "sin orden elegido = alfabetico A-Z");
    }

    @Test
    void alfabeticoDescendente() {
        assertEquals(List.of("taller", "Sin fecha", "MicroBit", "komikan", "Dalavuelta", "Ábaco"),
                nombres(OrdenBusqueda.ALFABETICO_DESC));
    }

    @Test
    void anioAscendente_yDentroDelAnioAlfabetico_sinFechaAlFinal() {
        assertEquals(List.of("Ábaco", "MicroBit", "Dalavuelta", "komikan", "taller", "Sin fecha"),
                nombres(OrdenBusqueda.ANIO_ASC));
    }

    @Test
    void anioDescendente_yDentroDelAnioAlfabetico_sinFechaAlFinal() {
        assertEquals(List.of("komikan", "taller", "Dalavuelta", "Ábaco", "MicroBit", "Sin fecha"),
                nombres(OrdenBusqueda.ANIO_DESC));
    }

    @Test
    void devuelveTarjetasConNombreDescripcionEImagen() {
        assertTrue(servicio.buscarCursos("micro", null).contains(new DTCursoResumen("MicroBit", "d", "cursos/MicroBit.jpg")));
    }

    @Test
    void textoSinEspaciosYVacioSiNoVino() {
        servicio.buscarCursos("  micro  ", null);
        servicio.buscarCursos(null, null);
        servicio.buscarCursos("", null);
        assertEquals(List.of("micro", "", ""), buscados);
    }
}
