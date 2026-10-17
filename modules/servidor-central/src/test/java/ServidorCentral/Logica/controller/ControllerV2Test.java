package ServidorCentral.Logica.controller;

import ServidorCentral.Logica.datatypes.*;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.services.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de datos del controller: su unico trabajo es pasarle cada pedido al servicio que corresponde
 * y devolver lo que el servicio responde. Cada servicio es un "espia" que anota que metodo se llamo y con que datos.
 */
class ControllerV2Test {

    private static final LocalDate F1 = LocalDate.of(2026, 3, 1);
    private static final LocalDate F2 = LocalDate.of(2026, 7, 1);
    private static final List<String> NOMBRES = List.of("uno", "dos");

    private final List<String> llamadas = new ArrayList<>();

    private void anotar(String llamada) {
        llamadas.add(llamada);
    }

    private final IControllerV2 controller = new ControllerV2(new ControllerV2.Servicios(
            new UsuarioService(null, null, null) {
                @Override
                public boolean nicknameDisponible(String nickname) { anotar("nicknameDisponible " + nickname); return true; }
                @Override
                public boolean mailDisponible(String mail) { anotar("mailDisponible " + mail); return false; }
                @Override
                public void registrar(String n, String m, String no, String a, LocalDate f, String i, String img, String c) { anotar("registrar(ruta) " + n + " " + img); }
                @Override
                public void registrar(String n, String m, String no, String a, LocalDate f, String i, DTImagen img, String c) { anotar("registrar(imagen) " + n + " " + i); }
                @Override
                public void modificar(String n, String m, String no, String a, LocalDate f) { anotar("modificar " + n + " " + no); }
                @Override
                public List<DTUsuarioResumen> listarUsuariosTabla() { anotar("listarUsuariosTabla"); return List.of(new DTUsuarioResumen("weiss", "w@x")); }
                @Override
                public DTUsuario obtenerDataUsuario(String n, String m) { anotar("obtenerDataUsuario " + n + " " + m); return null; }
                @Override
                public boolean esDocente(String n) { anotar("esDocente " + n); return true; }
                @Override
                public DTSesion autenticar(String id, String c) { anotar("autenticar " + id + " " + c); return null; }
                @Override
                public DTUsuario obtenerPorNickname(String n) { anotar("obtenerPorNickname " + n); return null; }
                @Override
                public void modificarDatos(String n, String no, String a, LocalDate f) { anotar("modificarDatos " + n + " " + no + " " + a + " " + f); }
            },
            new DocenteService(null) {
                @Override
                public DTMisCursos misCursos(String n) { anotar("misCursos " + n); return null; }
                @Override
                public DTDocenteDetalle obtenerData(String n) { anotar("obtenerData " + n); return null; }
                @Override
                public List<DTDocenteResumen> listarDocentesTabla() { anotar("listarDocentesTabla"); return List.of(); }
                @Override
                public List<DTDocenteResumen> porInstitutoTabla(String i) { anotar("porInstitutoTabla " + i); return List.of(); }
            },
            new EstudianteService(null) {
                @Override
                public DTInscripcionesEstudiante obtenerEdicionesYProgramas(String n) { anotar("obtenerEdicionesYProgramas " + n); return null; }
                @Override
                public List<DTEstudianteResumen> listarEstudiantesTabla() { anotar("listarEstudiantesTabla"); return List.of(); }
                @Override
                public List<DTInscripcionEstudiante> listarInscripciones(String n) { anotar("listarInscripciones " + n); return List.of(); }
                @Override
                public List<DTProgramaInscripto> listarProgramas(String n) { anotar("listarProgramas " + n); return List.of(); }
                @Override
                public List<DTInscripcionEstudiante> resultadosInscripciones(String n) { anotar("resultadosInscripciones " + n); return List.of(); }
            },
            new CursoService(null, null, null) {
                @Override
                public List<DTCursoResumen> listarCursosInstituto(String i) { anotar("listarCursosInstituto " + i); return List.of(); }
                @Override
                public DTCursoDetalle consultarCurso(String c) { anotar("consultarCurso " + c); return null; }
                @Override
                public void altaCurso(String d, String n, String de, int du, float h, int cr, String u, String i, List<String> p, DTImagen img) {
                    anotar("altaCurso " + d + " " + n + " " + du + " " + h + " " + cr + " " + p);
                }
                @Override
                public void registrar(String n, String de, int du, float h, int cr, String u, String i, String d, List<String> p) { anotar("registrarCurso " + n + " " + d); }
                @Override
                public void registrar(String n, String de, int du, float h, int cr, String u, String i, String d, List<String> p, String img) { anotar("registrarCurso " + n + " " + img); }
                @Override
                public List<DTCursoResumen> obtenerCursosTabla(String i) { anotar("obtenerCursosTabla " + i); return List.of(); }
                @Override
                public List<String> listarNombres() { anotar("listarNombresCursos"); return NOMBRES; }
                @Override
                public List<String> listarNombresPorInstituto(String i) { anotar("listarNombresPorInstituto " + i); return NOMBRES; }
                @Override
                public DTCurso obtenerCurso(String c) { anotar("obtenerCurso " + c); return null; }
                @Override
                public List<DTCursoResumen> buscarCursos(String t) { anotar("buscarCursos " + t); return List.of(); }
            },
            new InstitutoService(null) {
                @Override
                public List<String> obtenerNombres() { anotar("obtenerNombresInstitutos"); return NOMBRES; }
                @Override
                public void registrar(String n) { anotar("registrarInstituto " + n); }
            },
            new EdicionCursoService(null, null, null, null) {
                @Override
                public DTEdicionDetalle consultarEdicion(String e, String quien) { anotar("consultarEdicion " + e + " " + quien); return null; }
                @Override
                public void inscribirse(String n, String e) { anotar("inscribirse " + n + " " + e); }
                @Override
                public void altaEdicion(String d, String n, String c, LocalDate fi, LocalDate ff, Integer cupo, List<String> docentes, DTImagen img) {
                    anotar("altaEdicion " + d + " " + n + " " + c + " " + cupo + " " + docentes);
                }
                @Override
                public List<DTDocenteResumen> listarDocentes(String e) { anotar("listarDocentes " + e); return List.of(); }
                @Override
                public List<DTEstudianteResumen> listarEstudiantes(String e) { anotar("listarEstudiantes " + e); return List.of(); }
                @Override
                public void registrar(String n, String c, LocalDate fi, LocalDate ff, int cupo, List<String> d) { anotar("registrarEdicion " + n + " " + cupo); }
                @Override
                public void registrar(String n, String c, LocalDate fi, LocalDate ff, int cupo, List<String> d, String img) { anotar("registrarEdicion " + n + " " + img); }
                @Override
                public List<String> obtenerNombres(String c) { anotar("obtenerNombresEdiciones " + c); return NOMBRES; }
                @Override
                public DTEdicionCurso obtenerPorNombre(String e) { anotar("obtenerPorNombre " + e); return null; }
                @Override
                public String obtenerEdicionVigente(String c) { anotar("obtenerEdicionVigente " + c); return "vigente"; }
                @Override
                public void inscribirEstudiante(String n, String m, String e, LocalDate f) { anotar("inscribirEstudiante " + n + " " + e + " " + f); }
                @Override
                public DTInscripcionesEdicion consultarInscripcionesEdicion(String d, String e) { anotar("consultarInscripcionesEdicion " + d + " " + e); return null; }
                @Override
                public DTInscripcionesEdicion consultarAceptadosEdicion(String d, String e) { anotar("consultarAceptadosEdicion " + d + " " + e); return null; }
                @Override
                public void seleccionarEstudiantes(String d, String e, Map<String, EstadoInscripcion> estados) { anotar("seleccionarEstudiantes " + d + " " + e + " " + estados); }
            },
            new ProgramaFormacionService(null, null, null) {
                @Override
                public void inscribirEstudiante(String n, String m, String p, LocalDate f) { anotar("inscribirPrograma " + n + " " + p); }
                @Override
                public void crear(String n, String d, LocalDate fi, LocalDate ff, LocalDate fa) { anotar("crearPrograma " + n); }
                @Override
                public void modificar(String n, String d, LocalDate fi, LocalDate ff) { anotar("modificarPrograma " + n + " " + d); }
                @Override
                public void agregarCurso(String p, String c) { anotar("agregarCurso " + p + " " + c); }
                @Override
                public DTProgramaFormacion obtenerPorNombre(String p) { anotar("obtenerPrograma " + p); return null; }
                @Override
                public List<String> listarNombres() { anotar("listarNombresProgramas"); return NOMBRES; }
                @Override
                public List<String> obtenerPorCurso(String c) { anotar("programasPorCurso " + c); return NOMBRES; }
                @Override
                public boolean existe(String p) { anotar("existePrograma " + p); return true; }
                @Override
                public DTProgramaFormacion obtenerDatosBasicos(String p) { anotar("datosBasicosPrograma " + p); return null; }
                @Override
                public List<DTProgramaResumen> listarProgramasTabla() { anotar("listarProgramasTabla"); return List.of(); }
            },
            new SeguimientoService(null, null) {
                @Override
                public int seguir(String a, String b) { anotar("seguir " + a + " " + b); return 3; }
                @Override
                public int dejarDeSeguir(String a, String b) { anotar("dejarDeSeguir " + a + " " + b); return 2; }
                @Override
                public boolean estaSiguiendo(String a, String b) { anotar("estaSiguiendo " + a + " " + b); return true; }
                @Override
                public List<String> listarSeguidos(String n) { anotar("listarSeguidos " + n); return NOMBRES; }
                @Override
                public List<String> listarSeguidores(String n) { anotar("listarSeguidores " + n); return NOMBRES; }
            },
            new PerfilService(null, null, null, null) {
                @Override
                public List<DTUsuario> listarUsuarios() { anotar("listarUsuarios"); return List.of(); }
                @Override
                public DTPerfil obtenerPerfil(String c, String quien) { anotar("obtenerPerfil " + c + " " + quien); return null; }
            }));

