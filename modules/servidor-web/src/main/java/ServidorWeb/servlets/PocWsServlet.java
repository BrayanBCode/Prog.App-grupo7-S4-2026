package ServidorWeb.servlets;

import ServidorWeb.ws.central.ServicioCentral;
import ServidorWeb.ws.central.ServicioCentral_Service;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.BindingProvider;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Prueba de concepto (G12345-122): lista los institutos pidiendolos al Servidor Central por SOAP.
 * Se borra cuando los servlets reales usen el cliente SOAP (G12345-127).
 */
@WebServlet("/poc-ws")
public class PocWsServlet extends HttpServlet {

    private ServicioCentral central;

    @Override
    public void init() {
        String url = getServletContext().getInitParameter("edext.ws.url");
        central = new ServicioCentral_Service().getServicioCentralPort();
        ((BindingProvider) central).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("Institutos recibidos por SOAP:");
        for (String instituto : central.listarNombresInstitutos()) {
            out.println("- " + instituto);
        }
    }
}
