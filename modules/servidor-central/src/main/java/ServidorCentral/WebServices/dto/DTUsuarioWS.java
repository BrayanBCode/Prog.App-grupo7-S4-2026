package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTUsuario;
import ServidorCentral.WebServices.Fechas;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTUsuario")
public class DTUsuarioWS {

    public String nickname;
    public String mail;
    public String nombre;
    public String apellido;
    public String fechaNac;
    public String imagen;

    public DTUsuarioWS() {
    }

    public static DTUsuarioWS de(DTUsuario u) {
        if (u == null) {
            return null;
        }
        DTUsuarioWS ws = new DTUsuarioWS();
        ws.nickname = u.nickname();
        ws.mail = u.mail();
        ws.nombre = u.nombre();
        ws.apellido = u.apellido();
        ws.fechaNac = Fechas.texto(u.fechaNac());
        ws.imagen = u.imagen();
        return ws;
    }
}
