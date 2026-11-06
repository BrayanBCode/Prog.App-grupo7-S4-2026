package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTSesion;
import ServidorCentral.Logica.seguridad.Rol;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTSesion")
public class DTSesionWS {

    public String nickname;
    public String mail;
    public String nombre;
    public String apellido;
    public String imagen;
    public Rol rol;

    public DTSesionWS() {
    }

    public static DTSesionWS de(DTSesion s) {
        if (s == null) {
            return null;
        }
        DTSesionWS ws = new DTSesionWS();
        ws.nickname = s.getNickname();
        ws.mail = s.getMail();
        ws.nombre = s.getNombre();
        ws.apellido = s.getApellido();
        ws.imagen = s.getImagen();
        ws.rol = s.getRol();
        return ws;
    }
}
