package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTEdicionCurso;
import ServidorCentral.WebServices.Fechas;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTEdicionCurso")
public class DTEdicionCursoWS {

    public String nombre;
    public String nombreCurso;
    public String fechaInicio;
    public String fechaFin;
    public int cupo;
    public String fechaPublicacion;
    public String imagen;

    public DTEdicionCursoWS() {
    }

    public static DTEdicionCursoWS de(DTEdicionCurso e) {
        if (e == null) {
            return null;
        }
        DTEdicionCursoWS ws = new DTEdicionCursoWS();
        ws.nombre = e.nombre();
        ws.nombreCurso = e.nombreCurso();
        ws.fechaInicio = Fechas.texto(e.fechaInicio());
        ws.fechaFin = Fechas.texto(e.fechaFin());
        ws.cupo = e.cupo();
        ws.fechaPublicacion = Fechas.texto(e.fechaPublicacion());
        ws.imagen = e.imagen();
        return ws;
    }
}
