package ServidorCentral.WebServices;

import ServidorCentral.DataTypes.DTCurso;
import ServidorCentral.DataTypes.DTCursoDetalle;
import ServidorCentral.DataTypes.DTDocenteResumen;
import ServidorCentral.DataTypes.DTEdicionCurso;
import ServidorCentral.DataTypes.DTEdicionDetalle;
import ServidorCentral.DataTypes.DTInscripcionEdicion;
import ServidorCentral.DataTypes.DTInscripcionEstudiante;
import ServidorCentral.DataTypes.DTInscripcionesEdicion;
import ServidorCentral.DataTypes.DTMisCursos;
import ServidorCentral.DataTypes.DTCursoResumen;
import ServidorCentral.DataTypes.DTPerfil;
import ServidorCentral.DataTypes.DTProgramaInscripto;
import ServidorCentral.DataTypes.DTSesion;
import ServidorCentral.DataTypes.DTUsuario;
import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.seguridad.Rol;
import ServidorCentral.WebServices.dto.DTCursoDetalleWS;
import ServidorCentral.WebServices.dto.DTImagenWS;
import ServidorCentral.WebServices.dto.DTInscripcionesEdicionWS;
import ServidorCentral.WebServices.dto.DTMisCursosWS;
import ServidorCentral.WebServices.dto.DTPerfilWS;
import ServidorCentral.WebServices.dto.DTSeleccionWS;
import ServidorCentral.WebServices.dto.DTSesionWS;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** Los DTOs del contrato SOAP se serializan con JAXB y los adaptadores copian bien los DataTypes. */
class ContratoSoapTest {

