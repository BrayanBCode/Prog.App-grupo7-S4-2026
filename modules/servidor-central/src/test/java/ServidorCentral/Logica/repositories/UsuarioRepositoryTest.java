package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Persistencia.Conexion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de UsuarioRepository e InstitutoRepository contra una base H2 en memoria (ver {@link BaseEnMemoria}).
 * Cada test arranca con el instituto Medicina, el docente house (de Medicina) y el estudiante weiss.
 */
class UsuarioRepositoryTest {

    private static final LocalDate NAC = LocalDate.of(1990, 5, 20);

    private Conexion conexion;
    private UsuarioRepository usuarios;
    private InstitutoRepository institutos;

    @BeforeEach
    void cargarDatos() {
        conexion = BaseEnMemoria.nueva();
        usuarios = new UsuarioRepository(conexion);
        institutos = new InstitutoRepository(conexion);
        institutos.guardar("Medicina");
        usuarios.guardarDocente("house", "house@x.com", "Gregory", "House", NAC, "usuarios/house.jpg", "Medicina", "1234");
        usuarios.guardarEstudiante("weiss", "weiss@x.com", "Alicia", "Weiss", NAC, null, "abcd");
    }

    @AfterEach
    void cerrar() {
        conexion.cerrar();
    }

    @Test
    void existeNicknameYMail_enDocentesYEstudiantes() {
        assertTrue(usuarios.existeNickname("house"));
        assertTrue(usuarios.existeNickname("weiss"));
        assertFalse(usuarios.existeNickname("nadie"));
        assertTrue(usuarios.existeMail("house@x.com"));
        assertTrue(usuarios.existeMail("weiss@x.com"));
        assertFalse(usuarios.existeMail("nadie@x.com"));
    }

    @Test
    void buscarPorId_devuelveDocenteOEstudiante() {
        Usuario house = usuarios.buscarPorId("house", "house@x.com");
        assertInstanceOf(Docente.class, house);
        assertEquals("Gregory", house.getNombreU());
        assertEquals("usuarios/house.jpg", house.getImagen());
        assertInstanceOf(Estudiante.class, usuarios.buscarPorId("weiss", "weiss@x.com"));
        assertNull(usuarios.buscarPorId("weiss", "otro@x.com"));
    }

    @Test
    void buscarPorNicknameOMail() {
        assertEquals("house", usuarios.buscarPorNicknameOMail("house").getNickname());
        assertEquals("house", usuarios.buscarPorNicknameOMail("house@x.com").getNickname());
        assertEquals("abcd", usuarios.buscarPorNicknameOMail("weiss@x.com").getContraseña());
        assertNull(usuarios.buscarPorNicknameOMail("nadie"));
    }

    @Test
    void listarTodos_docentesYEstudiantes() {
        assertEquals(List.of("house", "weiss"),
                usuarios.listarTodos().stream().map(Usuario::getNickname).sorted().toList());
    }

    @Test
    void docenteQuedaVinculadoAlInstituto() {
        Instituto medicina = institutos.buscarPorNombre("Medicina");
        assertEquals("Medicina", medicina.getNombre());
        assertEquals(List.of("Medicina"), new DocenteRepository(conexion).nombresInstitutos("house"));
    }

    @Test
    void actualizarDatosBasicos_noTocaNicknameNiCorreo() {
        LocalDate nueva = LocalDate.of(1991, 1, 1);
        usuarios.actualizarDatosBasicos("weiss", "weiss@x.com", "Ali", "Weis", nueva);

        Usuario u = usuarios.buscarPorId("weiss", "weiss@x.com");
        assertEquals("Ali", u.getNombreU());
        assertEquals("Weis", u.getApellido());
        assertEquals(nueva, u.getFechaNac());
    }

    @Test
    void actualizarUsuarioInexistente_falla() {
        assertThrows(RuntimeException.class,
                () -> usuarios.actualizarDatosBasicos("nadie", "nadie@x.com", "A", "B", NAC));
    }

    @Test
    void guardarNicknameRepetido_falla() {
        assertThrows(RuntimeException.class,
                () -> usuarios.guardarEstudiante("weiss", "weiss@x.com", "Otra", "Weiss", NAC, null, "1"));
    }

    @Test
    void institutos_buscarListarYGuardar() {
        institutos.guardar("INCO");
        assertEquals(List.of("INCO", "Medicina"), institutos.obtenerNombres());
        assertNull(institutos.buscarPorNombre("Abogacia"));
        assertThrows(RuntimeException.class, () -> institutos.guardar("INCO"), "nombre repetido");
    }
}
