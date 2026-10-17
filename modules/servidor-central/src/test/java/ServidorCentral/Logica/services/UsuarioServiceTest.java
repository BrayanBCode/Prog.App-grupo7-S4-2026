package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.datatypes.DTUsuarioResumen;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de Alta, Modificar y consultas de Usuario (los metodos que usa Swing y el web).
 * Existen weiss (estudiante) y house (docente); el unico instituto es Medicina.
 */
class UsuarioServiceTest {

    private static final LocalDate NAC = LocalDate.of(1990, 5, 20);

    private final Estudiante weiss = new Estudiante("weiss", "aweiss@hotmail.com", "Alicia", "Weiss", NAC, "usuarios/weiss.jpg", "1234");
    private final Docente house = new Docente("house", "house@x.com", "Gregory", "House", NAC, null, "1234");
    private final List<Usuario> usuarios = List.of(weiss, house);

    /** Lo que el servicio le pidio guardar o actualizar al repositorio. */
    private final List<String> guardados = new ArrayList<>();

    private UsuarioService servicio() {
        UsuarioRepository usuarioRepo = new UsuarioRepository(null) {
            @Override
            public boolean existeNickname(String nickname) {
                return usuarios.stream().anyMatch(u -> u.getNickname().equals(nickname));
            }

            @Override
            public boolean existeMail(String mail) {
                return usuarios.stream().anyMatch(u -> u.getMail().equals(mail));
            }

            @Override
            public Usuario buscarPorId(String nickname, String mail) {
                return usuarios.stream().filter(u -> u.getNickname().equals(nickname) && u.getMail().equals(mail))
                        .findFirst().orElse(null);
            }

            @Override
            public Usuario buscarPorNicknameOMail(String id) {
                return usuarios.stream().filter(u -> u.getNickname().equals(id) || u.getMail().equals(id))
                        .findFirst().orElse(null);
            }

            @Override
            public List<Usuario> listarTodos() {
                return usuarios;
            }

            @Override
            public void guardarEstudiante(String n, String m, String no, String a, LocalDate f, String img, String c) {
                guardados.add("estudiante " + n + " " + img);
            }

            @Override
            public void guardarDocente(String n, String m, String no, String a, LocalDate f, String img, String inst, String c) {
                guardados.add("docente " + n + " " + inst);
            }

            @Override
            public void actualizarDatosBasicos(String n, String m, String no, String a, LocalDate f) {
                guardados.add("actualizar " + n + " " + no + " " + a + " " + f);
            }
        };
        InstitutoRepository institutoRepo = new InstitutoRepository(null) {
            @Override
            public Instituto buscarPorNombre(String nombre) {
                return "Medicina".equals(nombre) ? new Instituto("Medicina") : null;
            }
        };
        DocenteRepository docenteRepo = new DocenteRepository(null) {
            @Override
            public boolean existe(String nickname) {
                return "house".equals(nickname);
            }
        };
        return new UsuarioService(usuarioRepo, institutoRepo, docenteRepo);
    }

    @Test
    void alta_sinInstituto_esEstudiante() {
        servicio().registrar("roro", "roro@x.com", "Rodrigo", "Cotelo", NAC, "", (DTImagen) null, "1234");
        assertEquals(List.of("estudiante roro null"), guardados, "sin foto no se guarda ruta");
    }

    @Test
    void alta_conInstituto_esDocente() {
        servicio().registrar("wilson", "wilson@x.com", "James", "Wilson", NAC, " Medicina ", (DTImagen) null, "1234");
        assertEquals(List.of("docente wilson Medicina"), guardados);
    }

    @Test
    void alta_desdeSwing_conRutaDeImagenQueNoExiste_noGuardaFoto() {
        servicio().registrar("roro", "roro@x.com", "Rodrigo", "Cotelo", NAC, null, "no/existe.jpg", "1234");
        assertEquals(List.of("estudiante roro null"), guardados);
    }

    @Test
    void alta_conNicknameOCorreoRepetido_falla() {
        UsuarioService s = servicio();
        assertThrows(ReglaNegocioException.class,
                () -> s.registrar("weiss", "otro@x.com", "A", "B", NAC, null, (DTImagen) null, "1234"));
        assertThrows(ReglaNegocioException.class,
                () -> s.registrar("otro", "house@x.com", "A", "B", NAC, null, (DTImagen) null, "1234"));
        assertTrue(guardados.isEmpty());
    }

    @Test
    void alta_docenteConInstitutoInexistente_fallaSinGuardar() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> servicio().registrar("wilson", "wilson@x.com", "James", "Wilson", NAC, "Abogacia", (DTImagen) null, "1234"));
        assertTrue(e.getMessage().contains("Abogacia"));
        assertTrue(guardados.isEmpty());
    }

    @Test
    void modificar_usuarioExistente_actualizaDatosBasicos() throws Exception {
        servicio().modificar("weiss", "aweiss@hotmail.com", "Ali", "Weis", NAC);
        assertEquals(List.of("actualizar weiss Ali Weis " + NAC), guardados);
    }

    @Test
    void modificar_usuarioInexistente_falla() {
        assertThrows(Exception.class, () -> servicio().modificar("nadie", "nadie@x.com", "A", "B", NAC));
        assertTrue(guardados.isEmpty());
    }

    @Test
    void obtenerDataUsuario_devuelveLosDatosONull() {
        DTUsuario d = servicio().obtenerDataUsuario("weiss", "aweiss@hotmail.com");
        assertEquals(new DTUsuario("weiss", "aweiss@hotmail.com", "Alicia", "Weiss", NAC, "usuarios/weiss.jpg"), d);
        assertNull(servicio().obtenerDataUsuario("weiss", "otro@x.com"));
    }

    @Test
    void obtenerPorNickname_soloPorNicknameExacto() {
        assertEquals("house", servicio().obtenerPorNickname(" house ").nickname());
        assertNull(servicio().obtenerPorNickname("house@x.com"), "por correo no: el web navega por nickname");
        assertNull(servicio().obtenerPorNickname("nadie"));
        assertNull(servicio().obtenerPorNickname(null));
        assertNull(servicio().obtenerPorNickname("  "));
    }

    @Test
    void listarUsuariosTabla_nicknameYCorreo() {
        assertEquals(List.of(new DTUsuarioResumen("weiss", "aweiss@hotmail.com"), new DTUsuarioResumen("house", "house@x.com")),
                servicio().listarUsuariosTabla());
    }

    @Test
    void esDocente_preguntaAlRepositorioDeDocentes() {
        assertTrue(servicio().esDocente("house"));
        assertFalse(servicio().esDocente("weiss"));
    }
}
