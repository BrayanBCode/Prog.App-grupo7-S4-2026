package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.WebServices.Fechas;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTInscripcionEstudiante")
public class DTInscripcionEstudianteWS {

    public String edicion;
    public String curso;
    public String fechaInscripcion;
    public EstadoInscripcion estado;

    public DTInscripcionEstudianteWS() {
    }

    public static DTInscripcionEstudianteWS de(DTInscripcionEstudiante i) {
        if (i == null) {
            return null;
        }
        DTInscripcionEstudianteWS ws = new DTInscripcionEstudianteWS();
        ws.edicion = i.edicion();
        ws.curso = i.curso();
        ws.fechaInscripcion = Fechas.texto(i.fechaInscripcion());
        ws.estado = i.estado();
        return ws;
    }
}
