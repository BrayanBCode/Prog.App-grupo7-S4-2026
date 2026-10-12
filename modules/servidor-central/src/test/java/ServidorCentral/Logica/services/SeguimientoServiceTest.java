package ServidorCentral.Logica.services;

import ServidorCentral.Logica.repositories.SeguimientoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas SIN base de datos del caso de uso "Seguir a un Usuario". */
class SeguimientoServiceTest {

    private final List<String> usuarios = List.of("weiss", "chechi", "benkenobi");
    /** Seguimientos guardados, como "seguidor>seguido". */
    private final List<String> guardados = new ArrayList<>();

    private SeguimientoService servicio() {
        UsuarioRepository usuarioRepo = new UsuarioRepository(null) {
            @Override
            public boolean existeNickname(String nickname) {
                return usuarios.contains(nickname);
            }
        };
        SeguimientoRepository seguimientoRepo = new SeguimientoRepository(null) {
            @Override
            public boolean existe(String seguidor, String seguido) {
                return guardados.contains(seguidor + ">" + seguido);
            }

            @Override
            public void guardar(String seguidor, String seguido) {
                guardados.add(seguidor + ">" + seguido);
            }

            @Override
            public List<String> seguidosDe(String nickname) {
                List<String> r = new ArrayList<>();
                for (String g : guardados) {
                    if (g.startsWith(nickname + ">")) r.add(g.substring(g.indexOf('>') + 1));
                }
                return r;
            }

            @Override
            public List<String> seguidoresDe(String nickname) {
                List<String> r = new ArrayList<>();
                for (String g : guardados) {
                    if (g.endsWith(">" + nickname)) r.add(g.substring(0, g.indexOf('>')));
                }
                return r;
            }
        };
        return new SeguimientoService(usuarioRepo, seguimientoRepo);
    }

    @Test
    void seguirUsuarioExistente_loRegistra() throws Exception {
        SeguimientoService s = servicio();
        s.seguir("weiss", "chechi");

        assertEquals(List.of("weiss>chechi"), guardados);
        assertTrue(s.estaSiguiendo("weiss", "chechi"));
    }

    @Test
    void seguirNoEsSimetrico() throws Exception {
        SeguimientoService s = servicio();
        s.seguir("weiss", "chechi");

        assertFalse(s.estaSiguiendo("chechi", "weiss"));
    }

    @Test
    void noSePuedeSeguirALUnoMismo() {
        assertThrows(Exception.class, () -> servicio().seguir("weiss", "weiss"));
        assertTrue(guardados.isEmpty());
    }

    @Test
    void noSePuedeSeguirDosVecesAlMismoUsuario() throws Exception {
        SeguimientoService s = servicio();
        s.seguir("weiss", "chechi");

        Exception e = assertThrows(Exception.class, () -> s.seguir("weiss", "chechi"));
        assertTrue(e.getMessage().contains("chechi"));
        assertEquals(1, guardados.size());
    }

    @Test
    void seguirAUsuarioInexistente_falla() {
        assertThrows(Exception.class, () -> servicio().seguir("weiss", "fantasma"));
        assertThrows(Exception.class, () -> servicio().seguir("fantasma", "weiss"));
        assertTrue(guardados.isEmpty());
    }

    @Test
    void datosVaciosONulos_fallan() {
        assertThrows(Exception.class, () -> servicio().seguir(null, "weiss"));
        assertThrows(Exception.class, () -> servicio().seguir("weiss", null));
        assertThrows(Exception.class, () -> servicio().seguir("weiss", "  "));
        assertTrue(guardados.isEmpty());
    }

    @Test
    void listarSeguidosYSeguidores() throws Exception {
        SeguimientoService s = servicio();
        s.seguir("weiss", "chechi");
        s.seguir("weiss", "benkenobi");
        s.seguir("chechi", "benkenobi");

        assertEquals(List.of("chechi", "benkenobi"), s.listarSeguidos("weiss"));
        assertEquals(List.of("weiss", "chechi"), s.listarSeguidores("benkenobi"));
        assertTrue(s.listarSeguidores("weiss").isEmpty());
    }
}
