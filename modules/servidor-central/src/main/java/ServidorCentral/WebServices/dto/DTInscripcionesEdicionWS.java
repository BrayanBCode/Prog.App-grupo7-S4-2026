package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTInscripcionesEdicion;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTInscripcionesEdicion")
public class DTInscripcionesEdicionWS {

    public DTEdicionDetalleWS edicion;
    public List<DTInscripcionEdicionWS> inscripciones = new ArrayList<>();

    public DTInscripcionesEdicionWS() {
    }

    public static DTInscripcionesEdicionWS de(DTInscripcionesEdicion d) {
        if (d == null) {
            return null;
        }
        DTInscripcionesEdicionWS ws = new DTInscripcionesEdicionWS();
        ws.edicion = DTEdicionDetalleWS.de(d.edicion());
        ws.inscripciones = new ArrayList<>(d.inscripciones().stream().map(DTInscripcionEdicionWS::de).toList());
        return ws;
    }
}
