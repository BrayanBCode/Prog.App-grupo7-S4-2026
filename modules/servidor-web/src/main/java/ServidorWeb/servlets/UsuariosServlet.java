package ServidorWeb.servlets;

import ServidorCentral.Logica.datatypes.DTDocenteDetalle;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.datatypes.DTUsuarioResumen;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Caso de uso "Consulta de Usuario" (Visitante, Estudiante, Docente).
 * <ul>
 *   <li>/usuarios          -> lista de todos los usuarios</li>
 *   <li>/usuarios?nick=X   -> perfil: datos basicos e imagen; ediciones si es docente;
 *       inscripciones (Inscripto/Aceptada) y programas si es estudiante. En el perfil
 *       propio el estudiante ve tambien las Rechazadas y el docente tiene acceso a
 *       "Listar aceptados" de sus ediciones.</li>
 * </ul>
 */
@WebServlet("/usuarios")
public class UsuariosServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nick = param(req, "nick");
        if (nick.isEmpty()) {
            listar(req, resp);
        } else {
            perfil(req, resp, nick);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<DTUsuario> usuarios = new ArrayList<>();
        for (DTUsuarioResumen r : controller.listarUsuariosTabla()) {
            DTUsuario u = controller.obtenerDataUsuario(r.nickname(), r.mail());
            if (u != null) {
                usuarios.add(u);
            }
        }
        usuarios.sort(Comparator.comparing(DTUsuario::nickname, String.CASE_INSENSITIVE_ORDER));
        req.setAttribute("titulo", "Usuarios");
        req.setAttribute("usuarios", usuarios);
        vista(req, resp, "usuario/lista.jsp");
    }

    private void perfil(HttpServletRequest req, HttpServletResponse resp, String nick)
            throws ServletException, IOException {
        DTUsuario u = controller.obtenerUsuario(nick);
        if (u == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", "No existe el usuario \"" + nick + "\".");
            listar(req, resp);
            return;
        }

        DTSesion yo = usuario(req);
        boolean propio = yo != null && yo.getNickname().equals(u.nickname());
        boolean esDocente = controller.esDocente(u.nickname());

        if (esDocente) {
            DTDocenteDetalle detalle = controller.obtenerDataDocente(u.nickname());
            List<DTEdicionCurso> ediciones = new ArrayList<>();
            for (String ed : detalle.ediciones()) {
                try {
                    ediciones.add(controller.obtenerEdicionCurso(ed));
                } catch (Exception ignorada) {
                    // edicion borrada entre consultas
                }
            }
            ediciones.sort((a, b) -> b.fechaInicio().compareTo(a.fechaInicio()));
            req.setAttribute("institutos", detalle.institutos());
            req.setAttribute("ediciones", ediciones);
        } else {
            List<DTInscripcionEstudiante> inscripciones = new ArrayList<>();
            for (DTInscripcionEstudiante i : controller.listarInscripcionesEstudiante(u.nickname())) {
                // Las rechazadas solo las ve el propio estudiante (Listar resultados de inscripciones)
                if (propio || i.estado() != EstadoInscripcion.RECHAZADA) {
                    inscripciones.add(i);
                }
            }
            req.setAttribute("inscripciones", inscripciones);
            req.setAttribute("programasInscripto", controller.listarProgramasEstudiante(u.nickname()));
        }

        req.setAttribute("titulo", u.nickname());
        req.setAttribute("perfil", u);
        req.setAttribute("esDocente", esDocente);
        req.setAttribute("propio", propio);
        req.setAttribute("seguidores", controller.listarSeguidores(u.nickname()));
        req.setAttribute("seguidos", controller.listarSeguidos(u.nickname()));
        req.setAttribute("loSigo", yo != null && controller.estaSiguiendo(yo.getNickname(), u.nickname()));
        vista(req, resp, "usuario/perfil.jsp");
    }
}
