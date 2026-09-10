package Logica.controller;

import Logica.cursos.Instituto;
import Logica.usuarios.Usuario;
import Logica.usuarios.UsuarioID;
import Persistencia.Conexion;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TESTS DE INTEGRACIÓN del Controller — corren contra la base de datos
 * REAL (la misma que usa la app), no contra una simulada. Por eso:
 *
 *   - Todos los datos de prueba usan el prefijo "zzTest_" para que sean
 *     imposibles de confundir con datos reales del proyecto.
 *   - Cada test limpia lo que creó en @AfterEach, incluso si el test
 *     falla a mitad de camino (JUnit 5 garantiza que @AfterEach corre
 *     siempre que @BeforeEach/el test hayan empezado a ejecutarse).
 *   - NO correr estos tests mientras alguien esté usando la app en vivo
 *     o haciendo una demo — pueden pisarse con datos de otra persona.
 *
 * No usan Mockito ni ningún framework de mocks: son JUnit 5 puro
 * ejecutando el Controller real contra la base real.
 *
 * @author briha
 */
public class ControllerTest {

    private static final String PREFIJO = "zzTest_";

    private final Controller controller = new Controller();

    @AfterEach
    public void limpiarDatosDePrueba() {
        // Limpieza directa por SQL nativo (no hay método de baja en el
        // Controller todavía, y JPQL "DELETE FROM Usuario" no sirve porque
        // Usuario es TABLE_PER_CLASS: no tiene tabla propia, cada subclase
        // (Estudiante, Docente) tiene la suya). Se usa un EntityManager
        // propio, aparte del que usa el Controller internamente.
        //
        // Orden importante: primero la tabla intermedia instituto_docente
        // (tiene FK hacia docente), recién después docente/estudiante/instituto.
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();

            em.createNativeQuery("DELETE FROM INSTITUTO_DOCENTE WHERE NICKNAME LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM DOCENTE_EDICIONCURSO WHERE NICKNAME LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM DOCENTE WHERE NICKNAME LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM ESTUDIANTE WHERE NICKNAME LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM INSTITUTO WHERE NOMBRE LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // No relanzamos: si la limpieza falla, mejor que se vea en la
            // consola y no que tape el resultado real del test.
            System.err.println("Aviso: falló la limpieza de datos de test: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // ==================================================================
    // altaInstituto
    // ==================================================================

    @Test
    public void altaInstituto_loCreaYAparaceEnElListado() throws Exception {
        String nombre = PREFIJO + "Instituto";

        controller.altaInstituto(nombre);

        List<String> institutos = controller.listarNombresInstitutos();
        assertTrue(institutos.contains(nombre));
    }

    @Test
    public void altaInstituto_lanzaExcepcion_siYaExisteUnoConEseNombre() throws Exception {
        String nombre = PREFIJO + "InstitutoDuplicado";
        controller.altaInstituto(nombre); // primera alta: ok

        Exception ex = assertThrows(Exception.class, () -> controller.altaInstituto(nombre));

        assertTrue(ex.getMessage().contains("Ya existe"));
    }

    @Test
    public void altaInstituto_lanzaExcepcion_siElNombreEstaVacio() {
        Exception ex = assertThrows(Exception.class, () -> controller.altaInstituto("   "));

        assertTrue(ex.getMessage().contains("vacío"));
    }

    // ==================================================================
    // altaUsuario / obtenerDataUsuario (caso Estudiante, sin instituto)
    // ==================================================================

    @Test
    public void altaUsuario_creaUnEstudiante_yObtenerDataUsuarioLoEncuentra() {
        String nickname = PREFIJO + "jperez";
        String mail = nickname + "@correo.uy";

        controller.altaUsuario(nickname, mail, "Juan", "Perez", LocalDate.of(2001, 3, 15), null, null);

        String[] datos = controller.obtenerDataUsuario(nickname, mail);

        assertNotNull(datos);
        assertEquals(nickname, datos[0]);
        assertEquals(mail, datos[1]);
        assertEquals("Juan", datos[2]);
        assertEquals("Perez", datos[3]);
    }

    @Test
    public void altaUsuario_lanzaExcepcion_siElNicknameYaExiste() {
        String nickname = PREFIJO + "duplicado";
        String mail1 = nickname + "@correo.uy";
        String mail2 = nickname + "2@correo.uy"; // mail distinto, nickname repetido

        controller.altaUsuario(nickname, mail1, "Juan", "Perez", LocalDate.of(2001, 3, 15), null, null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                controller.altaUsuario(nickname, mail2, "Otro", "Nombre", LocalDate.of(2000, 1, 1), null, null)
        );

        assertTrue(ex.getMessage().contains("ya se encuentran registrados"));
    }

    // ==================================================================
    // modificarUsuario
    // ==================================================================

    @Test
    public void modificarUsuario_actualizaLosDatosBasicos() throws Exception {
        String nickname = PREFIJO + "modificar";
        String mail = nickname + "@correo.uy";
        controller.altaUsuario(nickname, mail, "Nombre Viejo", "Apellido Viejo", LocalDate.of(2000, 1, 1), null, null);

        controller.modificarUsuario(nickname, mail, "Nombre Nuevo", "Apellido Nuevo", LocalDate.of(2000, 1, 1));

        String[] datos = controller.obtenerDataUsuario(nickname, mail);
        assertEquals("Nombre Nuevo", datos[2]);
        assertEquals("Apellido Nuevo", datos[3]);
    }

    @Test
    public void modificarUsuario_lanzaExcepcion_siElUsuarioNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.modificarUsuario(PREFIJO + "noexiste", "noexiste@correo.uy", "X", "Y", LocalDate.now())
        );

        assertTrue(ex.getMessage().contains("no existe"));
    }

    // ==================================================================
    // esDocente (usa un Docente real, que a su vez necesita un Instituto real)
    // ==================================================================

    @Test
    public void esDocente_devuelveTrue_paraUnDocenteRecienCreado() {
        String institutoNombre = PREFIJO + "InstitutoDocente";
        String nickname = PREFIJO + "docente1";
        String mail = nickname + "@correo.uy";

        try {
            controller.altaInstituto(institutoNombre);
        } catch (Exception e) {
            fail("No se pudo preparar el instituto para el test: " + e.getMessage());
        }

        // instituto != null -> altaUsuario lo crea como Docente
        controller.altaUsuario(nickname, mail, "Docente", "DePrueba", LocalDate.of(1985, 4, 15), institutoNombre, null);

        assertTrue(controller.esDocente(nickname));
    }

    @Test
    public void esDocente_devuelveFalse_paraUnEstudiante() {
        String nickname = PREFIJO + "estudiante1";
        String mail = nickname + "@correo.uy";
        controller.altaUsuario(nickname, mail, "Estudiante", "DePrueba", LocalDate.of(2001, 3, 15), null, null);

        assertFalse(controller.esDocente(nickname));
    }
}