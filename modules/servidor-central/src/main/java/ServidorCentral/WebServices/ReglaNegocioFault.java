package ServidorCentral.WebServices;

import ServidorCentral.Exepciones.ReglaNegocioException;
import jakarta.xml.ws.WebFault;

/**
 * ReglaNegocioException como fault declarado en el WSDL: el web recibe una excepcion propia
 * con el mismo mensaje, en vez de un SOAPFaultException generico.
 */
@WebFault(name = "ReglaNegocioFault")
public class ReglaNegocioFault extends Exception {

    public ReglaNegocioFault(String mensaje) {
        super(mensaje);
    }

    public static ReglaNegocioFault de(ReglaNegocioException e) {
        return new ReglaNegocioFault(e.getMessage());
    }
}
