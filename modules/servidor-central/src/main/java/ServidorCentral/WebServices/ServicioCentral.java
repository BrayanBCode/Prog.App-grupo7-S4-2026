package ServidorCentral.WebServices;

import ServidorCentral.Logica.controller.Fabrica;
import ServidorCentral.Logica.controller.IControllerV2;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

/**
 * Operaciones del Servidor Central expuestas por SOAP para el Servidor Web.
 * Delega en el mismo controller que usa la Estacion de Trabajo.
 */
@WebService(serviceName = "ServicioCentral", portName = "ServicioCentralPort")
public class ServicioCentral {

    private final IControllerV2 controller = Fabrica.getInstance().getControllerV2();

    @WebMethod
    public List<String> listarNombresInstitutos() {
        return controller.listarNombresInstitutos();
    }
}
