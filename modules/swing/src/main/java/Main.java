
import ServidorCentral.WebServices.Publicador;
import Swing.JFInicio;

import javax.swing.JOptionPane;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

/**
 * Punto de entrada de servidor.jar (G12345-136): publica los Web Services del Servidor Central en la
 * "ws.url" de ~/.edEXT/servidor.properties y abre la Estacion de Trabajo.
 * <p>
 * Con {@code --sin-ventana} (o en una maquina sin entorno grafico) solo publica los Web Services: sirve
 * para dejar el central corriendo en un nodo sin pantalla. Cerrar la Estacion de Trabajo termina el
 * proceso y con el los Web Services.
 */
public class Main {

    public static void main(String[] args) {
        boolean sinVentana = Arrays.asList(args).contains("--sin-ventana") || GraphicsEnvironment.isHeadless();
        try {
            Publicador.publicar();
        } catch (RuntimeException e) {
            String mensaje = "No se pudieron publicar los Web Services: " + e.getMessage()
                    + "\nRevisá ws.url en ~/.edEXT/servidor.properties (¿el puerto ya está en uso?).";
            System.err.println(mensaje);
            if (sinVentana) {
                System.exit(1);
            }
            JOptionPane.showMessageDialog(null, mensaje, "edEXT - Servidor Central", JOptionPane.ERROR_MESSAGE);
        }
        if (sinVentana) {
            System.out.println("Servidor Central corriendo sin Estación de Trabajo (Ctrl+C para terminar).");
            return;
        }
        new JFInicio();
    }
}
