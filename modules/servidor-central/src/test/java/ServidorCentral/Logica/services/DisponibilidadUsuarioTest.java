package ServidorCentral.Logica.services;

import ServidorCentral.Logica.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos del aviso Ajax del Alta de Usuario: si el nickname o el correo ya estan en uso.
 * Existen weiss (aweiss@hotmail.com) y benkenobi (benkenobi@gmail.com).
 */
class DisponibilidadUsuarioTest {

    private final List<String> nicks = List.of("weiss", "benkenobi");
    private final List<String> mails = List.of("aweiss@hotmail.com", "benkenobi@gmail.com");

    private UsuarioService servicio() {
        UsuarioRepository repo = new UsuarioRepository(null) {
            @Override
            public boolean existeNickname(String nickname) {
                return nicks.contains(nickname);
            }

            @Override
            public boolean existeMail(String mail) {
                return mails.contains(mail);
            }
        };
        return new UsuarioService(repo, null, null);
    }

    @Test
    void nicknameEnUso_noEstaDisponible() {
        assertFalse(servicio().nicknameDisponible("weiss"));
        assertFalse(servicio().nicknameDisponible("  benkenobi "), "sin contar los espacios de los costados");
    }

    @Test
    void nicknameLibre_estaDisponible() {
        assertTrue(servicio().nicknameDisponible("nuevo_usuario"));
    }

    @Test
    void correoEnUso_noEstaDisponible() {
        assertFalse(servicio().mailDisponible("aweiss@hotmail.com"));
        assertTrue(servicio().mailDisponible("nuevo@example.test"));
    }

    @Test
    void vacioONulo_noEstaDisponible() {
        assertFalse(servicio().nicknameDisponible(null));
        assertFalse(servicio().nicknameDisponible("   "));
        assertFalse(servicio().mailDisponible(null));
        assertFalse(servicio().mailDisponible(""));
    }
}
