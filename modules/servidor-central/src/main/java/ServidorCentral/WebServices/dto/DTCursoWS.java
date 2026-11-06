package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTCurso;
import ServidorCentral.WebServices.Fechas;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTCurso")
public class DTCursoWS {

    public String nombre;
    public String descripcion;
    public int duracion;
    public float cantHoras;
    public int cantCreditos;
    public String url;
    public String fechaRegistro;
    public String instituto;
    public DTDocenteResumenWS docente;
    public List<String> previas = new ArrayList<>();
    public String imagen;

    public DTCursoWS() {
    }

    public static DTCursoWS de(DTCurso c) {
        if (c == null) {
            return null;
        }
        DTCursoWS ws = new DTCursoWS();
        ws.nombre = c.nombre();
        ws.descripcion = c.descripcion();
        ws.duracion = c.duracion();
        ws.cantHoras = c.cantHoras();
        ws.cantCreditos = c.cantCreditos();
        ws.url = c.url();
        ws.fechaRegistro = Fechas.texto(c.fechaRegistro());
        ws.instituto = c.instituto();
        ws.docente = DTDocenteResumenWS.de(c.docente());
        ws.previas = new ArrayList<>(c.previas());
        ws.imagen = c.imagen();
        return ws;
    }
}
