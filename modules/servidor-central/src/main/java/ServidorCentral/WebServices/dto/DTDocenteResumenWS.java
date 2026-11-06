package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTDocenteResumen;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTDocenteResumen")
public class DTDocenteResumenWS {

    public String nickname;
    public String nombre;
    public String apellido;

    public DTDocenteResumenWS() {
    }

    public static DTDocenteResumenWS de(DTDocenteResumen d) {
        if (d == null) {
            return null;
        }
        DTDocenteResumenWS ws = new DTDocenteResumenWS();
        ws.nickname = d.nickname();
        ws.nombre = d.nombre();
        ws.apellido = d.apellido();
        return ws;
    }
}
