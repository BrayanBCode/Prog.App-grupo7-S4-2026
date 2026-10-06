package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.entities.cursos.Categoria;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Logica.repositories.CategoriaRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import ServidorCentral.Logica.seguridad.Rol;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias SIN base de datos: los repositorios se reemplazan por
 * versiones en memoria (solo se pisan los metodos que toca cada caso de uso).
 */
class AutenticacionYCategoriaServiceTest {

    // ---------------- Inicio de sesion ----------------

    private final Docente docente = new Docente("benkenobi", "benkenobi@gmail.com", "Obi-Wan", "Kenobi",
            LocalDate.of(1914, 4, 2), null, "1234");
    private final Estudiante estudiante = new Estudiante("weiss", "aweiss@hotmail.com", "Adrian", "Weiss",
            LocalDate.of(1978, 12, 23), null, "abcd");
    private final Estudiante sinContraseña = new Estudiante("viejo", "viejo@x.com", "Viejo", "Dato",
            LocalDate.of(1990, 1, 1), null, null);

    private UsuarioService servicioDeUsuarios() {
        UsuarioRepository repo = new UsuarioRepository(null) {
            @Override
            public Usuario buscarPorNicknameOMail(String id) {
                for (Usuario u : new Usuario[]{docente, estudiante, sinContraseña}) {
                    if (u.getNickname().equals(id) || u.getMail().equals(id)) {
                        return u;
                    }
                }
                return null;
            }
        };
        return new UsuarioService(repo, null, null);
    }

    @Test
    void docenteEntraConNickname_yQuedaConRolDocente() {
        DTSesion s = servicioDeUsuarios().autenticar("benkenobi", "1234");
        assertEquals(Rol.DOCENTE, s.getRol());
        assertEquals("benkenobi", s.getNickname());
    }

    @Test
    void estudianteEntraConMail_yQuedaConRolEstudiante() {
        DTSesion s = servicioDeUsuarios().autenticar("aweiss@hotmail.com", "abcd");
        assertEquals(Rol.ESTUDIANTE, s.getRol());
        assertEquals("Adrian", s.getNombre());
    }

    @Test
    void credencialesInvalidas_usuarioInexistente_ySinClaveDanElMismoError() {
        UsuarioService svc = servicioDeUsuarios();
        String m1 = assertThrows(IllegalArgumentException.class, () -> svc.autenticar("weiss", "mala")).getMessage();
        String m2 = assertThrows(IllegalArgumentException.class, () -> svc.autenticar("noexiste", "abcd")).getMessage();
        String m3 = assertThrows(IllegalArgumentException.class, () -> svc.autenticar("viejo", "algo")).getMessage();
        assertEquals(m1, m2);   // no se revela si el usuario existe
        assertEquals(m1, m3);
    }

    @Test
    void datosVaciosONulos_seRechazan() {
        UsuarioService svc = servicioDeUsuarios();
        assertThrows(IllegalArgumentException.class, () -> svc.autenticar("", "abcd"));
        assertThrows(IllegalArgumentException.class, () -> svc.autenticar(null, "abcd"));
        assertThrows(IllegalArgumentException.class, () -> svc.autenticar("weiss", null));
        assertThrows(IllegalArgumentException.class, () -> svc.autenticar("weiss", ""));
    }

    // ---------------- Alta de categoria ----------------

    private final List<String> guardadas = new ArrayList<>();

    private CategoriaService servicioDeCategorias() {
        CategoriaRepository repo = new CategoriaRepository(null) {
            @Override
            public Categoria buscarPorNombre(String nombre) {
                return guardadas.contains(nombre) ? new Categoria(nombre) : null;
            }

            @Override
            public void guardar(String nombre) {
                guardadas.add(nombre);
            }

            @Override
            public List<String> obtenerNombres() {
                return new ArrayList<>(guardadas);
            }
        };
        return new CategoriaService(repo);
    }

    @Test
    void altaDeCategoria_guardaElNombreSinEspaciosSobrantes() throws Exception {
        CategoriaService svc = servicioDeCategorias();
        svc.registrar("  Social  ");
        assertEquals(List.of("Social"), svc.obtenerNombres());
    }

    @Test
    void altaDeCategoria_rechazaNombreRepetido() throws Exception {
        CategoriaService svc = servicioDeCategorias();
        svc.registrar("Social");
        Exception e = assertThrows(Exception.class, () -> svc.registrar("Social"));
        assertTrue(e.getMessage().contains("Social"));
        assertEquals(1, guardadas.size());
    }

    @Test
    void altaDeCategoria_rechazaNombreVacioONulo() {
        CategoriaService svc = servicioDeCategorias();
        assertThrows(Exception.class, () -> svc.registrar("   "));
        assertThrows(Exception.class, () -> svc.registrar(null));
        assertTrue(guardadas.isEmpty());
    }
}
