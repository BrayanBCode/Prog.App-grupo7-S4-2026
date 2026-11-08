package ServidorWeb.servlets;

import ServidorWeb.ws.ErrorNegocio;
import ServidorWeb.ws.central.DTInscripcionesEdicion;
import ServidorWeb.ws.central.DTNota;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Caso de uso "Ingresar resultados de evaluaciones de estudiantes en ediciones de cursos" (Tarea 3). Actor: Docente
 * que participa en la edicion (el AccesoFilter garantiza el rol; el central, que participe): /edicion/resultados?edicion=E.
 * Se llega desde "Mis cursos y ediciones" y desde la Consulta de Edicion.
 * Muestra los aceptados con su nota (1 a 12). El formulario manda nota.NICK=valor por cada estudiante (vacio = sin
 * cambios) y accion=guardar o accion=cerrar; cerrar guarda primero lo escrito y despues cierra (Post/Redirect/Get).
 * Con la edicion cerrada ya no hay nada que ingresar: lleva al acta (Ver resultados, /edicion/acta).
 */
@WebServlet("/edicion/resultados")
public class IngresarResultadosServlet extends BaseServlet {

    private static final String PREFIJO = "nota.";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            DTInscripcionesEdicion resultados = central.consultarAceptadosEdicion(usuario(req).getNickname(), param(req, "edicion"));
            if (resultados.getEdicion().isCerrada()) {
                redirigir(req, resp, "/edicion/acta?edicion=" + url(param(req, "edicion")));
                return;
            }
            req.setAttribute("resultados", resultados);
            req.setAttribute("titulo", "Resultados de evaluaciones");
            vista(req, resp, "edicion/resultados.jsp");
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", mensajeDeError(e));
            req.setAttribute("titulo", "Cursos");
            vista(req, resp, "curso/lista.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String edicion = param(req, "edicion");
        String nickDocente = usuario(req).getNickname();
        boolean cerrar = "cerrar".equals(param(req, "accion"));
        try {
            List<DTNota> notas = leerNotas(req);
            if (!notas.isEmpty()) {
                central.registrarNotas(nickDocente, edicion, notas);
            } else if (!cerrar) {
                throw new ErrorNegocio("No escribiste ninguna nota.");
            }
            if (cerrar) {
                central.cerrarEdicion(nickDocente, edicion);
                flash(req, "Se cerró la edición: las notas ya no se pueden modificar.");
            } else {
                flash(req, "Se guardaron las notas.");
            }
        } catch (RuntimeException e) {
            flashError(req, mensajeDeError(e));
        }
        redirigir(req, resp, "/edicion/resultados?edicion=" + url(edicion));
    }

    /** Una DTNota por cada campo nota.NICK con valor; los vacios no se mandan. */
    private static List<DTNota> leerNotas(HttpServletRequest req) {
        List<DTNota> notas = new ArrayList<>();
        for (String campo : Collections.list(req.getParameterNames())) {
            if (!campo.startsWith(PREFIJO)) {
                continue;
            }
            String nickname = campo.substring(PREFIJO.length());
            String valor = param(req, campo);
            if (nickname.isEmpty() || valor == null || valor.isBlank()) {
                continue;
            }
            DTNota n = new DTNota();
            n.setNickname(nickname);
            try {
                n.setNota(Integer.valueOf(valor.trim()));
            } catch (NumberFormatException e) {
                throw new ErrorNegocio("La nota de " + nickname + " debe ser un número entre 1 y 12.");
            }
            notas.add(n);
        }
        return notas;
    }
}
