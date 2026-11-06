package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTEdicionDetalle;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/** miInscripcion: estado de la inscripcion de quien mira; null si no esta inscripto. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTEdicionDetalle")
public class DTEdicionDetalleWS {

    public DTEdicionCursoWS edicion;
    public String instituto;
    public List<DTDocenteResumenWS> docentes = new ArrayList<>();
    public boolean vigente;
    public EstadoInscripcion miInscripcion;

    public DTEdicionDetalleWS() {
    }

    public static DTEdicionDetalleWS de(DTEdicionDetalle d) {
        if (d == null) {
            return null;
        }
        DTEdicionDetalleWS ws = new DTEdicionDetalleWS();
        ws.edicion = DTEdicionCursoWS.de(d.edicion());
        ws.instituto = d.instituto();
        ws.docentes = new ArrayList<>(d.docentes().stream().map(DTDocenteResumenWS::de).toList());
        ws.vigente = d.vigente();
        ws.miInscripcion = d.miInscripcion();
        return ws;
    }
}