    @Test
    void usuarios_vanAlServicioDeUsuarios() throws Exception {
        assertTrue(controller.nicknameDisponible("weiss"));
        assertFalse(controller.mailDisponible("w@x"));
        controller.altaUsuario("roro", "r@x", "Rodrigo", "Cotelo", F1, "", "foto.jpg", "1234");
        controller.altaUsuario("house", "h@x", "Gregory", "House", F1, "Medicina", (DTImagen) null, "1234");
        controller.modificarUsuario("roro", "r@x", "Rodri", "Cotelo", F1);
        assertEquals("weiss", controller.listarUsuariosTabla().get(0).nickname());
        controller.obtenerDataUsuario("roro", "r@x");
        assertTrue(controller.esDocente("house"));
        controller.iniciarSesion("roro", "1234");
        controller.obtenerUsuario("roro");

        assertEquals(List.of(
                "nicknameDisponible weiss", "mailDisponible w@x", "registrar(ruta) roro foto.jpg",
                "registrar(imagen) house Medicina", "modificar roro Rodri", "listarUsuariosTabla",
                "obtenerDataUsuario roro r@x", "esDocente house", "autenticar roro 1234", "obtenerPorNickname roro"), llamadas);
    }

    @Test
    void opcionalesWeb_resultadosModificarYBuscar_vanASusServicios() {
        controller.resultadosInscripciones("roro");
        controller.modificarDatosUsuario("roro", "Rodri", "Cotelo", F1);
        controller.buscarCursos("micro");

        assertEquals(List.of("resultadosInscripciones roro", "modificarDatos roro Rodri Cotelo 2026-03-01",
                "buscarCursos micro"), llamadas);
    }

