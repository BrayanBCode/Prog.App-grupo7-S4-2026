package Logica.controller;

import Logica.cursos.Instituto;
import Logica.programaFormacion.ProgramaFormacion;
import Logica.usuarios.Usuario;
import Logica.usuarios.UsuarioID;
import Persistencia.Conexion;

import java.time.LocalDate;
import java.util.ArrayList;
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
        // PASO 1 (vía JPA, no SQL nativo): limpiamos la relación ManyToMany
        // ProgramaFormacion <-> Curso ANTES de borrar filas por SQL nativo.
        // Así no hace falta adivinar el nombre real de la tabla intermedia
        // que genera EclipseLink (PROGRAMAFORMACION_CURSO o similar): dejamos
        // que el propio proveedor JPA se encargue de vaciarla.
        EntityManager emRel = Conexion.getInstancia().getEntityManager();
        try {
            emRel.getTransaction().begin();
            List<ProgramaFormacion> programas = emRel.createQuery(
                    "SELECT p FROM ProgramaFormacion p WHERE p.nombre LIKE :prefijo", ProgramaFormacion.class)
                    .setParameter("prefijo", PREFIJO + "%")
                    .getResultList();
            for (ProgramaFormacion p : programas) {
                p.getCursos().clear();
                emRel.merge(p);
            }
            emRel.getTransaction().commit();
        } catch (Exception e) {
            if (emRel.getTransaction().isActive()) {
                emRel.getTransaction().rollback();
            }
            System.err.println("Aviso: falló la limpieza de relaciones Programa-Curso: " + e.getMessage());
        } finally {
            emRel.close();
        }

        // PASO 2: limpieza por SQL nativo (no hay método de baja en el
        // Controller todavía, y JPQL "DELETE FROM Usuario" no sirve porque
        // Usuario es TABLE_PER_CLASS: no tiene tabla propia, cada subclase
        // (Estudiante, Docente) tiene la suya). Se usa un EntityManager
        // propio, aparte del que usa el Controller internamente.
        //
        // Orden importante (de "hijo" a "padre" según las FK):
        //   1) INSCRIPCIONEDICION (referencia a ESTUDIANTE y EDICIONCURSO)
        //   2) EDICIONCURSO (referencia a CURSO)
        //   3) CURSO (referencia a INSTITUTO y DOCENTE)
        //   4) PROGRAMAFORMACION (ya sin cursos asociados, ver PASO 1)
        //   5) tablas intermedias INSTITUTO_DOCENTE / DOCENTE_EDICIONCURSO
        //   6) DOCENTE / ESTUDIANTE
        //   7) INSTITUTO
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();

            em.createNativeQuery("DELETE FROM INSCRIPCIONEDICION WHERE ESTUDIANTE_NICKNAME LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM EDICIONCURSO WHERE NOMBRE LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM CURSO WHERE NOMBRE LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM PROGRAMAFORMACION WHERE NOMBRE LIKE ?")
                    .setParameter(1, PREFIJO + "%")
                    .executeUpdate();

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

    @Test
    public void listarInstitutos_incluyeElInstitutoCreado() throws Exception {
        String nombre = PREFIJO + "InstitutoListado";
        controller.altaInstituto(nombre);

        List<String> institutos = controller.listarInstitutos();

        assertTrue(institutos.contains(nombre));
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

    @Test
    public void listarUsuariosTabla_incluyeElUsuarioCreado() {
        String nickname = PREFIJO + "listadoUsuario";
        String mail = nickname + "@correo.uy";
        controller.altaUsuario(nickname, mail, "Ana", "Gomez", LocalDate.of(1999, 5, 20), null, null);

        List<String[]> tabla = controller.listarUsuariosTabla();

        assertTrue(tabla.stream().anyMatch(fila -> fila[0].equals(nickname)));
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

    @Test
    public void listarDocentesTabla_incluyeElDocenteCreado() throws Exception {
        String institutoNombre = PREFIJO + "InstitutoDocTabla";
        String nickname = PREFIJO + "docenteTabla";
        String mail = nickname + "@correo.uy";
        controller.altaInstituto(institutoNombre);
        controller.altaUsuario(nickname, mail, "Docente", "Tabla", LocalDate.of(1980, 1, 1), institutoNombre, null);

        List<String[]> tabla = controller.listarDocentesTabla();

        assertTrue(tabla.stream().anyMatch(fila -> fila[0].equals(nickname)));
    }

    @Test
    public void listarEstudiantesTabla_incluyeElEstudianteCreado() {
        String nickname = PREFIJO + "estudianteTabla";
        String mail = nickname + "@correo.uy";
        controller.altaUsuario(nickname, mail, "Estudiante", "Tabla", LocalDate.of(2002, 6, 10), null, null);

        List<String[]> tabla = controller.listarEstudiantesTabla();

        assertTrue(tabla.stream().anyMatch(fila -> fila[0].equals(nickname)));
    }

    // ==================================================================
    // Helpers privados para armar el escenario Instituto + Docente,
    // que varios tests de Curso/EdicionCurso/Programa necesitan.
    // ==================================================================

    private String crearInstitutoDePrueba(String sufijo) throws Exception {
        String nombre = PREFIJO + "Instituto" + sufijo;
        controller.altaInstituto(nombre);
        return nombre;
    }

    private String crearDocenteDePrueba(String sufijo, String institutoNombre) {
        String nickname = PREFIJO + "docente" + sufijo;
        String mail = nickname + "@correo.uy";
        controller.altaUsuario(nickname, mail, "Docente", sufijo, LocalDate.of(1980, 1, 1), institutoNombre, null);
        return nickname;
    }

    private String crearEstudianteDePrueba(String sufijo) {
        String nickname = PREFIJO + "estudiante" + sufijo;
        String mail = nickname + "@correo.uy";
        controller.altaUsuario(nickname, mail, "Estudiante", sufijo, LocalDate.of(2000, 1, 1), null, null);
        return nickname;
    }

    // ==================================================================
    // altaCurso / obtenerDataCurso
    // ==================================================================

    @Test
    public void altaCurso_creaCursoYObtenerDataCursoLoEncuentra() throws Exception {
        String instituto = crearInstitutoDePrueba("Curso1");
        String docente = crearDocenteDePrueba("Curso1", instituto);
        String nombreCurso = PREFIJO + "CursoJava";

        controller.altaCurso(nombreCurso, "Curso de Java", 4, 60f, 8, "http://curso.uy", instituto, docente, null);

        String[] datos = controller.obtenerDataCurso(nombreCurso);

        assertEquals(nombreCurso, datos[0]);
        assertEquals("Curso de Java", datos[1]);
        assertTrue(datos[8].contains(docente));
    }

    @Test
    public void altaCurso_lanzaExcepcion_siYaExisteCursoConEseNombre() throws Exception {
        String instituto = crearInstitutoDePrueba("CursoDup");
        String docente = crearDocenteDePrueba("CursoDup", instituto);
        String nombreCurso = PREFIJO + "CursoDuplicado";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        Exception ex = assertThrows(Exception.class, () ->
                controller.altaCurso(nombreCurso, "Desc2", 2, 30f, 4, "url", instituto, docente, null));

        assertTrue(ex.getMessage().contains("Ya existe"));
    }

    @Test
    public void altaCurso_lanzaExcepcion_siElInstitutoNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.altaCurso(PREFIJO + "CursoSinInst", "Desc", 2, 30f, 4, "url",
                        PREFIJO + "InstitutoQueNoExiste", PREFIJO + "docenteX", null));

        assertTrue(ex.getMessage().contains("instituto"));
    }

    @Test
    public void obtenerDataCurso_lanzaExcepcion_siNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.obtenerDataCurso(PREFIJO + "CursoQueNoExiste"));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    @Test
    public void listarNombresCursos_incluyeElCursoCreado() throws Exception {
        String instituto = crearInstitutoDePrueba("CursoNombres");
        String docente = crearDocenteDePrueba("CursoNombres", instituto);
        String nombreCurso = PREFIJO + "CursoParaListar";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        List<String> nombres = controller.listarNombresCursos();

        assertTrue(nombres.contains(nombreCurso));
    }

    @Test
    public void listarCursosPorInstituto_incluyeElCursoCreado() throws Exception {
        String instituto = crearInstitutoDePrueba("CursoPorInst");
        String docente = crearDocenteDePrueba("CursoPorInst", instituto);
        String nombreCurso = PREFIJO + "CursoDeEsteInstituto";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        List<String> cursos = controller.listarCursosPorInstituto(instituto);

        assertTrue(cursos.contains(nombreCurso));
    }

    @Test
    public void listarCursosTabla_incluyeElCursoCreado() throws Exception {
        String instituto = crearInstitutoDePrueba("CursoTabla");
        String docente = crearDocenteDePrueba("CursoTabla", instituto);
        String nombreCurso = PREFIJO + "CursoTablaX";
        controller.altaCurso(nombreCurso, "Descripcion tabla", 2, 30f, 4, "url", instituto, docente, null);

        List<String[]> tabla = controller.listarCursosTabla(instituto);

        assertTrue(tabla.stream().anyMatch(fila -> fila[0].equals(nombreCurso)));
    }

    @Test
    public void listarDocentesPorInstituto_incluyeElDocenteCreado() throws Exception {
        String instituto = crearInstitutoDePrueba("DocPorInst");
        String docente = crearDocenteDePrueba("DocPorInst", instituto);

        List<String[]> docentes = controller.listarDocentesPorInstituto(instituto);

        assertTrue(docentes.stream().anyMatch(fila -> fila[0].equals(docente)));
    }

    @Test
    public void obtenerDataDocente_incluyeElCursoQueDicta() throws Exception {
        String instituto = crearInstitutoDePrueba("DataDocente");
        String docente = crearDocenteDePrueba("DataDocente", instituto);
        String nombreCurso = PREFIJO + "CursoDelDocente";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        List<String> data = controller.obtenerDataDocente(docente);

        assertTrue(data.contains("- " + nombreCurso));
        assertTrue(data.contains("(Sin ediciones asignadas)"));
        assertTrue(data.contains("(Sin programas vinculados)"));
    }

    // ==================================================================
    // ProgramaFormacion: crearPrograma / modificarPorgrama / consultas
    // ==================================================================

    @Test
    public void crearPrograma_loCreaYObtenerDatosBasicosLoEncuentra() throws Exception {
        String nombre = PREFIJO + "ProgramaBasico";

        controller.crearPrograma(nombre, "Descripcion del programa",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 12, 1), LocalDate.now());

        String[] datos = controller.obtenerDatosBasicosPrograma(nombre);

        assertNotNull(datos);
        assertEquals(nombre, datos[0]);
        assertEquals("Descripcion del programa", datos[1]);
    }

    @Test
    public void crearPrograma_lanzaExcepcion_siYaExisteUnoConEseNombre() throws Exception {
        String nombre = PREFIJO + "ProgramaDuplicado";
        controller.crearPrograma(nombre, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        Exception ex = assertThrows(Exception.class, () ->
                controller.crearPrograma(nombre, "Otra desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now()));

        assertTrue(ex.getMessage().contains("Ya existe"));
    }

    @Test
    public void obtenerDatosBasicosPrograma_devuelveNull_siNoExiste() {
        String[] datos = controller.obtenerDatosBasicosPrograma(PREFIJO + "ProgramaQueNoExiste");

        assertNull(datos);
    }

    @Test
    public void modificarPorgrama_actualizaLosDatos() throws Exception {
        String nombre = PREFIJO + "ProgramaAModificar";
        controller.crearPrograma(nombre, "Desc vieja", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), LocalDate.now());

        controller.modificarPorgrama(nombre, "Desc nueva", LocalDate.of(2026, 2, 1), LocalDate.of(2026, 7, 1));

        String[] datos = controller.obtenerDatosBasicosPrograma(nombre);
        assertEquals("Desc nueva", datos[1]);
        assertEquals("2026-02-01", datos[2]);
        assertEquals("2026-07-01", datos[3]);
    }

    @Test
    public void modificarPorgrama_lanzaExcepcion_siNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.modificarPorgrama(PREFIJO + "ProgramaQueNoExiste", "Desc", LocalDate.now(), LocalDate.now()));

        assertTrue(ex.getMessage().contains("No existe"));
    }

    @Test
    public void existePrograma_devuelveTrueYFalse() throws Exception {
        String nombre = PREFIJO + "ProgramaExiste";
        controller.crearPrograma(nombre, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        assertTrue(controller.existePrograma(nombre));
        assertFalse(controller.existePrograma(PREFIJO + "ProgramaQueNoExiste"));
    }

    @Test
    public void listarNombreProgramas_incluyeElCreado() throws Exception {
        String nombre = PREFIJO + "ProgramaEnListado";
        controller.crearPrograma(nombre, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        List<String> nombres = controller.listarNombreProgramas();

        assertTrue(nombres.contains(nombre));
    }

    @Test
    public void listarProgramasTabla_incluyeElCreado() throws Exception {
        String nombre = PREFIJO + "ProgramaEnTabla";
        controller.crearPrograma(nombre, "Descripcion en tabla", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        List<String[]> tabla = controller.listarProgramasTabla();

        assertTrue(tabla.stream().anyMatch(fila -> fila[0].equals(nombre)));
    }

    @Test
    public void obtenerDataPrograma_lanzaExcepcion_siNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.obtenerDataPrograma(PREFIJO + "ProgramaQueNoExiste"));

        assertTrue(ex.getMessage().contains("No existe"));
    }

    @Test
    public void obtenerDataPrograma_devuelveSinCursos_siNoTieneNinguno() throws Exception {
        String nombre = PREFIJO + "ProgramaSinCursos";
        controller.crearPrograma(nombre, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        List<String> data = controller.obtenerDataPrograma(nombre);

        assertTrue(data.contains("Sin Cursos Registrados"));
    }

    // ==================================================================
    // agregarCursoPrograma (necesita Instituto + Docente + Curso + Programa)
    // ==================================================================

    @Test
    public void agregarCursoPrograma_agregaElCursoYSeReflejaEnAmbasConsultas() throws Exception {
        String instituto = crearInstitutoDePrueba("AgregarProg");
        String docente = crearDocenteDePrueba("AgregarProg", instituto);
        String nombreCurso = PREFIJO + "CursoParaPrograma";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        String nombrePrograma = PREFIJO + "ProgramaConCurso";
        controller.crearPrograma(nombrePrograma, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        controller.agregarCursoPrograma(nombrePrograma, nombreCurso);

        List<String> dataPrograma = controller.obtenerDataPrograma(nombrePrograma);
        assertTrue(dataPrograma.contains("- " + nombreCurso));

        List<String> programasDelCurso = controller.listarProgramasPorCurso(nombreCurso);
        assertTrue(programasDelCurso.contains(nombrePrograma));

        // Además, el docente de ese curso ahora debería ver el programa
        // en obtenerDataDocente (rama "programas" no vacía).
        List<String> dataDocente = controller.obtenerDataDocente(docente);
        assertTrue(dataDocente.contains("- " + nombrePrograma));
    }

    @Test
    public void agregarCursoPrograma_lanzaExcepcion_siElProgramaNoExiste() throws Exception {
        String instituto = crearInstitutoDePrueba("ProgNoExiste");
        String docente = crearDocenteDePrueba("ProgNoExiste", instituto);
        String nombreCurso = PREFIJO + "CursoSueltoo";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        Exception ex = assertThrows(Exception.class, () ->
                controller.agregarCursoPrograma(PREFIJO + "ProgramaQueNoExiste", nombreCurso));

        assertTrue(ex.getMessage().contains("No existe un programa"));
    }

    @Test
    public void agregarCursoPrograma_lanzaExcepcion_siElCursoNoExiste() throws Exception {
        String nombrePrograma = PREFIJO + "ProgramaSinCurso";
        controller.crearPrograma(nombrePrograma, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());

        Exception ex = assertThrows(Exception.class, () ->
                controller.agregarCursoPrograma(nombrePrograma, PREFIJO + "CursoQueNoExiste"));

        assertTrue(ex.getMessage().contains("No existe un curso"));
    }

    @Test
    public void agregarCursoPrograma_lanzaExcepcion_siElCursoYaEstaEnElPrograma() throws Exception {
        String instituto = crearInstitutoDePrueba("ProgRepetido");
        String docente = crearDocenteDePrueba("ProgRepetido", instituto);
        String nombreCurso = PREFIJO + "CursoRepetido";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        String nombrePrograma = PREFIJO + "ProgramaRepetido";
        controller.crearPrograma(nombrePrograma, "Desc", LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now());
        controller.agregarCursoPrograma(nombrePrograma, nombreCurso); // primera vez: ok

        Exception ex = assertThrows(Exception.class, () ->
                controller.agregarCursoPrograma(nombrePrograma, nombreCurso));

        assertTrue(ex.getMessage().contains("ya se encuentra"));
    }

    // ==================================================================
    // altaEdicionCurso / obtenerEdicionCurso / listarEdicionesCurso /
    // obtenerEdicionVigente
    // ==================================================================

    @Test
    public void altaEdicionCurso_creaEdicionYObtenerEdicionCursoLaEncuentra() throws Exception {
        String instituto = crearInstitutoDePrueba("Edicion1");
        String docente = crearDocenteDePrueba("Edicion1", instituto);
        String nombreCurso = PREFIJO + "CursoConEdicion";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        String nombreEdicion = PREFIJO + "Edicion2026A";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 6, 1), 20, new ArrayList<>());

        String[] datos = controller.obtenerEdicionCurso(nombreEdicion);

        assertEquals(nombreEdicion, datos[0]);
        assertEquals(nombreCurso, datos[1]);
        assertEquals("20", datos[4]);
    }

    @Test
    public void altaEdicionCurso_lanzaExcepcion_siYaExisteUnaConEseNombre() throws Exception {
        String instituto = crearInstitutoDePrueba("EdicionDup");
        String docente = crearDocenteDePrueba("EdicionDup", instituto);
        String nombreCurso = PREFIJO + "CursoEdicionDup";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        String nombreEdicion = PREFIJO + "EdicionDuplicada";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 10, new ArrayList<>());

        Exception ex = assertThrows(Exception.class, () ->
                controller.altaEdicionCurso(nombreEdicion, nombreCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 10, new ArrayList<>()));

        assertTrue(ex.getMessage().contains("Ya existe"));
    }

    @Test
    public void altaEdicionCurso_lanzaExcepcion_siElCursoNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.altaEdicionCurso(PREFIJO + "EdicionSinCurso", PREFIJO + "CursoQueNoExiste",
                        LocalDate.now(), LocalDate.now().plusMonths(1), 10, new ArrayList<>()));

        assertTrue(ex.getMessage().contains("curso"));
    }

    @Test
    public void altaEdicionCurso_lanzaExcepcion_siFechaFinEsAnteriorAFechaInicio() throws Exception {
        String instituto = crearInstitutoDePrueba("EdicionFechas");
        String docente = crearDocenteDePrueba("EdicionFechas", instituto);
        String nombreCurso = PREFIJO + "CursoFechas";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);

        Exception ex = assertThrows(Exception.class, () ->
                controller.altaEdicionCurso(PREFIJO + "EdicionFechaInvalida", nombreCurso,
                        LocalDate.of(2026, 6, 1), LocalDate.of(2026, 1, 1), 10, new ArrayList<>()));

        assertTrue(ex.getMessage().contains("fecha"));
    }

    @Test
    public void obtenerEdicionCurso_lanzaExcepcion_siNoExiste() {
        Exception ex = assertThrows(Exception.class, () ->
                controller.obtenerEdicionCurso(PREFIJO + "EdicionQueNoExiste"));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    @Test
    public void listarEdicionesCurso_incluyeLaEdicionCreada() throws Exception {
        String instituto = crearInstitutoDePrueba("EdicionesListado");
        String docente = crearDocenteDePrueba("EdicionesListado", instituto);
        String nombreCurso = PREFIJO + "CursoConEdicionesListado";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        String nombreEdicion = PREFIJO + "EdicionListada";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 10, new ArrayList<>());

        List<String> ediciones = controller.listarEdicionesCurso(nombreCurso);

        assertTrue(ediciones.contains(nombreEdicion));
    }

    @Test
    public void obtenerEdicionVigente_devuelveElNombre_siHoyEstaDentroDelRango() throws Exception {
        String instituto = crearInstitutoDePrueba("EdicionVigente");
        String docente = crearDocenteDePrueba("EdicionVigente", instituto);
        String nombreCurso = PREFIJO + "CursoVigente";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        String nombreEdicion = PREFIJO + "EdicionVigenteHoy";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), 10, new ArrayList<>());

        String vigente = controller.obtenerEdicionVigente(nombreCurso);

        assertEquals(nombreEdicion, vigente);
    }

    @Test
    public void obtenerEdicionVigente_devuelveNull_siNoHayNingunaVigente() throws Exception {
        String instituto = crearInstitutoDePrueba("EdicionNoVigente");
        String docente = crearDocenteDePrueba("EdicionNoVigente", instituto);
        String nombreCurso = PREFIJO + "CursoNoVigente";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        String nombreEdicion = PREFIJO + "EdicionYaTermino";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(20), 10, new ArrayList<>());

        String vigente = controller.obtenerEdicionVigente(nombreCurso);

        assertNull(vigente);
    }

    // ==================================================================
    // inscribirEstudianteEdicion / obtenerEdicionesYProgramas
    // ==================================================================

    @Test
    public void inscribirEstudianteEdicion_creaLaInscripcionYApareceEnObtenerEdicionesYProgramas() throws Exception {
        String instituto = crearInstitutoDePrueba("Inscripcion1");
        String docente = crearDocenteDePrueba("Inscripcion1", instituto);
        String nombreCurso = PREFIJO + "CursoInscripcion";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        String nombreEdicion = PREFIJO + "EdicionInscripcion";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 10, new ArrayList<>());
        String nickEstudiante = crearEstudianteDePrueba("Inscripcion1");
        String mailEstudiante = nickEstudiante + "@correo.uy";

        controller.inscribirEstudianteEdicion(nickEstudiante, mailEstudiante, nombreEdicion, LocalDate.now());

        List<String> resultado = controller.obtenerEdicionesYProgramas(nickEstudiante);
        assertTrue(resultado.contains(nombreEdicion));
    }

    @Test
    public void inscribirEstudianteEdicion_lanzaExcepcion_siYaEstaInscripto() throws Exception {
        String instituto = crearInstitutoDePrueba("InscripcionDup");
        String docente = crearDocenteDePrueba("InscripcionDup", instituto);
        String nombreCurso = PREFIJO + "CursoInscripcionDup";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        String nombreEdicion = PREFIJO + "EdicionInscripcionDup";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 10, new ArrayList<>());
        String nickEstudiante = crearEstudianteDePrueba("InscripcionDup");
        String mailEstudiante = nickEstudiante + "@correo.uy";
        controller.inscribirEstudianteEdicion(nickEstudiante, mailEstudiante, nombreEdicion, LocalDate.now());

        Exception ex = assertThrows(Exception.class, () ->
                controller.inscribirEstudianteEdicion(nickEstudiante, mailEstudiante, nombreEdicion, LocalDate.now()));

        assertTrue(ex.getMessage().contains("ya está inscripto"));
    }

    @Test
    public void inscribirEstudianteEdicion_lanzaExcepcion_siNoHayCupo() throws Exception {
        String instituto = crearInstitutoDePrueba("InscripcionCupo");
        String docente = crearDocenteDePrueba("InscripcionCupo", instituto);
        String nombreCurso = PREFIJO + "CursoSinCupo";
        controller.altaCurso(nombreCurso, "Desc", 2, 30f, 4, "url", instituto, docente, null);
        // Cupo = 1: el primer estudiante entra, el segundo no.
        String nombreEdicion = PREFIJO + "EdicionCupoUno";
        controller.altaEdicionCurso(nombreEdicion, nombreCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 1, new ArrayList<>());

        String nick1 = crearEstudianteDePrueba("Cupo1");
        controller.inscribirEstudianteEdicion(nick1, nick1 + "@correo.uy", nombreEdicion, LocalDate.now());

        String nick2 = crearEstudianteDePrueba("Cupo2");
        Exception ex = assertThrows(Exception.class, () ->
                controller.inscribirEstudianteEdicion(nick2, nick2 + "@correo.uy", nombreEdicion, LocalDate.now()));

        assertTrue(ex.getMessage().contains("No hay cupos"));
    }

    @Test
    public void inscribirEstudianteEdicion_lanzaExcepcion_siLaEdicionNoExiste() {
        String nick = crearEstudianteDePrueba("EdicionInexistente");

        Exception ex = assertThrows(Exception.class, () ->
                controller.inscribirEstudianteEdicion(nick, nick + "@correo.uy", PREFIJO + "EdicionQueNoExiste", LocalDate.now()));

        assertTrue(ex.getMessage().contains("no existe"));
    }
}
