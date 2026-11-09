package ServidorCentral.Logica.services;

import ServidorCentral.DataTypes.DTEdicionCurso;
import ServidorCentral.DataTypes.DTInscripcionEstudiante;
import ServidorCentral.DataTypes.DTPerfil;
import ServidorCentral.DataTypes.DTUsuario;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Logica.repositories.SeguimientoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import ServidorCentral.Logica.seguridad.Rol;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos de la Consulta de Usuario: que ve cada uno en un perfil.
 * Datos: el docente benkenobi (INCO, dos ediciones), los estudiantes weiss (una inscripcion
 * en cada estado y un programa) y chechi, que sigue a weiss.
 */
class PerfilServiceTest {

    private final Docente benkenobi = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), "usuarios/benkenobi.jpg", "1234");
    private final Estudiante weiss = new Estudiante("weiss", "aweiss@hotmail.com", "Adrian", "Weiss",
            LocalDate.of(1978, 12, 23), null, "1234");
    private final Estudiante chechi = new Estudiante("Chechi", "cgarrido@hotmail.com", "Cecilia", "Garrido",
            LocalDate.of(1987, 9, 12), null, "1234");
    private final List<Usuario> usuarios = List.of(weiss, benkenobi, chechi);

    private final Curso robotica = new Curso("Robotica", "d", 4, 60f, 8, LocalDate.of(2024, 1, 1), "u", new Instituto("INCO"));
    private final EdicionCurso ed2025 = new EdicionCurso("Robotica 2025", robotica,
            LocalDate.of(2025, 3, 1), LocalDate.of(2025, 7, 1), 10, LocalDate.of(2025, 1, 1));
    private final EdicionCurso ed2026 = new EdicionCurso("Robotica 2026", robotica,
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 1), 0, LocalDate.of(2026, 1, 1));

    /** Seguimientos, como "seguidor>seguido". */
    private final List<String> seguimientos = List.of("Chechi>weiss");

    private PerfilService servicio() {
        UsuarioRepository usuarioRepo = new UsuarioRepository(null) {
            @Override
            public List<Usuario> listarTodos() {
                return usuarios;
            }

            @Override
            public Usuario buscarPorNicknameOMail(String id) {
                for (Usuario u : usuarios) {
                    if (u.getNickname().equals(id) || u.getMail().equals(id)) {
                        return u;
                    }
                }
                return null;
            }
        };
        DocenteRepository docenteRepo = new DocenteRepository(null) {
            @Override
            public boolean existe(String nickname) {
                return benkenobi.getNickname().equals(nickname);
            }

            @Override
            public List<String> nombresInstitutos(String nickname) {
                return List.of("INCO");
            }

            @Override
            public List<EdicionCurso> edicionesDe(String nickname) {
                return List.of(ed2025, ed2026);
            }
        };
        EstudianteRepository estudianteRepo = new EstudianteRepository(null) {
            @Override
            public List<InscripcionEdicion> inscripcionesDe(String nickname) {
                if (!nickname.equals("weiss")) {
                    return List.of();
                }
                InscripcionEdicion inscripto = new InscripcionEdicion(LocalDate.of(2026, 2, 1), ed2026, weiss);
                InscripcionEdicion aceptada = new InscripcionEdicion(LocalDate.of(2025, 2, 1), ed2025, weiss);
                aceptada.setEstado(EstadoInscripcion.ACEPTADA);
                InscripcionEdicion rechazada = new InscripcionEdicion(LocalDate.of(2025, 2, 15), ed2025, weiss);
                rechazada.setEstado(EstadoInscripcion.RECHAZADA);
                return List.of(aceptada, inscripto, rechazada);
            }

            @Override
            public List<Object[]> programasConFecha(String nickname) {
                List<Object[]> filas = new ArrayList<>();
                if (nickname.equals("weiss")) {
                    filas.add(new Object[]{"EFI Robotica", LocalDate.of(2025, 1, 10)});
                }
                return filas;
            }
        };
        SeguimientoRepository seguimientoRepo = new SeguimientoRepository(null) {
            @Override
            public boolean existe(String seguidor, String seguido) {
                return seguimientos.contains(seguidor + ">" + seguido);
            }

            @Override
            public List<String> seguidoresDe(String nickname) {
                List<String> r = new ArrayList<>();
                for (String s : seguimientos) {
                    if (s.endsWith(">" + nickname)) r.add(s.substring(0, s.indexOf('>')));
                }
                return r;
            }

            @Override
            public List<String> seguidosDe(String nickname) {
                List<String> r = new ArrayList<>();
                for (String s : seguimientos) {
                    if (s.startsWith(nickname + ">")) r.add(s.substring(s.indexOf('>') + 1));
                }
                return r;
            }
        };
        return new PerfilService(usuarioRepo, docenteRepo, estudianteRepo, seguimientoRepo);
    }

    private static List<EstadoInscripcion> estados(DTPerfil p) {
        return p.inscripciones().stream().map(DTInscripcionEstudiante::estado).toList();
    }

    // ---- Lista de usuarios ----

    @Test
    void listarUsuarios_todosOrdenadosPorNicknameSinImportarMayusculas() {
        List<DTUsuario> lista = servicio().listarUsuarios();

        assertEquals(List.of("benkenobi", "Chechi", "weiss"), lista.stream().map(DTUsuario::nickname).toList());
        assertEquals("usuarios/benkenobi.jpg", lista.get(0).imagen());
    }

    // ---- Usuario inexistente ----

    @Test
    void usuarioInexistente_falla() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class,
                () -> servicio().obtenerPerfil("nadie", null));
        assertEquals("No existe el usuario \"nadie\".", e.getMessage());
    }

    @Test
    void sinNicknameOPorMail_noLoEncuentra() {
        assertThrows(ReglaNegocioException.class, () -> servicio().obtenerPerfil(null, null));
        assertThrows(ReglaNegocioException.class, () -> servicio().obtenerPerfil("  ", null));
        assertThrows(ReglaNegocioException.class, () -> servicio().obtenerPerfil("aweiss@hotmail.com", null),
                "el perfil se busca por nickname, no por mail");
    }

    // ---- Visitante ----

    @Test
    void visitanteVeEstudiante_soloDatosBasicos() {
        DTPerfil p = servicio().obtenerPerfil("weiss", null);

        assertEquals("Adrian", p.usuario().nombre());
        assertEquals(Rol.ESTUDIANTE, p.rol());
        assertFalse(p.verDetalle());
        assertFalse(p.propio());
        assertTrue(p.inscripciones().isEmpty());
        assertTrue(p.programas().isEmpty());
        assertTrue(p.seguidores().isEmpty());
        assertTrue(p.seguidos().isEmpty());
        assertFalse(p.loSigue());
    }

    @Test
    void visitanteVeDocente_conInstitutoPeroSinEdiciones() {
        DTPerfil p = servicio().obtenerPerfil("benkenobi", null);

        assertTrue(p.esDocente());
        assertEquals(List.of("INCO"), p.institutos());
        assertTrue(p.ediciones().isEmpty());
        assertFalse(p.verAceptados());
    }

    // ---- Estudiante que mira ----

    @Test
    void estudianteVeOtroEstudiante_soloDatosBasicosYSeguir() {
        DTPerfil p = servicio().obtenerPerfil("weiss", "Chechi");

        assertTrue(p.verDetalle());
        assertFalse(p.propio());
        assertFalse(p.verCursos());
        assertFalse(p.verSeguidores());
        assertTrue(p.inscripciones().isEmpty());
        assertTrue(p.programas().isEmpty());
        assertTrue(p.seguidores().isEmpty());
        assertTrue(p.seguidos().isEmpty());
        assertTrue(p.loSigue(), "el boton Seguir sigue sabiendo si ya lo sigue");
    }

    @Test
    void estudianteVeSuPropioPerfil_conRechazadas() {
        DTPerfil p = servicio().obtenerPerfil("weiss", "weiss");

        assertTrue(p.propio());
        assertTrue(p.verCursos());
        assertTrue(p.verSeguidores());
        assertEquals(List.of("Chechi"), p.seguidores());
        assertEquals("EFI Robotica", p.programas().get(0).programa());
        assertEquals(3, p.inscripciones().size());
        assertTrue(estados(p).contains(EstadoInscripcion.RECHAZADA));
        assertFalse(p.loSigue(), "uno no se sigue a si mismo");
        assertFalse(p.verAceptados());
    }

    @Test
    void inscripcionesOrdenadasDeLaMasReciente() {
        DTPerfil p = servicio().obtenerPerfil("weiss", "weiss");

        assertEquals(List.of(LocalDate.of(2026, 2, 1), LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 1)),
                p.inscripciones().stream().map(DTInscripcionEstudiante::fechaInscripcion).toList());
        assertEquals("Robotica", p.inscripciones().get(0).curso());
    }

    @Test
    void estudianteVeDocente_sinEdicionesNiSeguidores() {
        DTPerfil p = servicio().obtenerPerfil("benkenobi", "weiss");

        assertFalse(p.verCursos());
        assertFalse(p.verSeguidores());
        assertTrue(p.ediciones().isEmpty());
        assertEquals(List.of("INCO"), p.institutos(), "los institutos son dato basico");
        assertFalse(p.verAceptados());
        assertFalse(p.loSigue());
        assertTrue(p.inscripciones().isEmpty(), "un docente no tiene inscripciones");
    }

    // ---- Docente que mira ----

    @Test
    void docenteVeEstudiante_cursosYProgramasConRechazadasPeroSinSeguidores() {
        DTPerfil p = servicio().obtenerPerfil("weiss", "benkenobi");

        assertFalse(p.propio());
        assertTrue(p.verCursos());
        assertTrue(estados(p).contains(EstadoInscripcion.RECHAZADA));
        assertEquals("EFI Robotica", p.programas().get(0).programa());
        assertFalse(p.verSeguidores());
        assertTrue(p.seguidores().isEmpty());
        assertTrue(p.seguidos().isEmpty());
    }

    @Test
    void docenteVeSuPropioPerfil_edicionesDeLaMasRecienteYVerAceptados() {
        DTPerfil p = servicio().obtenerPerfil("benkenobi", "benkenobi");

        assertTrue(p.propio());
        assertTrue(p.verAceptados());
        assertEquals(List.of("Robotica 2026", "Robotica 2025"), p.ediciones().stream().map(DTEdicionCurso::nombre).toList());
        assertEquals("Robotica", p.ediciones().get(0).nombreCurso());
    }
}