    @Test
    void perfilDocentesYEstudiantes_vanASusServicios() {
        controller.listarUsuarios();
        controller.obtenerPerfil("roro", "weiss");
        controller.misCursosDocente("house");
        controller.obtenerDataDocente("house");
        controller.listarDocentesTabla();
        controller.listarDocentesPorInstituto("Medicina");
        controller.obtenerEdicionesYProgramas("roro");
        controller.listarEstudiantesTabla();
        controller.listarInscripcionesEstudiante("roro");
        controller.listarProgramasEstudiante("roro");

        assertEquals(List.of(
                "listarUsuarios", "obtenerPerfil roro weiss", "misCursos house", "obtenerData house",
                "listarDocentesTabla", "porInstitutoTabla Medicina", "obtenerEdicionesYProgramas roro",
                "listarEstudiantesTabla", "listarInscripciones roro", "listarProgramas roro"), llamadas);
    }

    @Test
    void institutosYCursos_vanASusServicios() throws Exception {
        controller.altaInstituto("Medicina");
        assertSame(NOMBRES, controller.listarNombresInstitutos());
        assertSame(NOMBRES, controller.listarInstitutos());
        controller.listarCursosInstituto("Medicina");
        controller.consultarCurso("Komikan");
        controller.altaCurso("house", "Anatomia", "desc", 4, 60f, 8, "http://x", "Medicina", List.of("Komikan"), null);
        controller.altaCurso("Anatomia", "desc", 4, 60f, 8, "http://x", "Medicina", "house", List.of());
        controller.altaCursoConImagen("Anatomia", "desc", 4, 60f, 8, "http://x", "Medicina", "house", List.of(), "a.png");
        controller.listarCursosTabla("Medicina");
        assertSame(NOMBRES, controller.listarNombresCursos());
        assertSame(NOMBRES, controller.listarCursosPorInstituto("Medicina"));
        controller.obtenerDataCurso("Komikan");

        assertEquals(List.of(
                "registrarInstituto Medicina", "obtenerNombresInstitutos", "obtenerNombresInstitutos",
                "listarCursosInstituto Medicina", "consultarCurso Komikan", "altaCurso house Anatomia 4 60.0 8 [Komikan]",
                "registrarCurso Anatomia house", "registrarCurso Anatomia a.png", "obtenerCursosTabla Medicina",
                "listarNombresCursos", "listarNombresPorInstituto Medicina", "obtenerCurso Komikan"), llamadas);
    }

