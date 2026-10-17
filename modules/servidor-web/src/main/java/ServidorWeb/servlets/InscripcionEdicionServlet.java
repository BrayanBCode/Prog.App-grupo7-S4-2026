package ServidorWeb.servlets;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorWeb.config.Json;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Caso de uso "Inscripcion a Edicion de Curso" con Ajax (pedido del docente). Actor: Estudiante (el
 * AccesoFilter contesta 401/403 JSON si no corresponde). Lo llama js/inscripcion.js desde la Consulta
 * de Edicion; responde JSON y la pagina no se recarga:
 * {"ok": true, "estado": "Inscripto"} o {"ok": false, "error": "..."}.
 */
@WebServlet("/edicion/inscripcion")
public class InscripcionEdicionServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            controller.inscribirse(usuario(req).getNickname(), param(req, "edicion"));
            Json.escribir(resp, HttpServletResponse.SC_OK,
                    Map.of("ok", true, "estado", EstadoInscripcion.INSCRIPTO.getEtiqueta()));
        } catch (RuntimeException e) {
            int status = e instanceof ReglaNegocioException
                    ? HttpServletResponse.SC_CONFLICT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            Json.error(resp, status, mensajeDeError(e));
        }
    }
}
