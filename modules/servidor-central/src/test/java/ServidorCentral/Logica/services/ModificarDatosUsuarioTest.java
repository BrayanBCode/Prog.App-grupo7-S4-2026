package ServidorCentral.Logica.services;

import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de "Modificar Datos de Usuario" (4.11, web). Existen weiss (estudiante) y house (docente).
 * Nickname y correo no se cambian: el servicio los toma del usuario guardado.
 */
class ModificarDatosUsuarioTest {

    private static final LocalDate NAC = LocalDate.of(1990, 5, 20);
    private static final LocalDate NUEVA_NAC = LocalDate.of(1985, 3, 7);

    private final List<Usuario> usuarios = List.of(
            new Estudiante("weiss", "aweiss@hotmail.com", "Alicia", "Weiss", NAC, null, "1234"),
            new Docente("house", "house@x.com", "Gregory", "House", NAC, null, "1234"));

    /** Lo que el servicio le pidio actualizar al repositorio. */
    private final List<String> actualizados = new ArrayList<>();

    private final UsuarioService servicio = new UsuarioService(new UsuarioRepository(null) {
        @Override
        public Usuario buscarPorNicknameOMail(String id) {
            return usuarios.stream().filter(u -> u.getNickname().equals(id) || u.getMail().equals(id))
                    .findFirst().orElse(null);
        }

        @Override
        public void actualizarDatosBasicos(String n, String m, String no, String a, LocalDate f) {
            actualizados.add(n + " " + m + " " + no + " " + a + " " + f);
        }
    }, null, null);

    @Test
    void estudiante_actualizaNombreApellidoYFecha_sinEspaciosDeMas() {
        servicio.modificarDatos("weiss", "  Ali ", " Weiss Pérez ", NUEVA_NAC);
        assertEquals(List.of("weiss aweiss@hotmail.com Ali Weiss Pérez 1985-03-07"), actualizados);
    }

    @Test
    void docente_tambienPuede() {
        servicio.modificarDatos("house", "Greg", "House", NUEVA_NAC);
        assertEquals(List.of("house house@x.com Greg House 1985-03-07"), actualizados);
    }

    @Test
    void usuarioInexistente_oBuscadoPorCorreo_noSeModifica() {
        assertError("El usuario no existe.", "nadie", "A", "B", NUEVA_NAC);
        assertError("El usuario no existe.", "aweiss@hotmail.com", "A", "B", NUEVA_NAC);
        assertError("El usuario no existe.", " ", "A", "B", NUEVA_NAC);
        assertError("El usuario no existe.", null, "A", "B", NUEVA_NAC);
    }

    @Test
    void datosObligatorios() {
        assertError("El nombre es obligatorio.", "weiss", " ", "Weiss", NUEVA_NAC);
        assertError("El nombre es obligatorio.", "weiss", null, "Weiss", NUEVA_NAC);
        assertError("El apellido es obligatorio.", "weiss", "Alicia", "", NUEVA_NAC);
        assertError("El apellido es obligatorio.", "weiss", "Alicia", null, NUEVA_NAC);
        assertError("La fecha de nacimiento es obligatoria.", "weiss", "Alicia", "Weiss", null);
    }

    @Test
    void fechaDeNacimiento_tieneQueSerAnteriorAHoy() {
        assertError("La fecha de nacimiento tiene que ser anterior a hoy.", "weiss", "Alicia", "Weiss", LocalDate.now());
        assertError("La fecha de nacimiento tiene que ser anterior a hoy.", "weiss", "Alicia", "Weiss", LocalDate.now().plusDays(1));
    }

    private void assertError(String mensaje, String nick, String nombre, String apellido, LocalDate fecha) {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> servicio.modificarDatos(nick, nombre, apellido, fecha));
        assertEquals(mensaje, e.getMessage());
        assertTrue(actualizados.isEmpty(), "con un dato invalido no se actualiza nada");
    }
}
