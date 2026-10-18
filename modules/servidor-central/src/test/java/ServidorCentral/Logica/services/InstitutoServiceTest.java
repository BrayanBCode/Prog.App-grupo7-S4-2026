package ServidorCentral.Logica.services;

import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas SIN base de datos de Alta de Instituto (Swing). Ya existe Medicina. */
class InstitutoServiceTest {

    private final List<String> institutos = new ArrayList<>(List.of("Medicina"));

    private InstitutoService servicio() {
        return new InstitutoService(new InstitutoRepository(null) {
            @Override
            public Instituto buscarPorNombre(String nombre) {
                return institutos.contains(nombre) ? new Instituto(nombre) : null;
            }

            @Override
            public List<String> obtenerNombres() {
                return institutos;
            }

            @Override
            public void guardar(String nombre) {
                institutos.add(nombre);
            }
        });
    }

    @Test
    void alta_guardaElNombreSinEspacios() throws Exception {
        servicio().registrar("  INCO ");
        assertEquals(List.of("Medicina", "INCO"), servicio().obtenerNombres());
    }

    @Test
    void alta_conNombreVacio_falla() {
        assertThrows(Exception.class, () -> servicio().registrar(null));
        assertThrows(Exception.class, () -> servicio().registrar("   "));
        assertEquals(List.of("Medicina"), institutos);
    }

    @Test
    void alta_repetido_falla() {
        Exception e = assertThrows(Exception.class, () -> servicio().registrar("Medicina"));
        assertTrue(e.getMessage().contains("Medicina"));
        assertEquals(1, institutos.size());
    }
}
