package ServidorCentral.WebServices.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Nota de un estudiante en "Ingresar resultados". SOAP no tiene Map: el web manda una por estudiante. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTNota")
public class DTNotaWS {

    public String nickname;
    public Integer nota;

    public DTNotaWS() {
    }

    public static Map<String, Integer> aMapa(List<DTNotaWS> notas) {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        if (notas != null) {
            for (DTNotaWS n : notas) {
                resultado.put(n.nickname, n.nota);
            }
        }
        return resultado;
    }
}
