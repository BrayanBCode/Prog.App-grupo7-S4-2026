package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTPerfil;
import ServidorCentral.Logica.seguridad.Rol;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTPerfil")
public class DTPerfilWS {

    public DTUsuarioWS usuario;
    public Rol rol;
    public boolean propio;
    public boolean verDetalle;
    public List<String> institutos = new ArrayList<>();
    public List<DTEdicionCursoWS> ediciones = new ArrayList<>();
    public List<DTInscripcionEstudianteWS> inscripciones = new ArrayList<>();
    public List<DTProgramaInscriptoWS> programas = new ArrayList<>();
    public List<String> seguidores = new ArrayList<>();
    public List<String> seguidos = new ArrayList<>();
    public boolean loSigue;
    public boolean verAceptados;

    public DTPerfilWS() {
    }

    public static DTPerfilWS de(DTPerfil p) {
        if (p == null) {
            return null;
        }
        DTPerfilWS ws = new DTPerfilWS();
        ws.usuario = DTUsuarioWS.de(p.usuario());
        ws.rol = p.rol();
        ws.propio = p.propio();
        ws.verDetalle = p.verDetalle();
        ws.institutos = new ArrayList<>(p.institutos());
        ws.ediciones = new ArrayList<>(p.ediciones().stream().map(DTEdicionCursoWS::de).toList());
        ws.inscripciones = new ArrayList<>(p.inscripciones().stream().map(DTInscripcionEstudianteWS::de).toList());
        ws.programas = new ArrayList<>(p.programas().stream().map(DTProgramaInscriptoWS::de).toList());
        ws.seguidores = new ArrayList<>(p.seguidores());
        ws.seguidos = new ArrayList<>(p.seguidos());
        ws.loSigue = p.loSigue();
        ws.verAceptados = p.verAceptados();
        return ws;
    }
}
