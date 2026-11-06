package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTInscripcionEdicion;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.WebServices.Fechas;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTInscripcionEdicion")
public class DTInscripcionEdicionWS {

    public String nickname;
    public String mail;
    public String nombre;
    public String apellido;
    public String fechaInscripcion;
    public EstadoInscripcion estado;

    public DTInscripcionEdicionWS() {
    }

    public static DTInscripcionEdicionWS de(DTInscripcionEdicion i) {
        if (i == null) {
            return null;
        }
        DTInscripcionEdicionWS ws = new DTInscripcionEdicionWS();
        ws.nickname = i.nickname();
        ws.mail = i.mail();
        ws.nombre = i.nombre();
        ws.apellido = i.apellido();
        ws.fechaInscripcion = Fechas.texto(i.fechaInscripcion());
        ws.estado = i.estado();
        return ws;
    }
}