    @Test
    void ediciones_vanAlServicioDeEdiciones() throws Exception {
        controller.consultarEdicion("MicroBit-2026", "roro");
        controller.inscribirse("roro", "MicroBit-2026");
        controller.altaEdicion("house", "Anatomia-2026", "Anatomia", F1, F2, 30, List.of("house"), null);
        controller.listarDocentesEdicion("MicroBit-2026");
        controller.listarEstudiantesEdicion("MicroBit-2026");
        controller.altaEdicionCurso("Anatomia-2026", "Anatomia", F1, F2, 30, List.of("house"));
        controller.altaEdicionCurso("Anatomia-2026", "Anatomia", F1, F2, 30, List.of("house"), "e.png");
        assertSame(NOMBRES, controller.listarEdicionesCurso("Anatomia"));
        controller.obtenerEdicionCurso("Anatomia-2026");
        assertEquals("vigente", controller.obtenerEdicionVigente("Anatomia"));
        controller.inscribirEstudianteEdicion("roro", "r@x", "Anatomia-2026", F1);
        controller.consultarInscripcionesEdicion("house", "Anatomia-2026");
        controller.consultarAceptadosEdicion("house", "Anatomia-2026");
        controller.seleccionarEstudiantes("house", "Anatomia-2026", Map.of("roro", EstadoInscripcion.ACEPTADA));

        assertEquals(List.of(
                "consultarEdicion MicroBit-2026 roro", "inscribirse roro MicroBit-2026",
                "altaEdicion house Anatomia-2026 Anatomia 30 [house]", "listarDocentes MicroBit-2026",
                "listarEstudiantes MicroBit-2026", "registrarEdicion Anatomia-2026 30", "registrarEdicion Anatomia-2026 e.png",
                "obtenerNombresEdiciones Anatomia", "obtenerPorNombre Anatomia-2026", "obtenerEdicionVigente Anatomia",
                "inscribirEstudiante roro Anatomia-2026 " + F1, "consultarInscripcionesEdicion house Anatomia-2026",
                "consultarAceptadosEdicion house Anatomia-2026", "seleccionarEstudiantes house Anatomia-2026 {roro=ACEPTADA}"),
                llamadas);
    }

    @Test
    void seguimientos_devuelvenLoQueDiceElServicio() {
        assertEquals(3, controller.seguirUsuario("roro", "weiss"));
        assertEquals(2, controller.dejarDeSeguirUsuario("roro", "weiss"));
        assertTrue(controller.estaSiguiendo("roro", "weiss"));
        assertSame(NOMBRES, controller.listarSeguidos("roro"));
        assertSame(NOMBRES, controller.listarSeguidores("roro"));

        assertEquals(List.of("seguir roro weiss", "dejarDeSeguir roro weiss", "estaSiguiendo roro weiss",
                "listarSeguidos roro", "listarSeguidores roro"), llamadas);
    }

    @Test
    void programas_vanAlServicioDeProgramas() throws Exception {
        controller.inscribirEstudiantePrograma("roro", "r@x", "EFI Robótica", F1);
        controller.crearPrograma("EFI Robótica", "desc", F1, F2, F1);
        controller.modificarPorgrama("EFI Robótica", "nueva", F1, F2);
        controller.agregarCursoPrograma("EFI Robótica", "MicroBit");
        controller.obtenerDataPrograma("EFI Robótica");
        assertSame(NOMBRES, controller.listarNombreProgramas());
        assertSame(NOMBRES, controller.listarProgramasPorCurso("MicroBit"));
        assertTrue(controller.existePrograma("EFI Robótica"));
        controller.obtenerDatosBasicosPrograma("EFI Robótica");
        controller.listarProgramasTabla();

        assertEquals(List.of(
                "inscribirPrograma roro EFI Robótica", "crearPrograma EFI Robótica", "modificarPrograma EFI Robótica nueva",
                "agregarCurso EFI Robótica MicroBit", "obtenerPrograma EFI Robótica", "listarNombresProgramas",
                "programasPorCurso MicroBit", "existePrograma EFI Robótica", "datosBasicosPrograma EFI Robótica",
                "listarProgramasTabla"), llamadas);
    }

    @Test
    void fabrica_esUnicaYNoDaElControllerViejoComoV2() {
        assertSame(Fabrica.getInstance(), Fabrica.getInstance());
        assertThrows(UnsupportedOperationException.class,
                () -> Fabrica.getInstance().getUserControler(ControllerVersion.v2));
    }
}
