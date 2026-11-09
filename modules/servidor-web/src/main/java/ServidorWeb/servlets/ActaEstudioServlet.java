package ServidorWeb.servlets;

import ServidorWeb.pdf.ActaEstudioPdf;
import ServidorWeb.seguridad.DTSesion;
import ServidorWeb.ws.ErrorNegocio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Caso de uso "Tramitar Acta de Estudio" (letra 6.1 y 7.4). Actor: Estudiante (lo garantiza el AccesoFilter). Se
 * entra desde el propio perfil con "Generar acta de estudio": el PDF se arma en cada pedido con los resultados que
 * trae el central y se descarga.
 */
@WebServlet("/perfil/acta")
public class ActaEstudioServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DTSesion yo = usuario(req);
        byte[] pdf;
        try {
            pdf = ActaEstudioPdf.generar(yo.getNombre() + " " + yo.getApellido(), yo.getNickname(), yo.getMail(),
                    ActaEstudioPdf.filas(central.resultadosInscripciones(yo.getNickname())), LocalDateTime.now());
        } catch (ErrorNegocio e) {
            flashError(req, e.getMessage());
            redirigir(req, resp, "/usuarios?nick=" + url(yo.getNickname()));
            return;
        }
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=\"acta-de-estudio-" + yo.getNickname().replaceAll("[^A-Za-z0-9_.-]", "_") + ".pdf\"");
        resp.setHeader("Cache-Control", "no-store");
        resp.setContentLength(pdf.length);
        resp.getOutputStream().write(pdf);
    }
}
