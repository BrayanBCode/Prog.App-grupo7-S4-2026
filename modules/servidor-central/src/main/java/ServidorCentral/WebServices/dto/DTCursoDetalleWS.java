package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTCursoDetalle;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTCursoDetalle")
public class DTCursoDetalleWS {

    public DTCursoWS curso;
    public List<DTEdicionCursoWS> ediciones = new ArrayList<>();
    public List<String> programas = new ArrayList<>();

    public DTCursoDetalleWS() {
    }

    public static DTCursoDetalleWS de(DTCursoDetalle d) {
        if (d == null) {
            return null;
        }
        DTCursoDetalleWS ws = new DTCursoDetalleWS();
        ws.curso = DTCursoWS.de(d.curso());
        ws.ediciones = new ArrayList<>(d.ediciones().stream().map(DTEdicionCursoWS::de).toList());
        ws.programas = new ArrayList<>(d.programas());
        return ws;
    }
}
