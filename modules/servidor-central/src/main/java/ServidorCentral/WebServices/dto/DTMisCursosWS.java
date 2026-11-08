package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTMisCursos;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTMisCursos")
public class DTMisCursosWS {

    public List<DTCursoResumenWS> cursos = new ArrayList<>();
    public List<Edicion> ediciones = new ArrayList<>();

    /** DTMisCursos.Edicion: la edicion, si esta vigente y si esta cerrada. */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "DTMisCursosEdicion")
    public static class Edicion {

        public DTEdicionCursoWS edicion;
        public boolean vigente;
        public boolean cerrada;

        public Edicion() {
        }

        public static Edicion de(DTMisCursos.Edicion e) {
            Edicion ws = new Edicion();
            ws.edicion = DTEdicionCursoWS.de(e.edicion());
            ws.vigente = e.vigente();
            ws.cerrada = e.cerrada();
            return ws;
        }
    }

    public DTMisCursosWS() {
    }

    public static DTMisCursosWS de(DTMisCursos m) {
        if (m == null) {
            return null;
        }
        DTMisCursosWS ws = new DTMisCursosWS();
        ws.cursos = new ArrayList<>(m.cursos().stream().map(DTCursoResumenWS::de).toList());
        ws.ediciones = new ArrayList<>(m.ediciones().stream().map(Edicion::de).toList());
        return ws;
    }
}
