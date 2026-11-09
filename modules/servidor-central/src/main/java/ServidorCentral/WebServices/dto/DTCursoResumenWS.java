package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTCursoResumen;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTCursoResumen")
public class DTCursoResumenWS {

    public String nombre;
    public String descripcion;
    public String imagen;
    /** Promedio de valoraciones (0 si no tiene) y cuantas hay: solo en la busqueda. */
    public double promedio;
    public int valoraciones;

    public DTCursoResumenWS() {
    }

    public static DTCursoResumenWS de(DTCursoResumen c) {
        if (c == null) {
            return null;
        }
        DTCursoResumenWS ws = new DTCursoResumenWS();
        ws.nombre = c.nombre();
        ws.descripcion = c.descripcion();
        ws.imagen = c.imagen();
        ws.promedio = c.promedio();
        ws.valoraciones = c.valoraciones();
        return ws;
    }
}
