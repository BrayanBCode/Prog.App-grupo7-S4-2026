package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTProgramaInscripto;
import ServidorCentral.WebServices.Fechas;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTProgramaInscripto")
public class DTProgramaInscriptoWS {

    public String programa;
    public String fechaInscripcion;

    public DTProgramaInscriptoWS() {
    }

    public static DTProgramaInscriptoWS de(DTProgramaInscripto p) {
        if (p == null) {
            return null;
        }
        DTProgramaInscriptoWS ws = new DTProgramaInscriptoWS();
        ws.programa = p.programa();
        ws.fechaInscripcion = Fechas.texto(p.fechaInscripcion());
        return ws;
    }
}
