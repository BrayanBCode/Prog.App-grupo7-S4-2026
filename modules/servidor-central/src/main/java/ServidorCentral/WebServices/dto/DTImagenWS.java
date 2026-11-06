package ServidorCentral.WebServices.dto;

import ServidorCentral.DataTypes.DTImagen;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/** Imagen que sube el web (alta de usuario, curso o edicion): el contenido viaja en base64. */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTImagen")
public class DTImagenWS {

    public byte[] contenido;
    public String extension;

    public DTImagenWS() {
    }

    /** null si no vino imagen. */
    public DTImagen aDataType() {
        return contenido == null || contenido.length == 0 ? null : new DTImagen(contenido, extension);
    }
}
