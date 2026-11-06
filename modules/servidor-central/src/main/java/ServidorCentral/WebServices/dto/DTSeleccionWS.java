package ServidorCentral.WebServices.dto;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Una entrada del Map de Seleccionar estudiantes (nickname -> estado):
 * SOAP no tiene Map, asi que el web manda una lista de estos.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTSeleccion")
public class DTSeleccionWS {

    public String nickname;
    public EstadoInscripcion estado;

    public DTSeleccionWS() {
    }

    public static Map<String, EstadoInscripcion> aMapa(List<DTSeleccionWS> selecciones) {
        Map<String, EstadoInscripcion> estados = new LinkedHashMap<>();
        if (selecciones != null) {
            for (DTSeleccionWS s : selecciones) {
                estados.put(s.nickname, s.estado);
            }
        }
        return estados;
    }
}
