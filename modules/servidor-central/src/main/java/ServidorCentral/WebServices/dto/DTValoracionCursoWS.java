package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTValoracionCurso;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

/** DTValoracionCurso por SOAP. porPuntaje: posicion 0 = 1 estrella ... posicion 4 = 5 estrellas. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTValoracionCurso")
public class DTValoracionCursoWS {

    public String curso;
    public double promedio;
    public int cantidad;
    public List<Integer> porPuntaje = new ArrayList<>();
    public Integer miPuntaje;

    public DTValoracionCursoWS() {
    }

    public static DTValoracionCursoWS de(DTValoracionCurso d) {
        if (d == null) {
            return null;
        }
        DTValoracionCursoWS ws = new DTValoracionCursoWS();
        ws.curso = d.curso();
        ws.promedio = d.promedio();
        ws.cantidad = d.cantidad();
        ws.porPuntaje = new ArrayList<>(d.porPuntaje());
        ws.miPuntaje = d.miPuntaje();
        return ws;
    }
}