    private static final DTDocenteResumen DOCENTE = new DTDocenteResumen("doc1", "Ana", "Perez");
    private static final DTEdicionCurso EDICION = new DTEdicionCurso("Java 2026", "Java",
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 1), 20, LocalDate.of(2026, 2, 1), "ediciones/java.jpg");

    /** Serializa a XML y vuelve a leer, como pasa en un pedido SOAP. */
    @SuppressWarnings("unchecked")
    private static <T> T idaYVuelta(T objeto) throws JAXBException {
        Class<T> tipo = (Class<T>) objeto.getClass();
        JAXBContext ctx = JAXBContext.newInstance(tipo);
        StringWriter xml = new StringWriter();
        ctx.createMarshaller().marshal(new JAXBElement<>(new QName("x"), tipo, objeto), xml);
        return ctx.createUnmarshaller().unmarshal(new StreamSource(new StringReader(xml.toString())), tipo).getValue();
    }

    @Test
    void cursoDetalleConFechasComoTexto() throws JAXBException {
        DTCurso curso = new DTCurso("Java", "Intro", 8, 90f, 6, "http://x", LocalDate.of(2026, 1, 15),
                "INCO", DOCENTE, List.of("Prog 1"), "cursos/java.jpg");
        DTCursoDetalleWS ws = idaYVuelta(DTCursoDetalleWS.de(new DTCursoDetalle(curso, List.of(EDICION), List.of("EFI"))));

        assertEquals("Java", ws.curso.nombre);
        assertEquals("2026-01-15", ws.curso.fechaRegistro);
        assertEquals("doc1", ws.curso.docente.nickname);
        assertEquals(List.of("Prog 1"), ws.curso.previas);
        assertEquals("2026-03-01", ws.ediciones.get(0).fechaInicio);
        assertEquals(List.of("EFI"), ws.programas);
    }

    @Test
    void cursoSinDocenteNiFecha() throws JAXBException {
        DTCurso curso = new DTCurso("Java", "Intro", 8, 90f, 6, null, null, "INCO", null, null);
        DTCursoDetalleWS ws = idaYVuelta(DTCursoDetalleWS.de(new DTCursoDetalle(curso, List.of(), List.of())));

        assertNull(ws.curso.docente);
        assertNull(ws.curso.fechaRegistro);
        assertTrue(ws.ediciones.isEmpty());
    }

    @Test
    void perfilConEnumsYListas() throws JAXBException {
        DTUsuario usuario = new DTUsuario("est1", "e@x.com", "Eva", "Lopez", LocalDate.of(2000, 5, 2), null);
        DTPerfil perfil = new DTPerfil(usuario, Rol.ESTUDIANTE, true, true, List.of(), List.of(EDICION),
                List.of(new DTInscripcionEstudiante("Java 2026", "Java", LocalDate.of(2026, 2, 10), EstadoInscripcion.ACEPTADA)),
                List.of(new DTProgramaInscripto("EFI", LocalDate.of(2026, 2, 11))),
                List.of("seg1"), List.of(), false, false);
        DTPerfilWS ws = idaYVuelta(DTPerfilWS.de(perfil));

        assertEquals(Rol.ESTUDIANTE, ws.rol);
        assertEquals("2000-05-02", ws.usuario.fechaNac);
        assertEquals(EstadoInscripcion.ACEPTADA, ws.inscripciones.get(0).estado);
        assertEquals("2026-02-11", ws.programas.get(0).fechaInscripcion);
        assertEquals(List.of("seg1"), ws.seguidores);
        assertTrue(ws.propio);
    }

    @Test
    void inscripcionesDeEdicionConEstadoNulo() throws JAXBException {
        DTEdicionDetalle detalle = new DTEdicionDetalle(EDICION, "INCO", List.of(DOCENTE), true, null, false);
        DTInscripcionesEdicion insc = new DTInscripcionesEdicion(detalle, List.of(
                new DTInscripcionEdicion("est1", "e@x.com", "Eva", "Lopez", LocalDate.of(2026, 2, 10), EstadoInscripcion.INSCRIPTO, null)));
        DTInscripcionesEdicionWS ws = idaYVuelta(DTInscripcionesEdicionWS.de(insc));

        assertNull(ws.edicion.miInscripcion);
        assertTrue(ws.edicion.vigente);
        assertEquals("doc1", ws.edicion.docentes.get(0).nickname);
        assertEquals(EstadoInscripcion.INSCRIPTO, ws.inscripciones.get(0).estado);
    }

    @Test
    void misCursosConClaseAnidada() throws JAXBException {
        DTMisCursos mis = new DTMisCursos(List.of(new DTCursoResumen("Java", "Intro", null)),
                List.of(new DTMisCursos.Edicion(EDICION, true)));
        DTMisCursosWS ws = idaYVuelta(DTMisCursosWS.de(mis));

        assertEquals("Java", ws.cursos.get(0).nombre);
        assertEquals("Java 2026", ws.ediciones.get(0).edicion.nombre);
        assertTrue(ws.ediciones.get(0).vigente);
    }

    @Test
    void sesionDesdeClaseConGetters() throws JAXBException {
        DTSesionWS ws = idaYVuelta(DTSesionWS.de(new DTSesion("doc1", "d@x.com", "Ana", "Perez", null, Rol.DOCENTE)));

        assertEquals("doc1", ws.nickname);
        assertEquals(Rol.DOCENTE, ws.rol);
        assertNull(DTSesionWS.de(null));
    }

    @Test
    void imagenViajaEnBase64() throws JAXBException {
        DTImagenWS img = new DTImagenWS();
        img.contenido = new byte[]{1, 2, 3};
        img.extension = ".PNG";
        DTImagenWS ws = idaYVuelta(img);

        assertArrayEquals(new byte[]{1, 2, 3}, ws.aDataType().contenido());
        assertEquals("png", ws.aDataType().extension());
        assertNull(new DTImagenWS().aDataType());
    }

    @Test
    void seleccionPasaAMapaEnOrden() {
        DTSeleccionWS a = new DTSeleccionWS();
        a.nickname = "est1";
        a.estado = EstadoInscripcion.ACEPTADA;
        DTSeleccionWS r = new DTSeleccionWS();
        r.nickname = "est2";
        r.estado = EstadoInscripcion.RECHAZADA;

        Map<String, EstadoInscripcion> mapa = DTSeleccionWS.aMapa(List.of(a, r));

        assertEquals(List.of("est1", "est2"), List.copyOf(mapa.keySet()));
        assertEquals(EstadoInscripcion.RECHAZADA, mapa.get("est2"));
        assertTrue(DTSeleccionWS.aMapa(null).isEmpty());
    }

    @Test
    void fechasYFault() {
        assertEquals(LocalDate.of(2026, 11, 5), Fechas.fecha(" 2026-11-05 "));
        assertNull(Fechas.fecha(""));
        assertNull(Fechas.texto(null));
        assertEquals("Cupo lleno", ReglaNegocioFault.de(new ReglaNegocioException("Cupo lleno")).getMessage());
    }
}
