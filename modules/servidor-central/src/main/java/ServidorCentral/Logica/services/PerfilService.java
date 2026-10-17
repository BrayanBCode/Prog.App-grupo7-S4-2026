package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTInscripcionEstudiante;
import ServidorCentral.Logica.datatypes.DTPerfil;
import ServidorCentral.Logica.datatypes.DTProgramaInscripto;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.entities.cursos.EdicionCurso;
import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.InscripcionEdicion;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Logica.repositories.SeguimientoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import ServidorCentral.Logica.seguridad.Rol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Caso de uso "Consulta de Usuario" (Servidor Web). Aca se decide que ve cada uno:
 * <ul>
 *   <li>Visitante: datos basicos e imagen (y los institutos si es docente).</li>
 *   <li>Con sesion: ademas las ediciones (docente) o las inscripciones Inscripto/Aceptada y
 *       los programas (estudiante), y seguidores/seguidos.</li>
 *   <li>Las inscripciones Rechazadas las ve el propio estudiante y cualquier docente.</li>
 *   <li>El docente en su propio perfil tiene "Ver aceptados" en sus ediciones.</li>
 * </ul>
 */
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final SeguimientoRepository seguimientoRepository;

    public PerfilService(UsuarioRepository usuarioRepository, DocenteRepository docenteRepository,
                         EstudianteRepository estudianteRepository, SeguimientoRepository seguimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.seguimientoRepository = seguimientoRepository;
    }

    /** Todos los usuarios, ordenados por nickname (lista de la Consulta de Usuario). */
    public List<DTUsuario> listarUsuarios() {
        List<DTUsuario> usuarios = new ArrayList<>();
        for (Usuario u : usuarioRepository.listarTodos()) {
            usuarios.add(aDT(u));
        }
        usuarios.sort(Comparator.comparing(DTUsuario::nickname, String.CASE_INSENSITIVE_ORDER));
        return usuarios;
    }

    /**
     * @param nickConsultado usuario cuyo perfil se muestra
     * @param nickQueMira    usuario con sesion que mira, o null si es un Visitante
     * @throws ReglaNegocioException si el usuario consultado no existe
     */
    public DTPerfil obtenerPerfil(String nickConsultado, String nickQueMira) {
        Usuario u = buscar(nickConsultado);
        if (u == null) {
            throw new ReglaNegocioException("No existe el usuario \"" + (nickConsultado == null ? "" : nickConsultado.trim()) + "\".");
        }
        String nick = u.getNickname();
        boolean esDocente = u instanceof Docente;
        boolean verDetalle = nickQueMira != null && !nickQueMira.isBlank();
        boolean propio = verDetalle && nickQueMira.equals(nick);
        boolean miraUnDocente = verDetalle && docenteRepository.existe(nickQueMira);

        List<String> institutos = esDocente ? docenteRepository.nombresInstitutos(nick) : List.of();
        List<DTEdicionCurso> ediciones = List.of();
        List<DTInscripcionEstudiante> inscripciones = List.of();
        List<DTProgramaInscripto> programas = List.of();
        List<String> seguidores = List.of();
        List<String> seguidos = List.of();
        boolean loSigue = false;

        if (verDetalle) {
            if (esDocente) {
                ediciones = edicionesDe(nick);
            } else {
                inscripciones = inscripcionesDe(nick, propio || miraUnDocente);
                programas = programasDe(nick);
            }
            seguidores = seguimientoRepository.seguidoresDe(nick);
            seguidos = seguimientoRepository.seguidosDe(nick);
            loSigue = !propio && seguimientoRepository.existe(nickQueMira, nick);
        }

        return new DTPerfil(aDT(u), esDocente ? Rol.DOCENTE : Rol.ESTUDIANTE, propio, verDetalle,
                institutos, ediciones, inscripciones, programas, seguidores, seguidos, loSigue,
                propio && esDocente);
    }

    /** Busca por nickname exacto (el repositorio busca por nickname o mail). */
    private Usuario buscar(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return null;
        }
        Usuario u = usuarioRepository.buscarPorNicknameOMail(nickname.trim());
        return u != null && u.getNickname().equals(nickname.trim()) ? u : null;
    }

    /** Ediciones del docente, de la mas reciente a la mas vieja. */
    private List<DTEdicionCurso> edicionesDe(String nickname) {
        List<DTEdicionCurso> resultado = new ArrayList<>();
        for (EdicionCurso ed : docenteRepository.edicionesDe(nickname)) {
            resultado.add(new DTEdicionCurso(ed.getNombre(), ed.getCurso().getNombreC(), ed.getFechaInicio(),
                    ed.getFechaFin(), ed.getCupo(), ed.getFechaPublicacion(), ed.getImagen()));
        }
        resultado.sort(Comparator.comparing(DTEdicionCurso::fechaInicio,
                Comparator.nullsLast(Comparator.<LocalDate>reverseOrder())));
        return resultado;
    }

    /** Inscripciones del estudiante, de la mas reciente a la mas vieja. */
    private List<DTInscripcionEstudiante> inscripcionesDe(String nickname, boolean conRechazadas) {
        List<DTInscripcionEstudiante> resultado = new ArrayList<>();
        for (InscripcionEdicion i : estudianteRepository.inscripcionesDe(nickname)) {
            if (conRechazadas || i.getEstado() != EstadoInscripcion.RECHAZADA) {
                resultado.add(new DTInscripcionEstudiante(i.getEdicionCurso().getNombre(),
                        i.getEdicionCurso().getCurso().getNombreC(), i.getFechaInscripcion(), i.getEstado()));
            }
        }
        resultado.sort(Comparator.comparing(DTInscripcionEstudiante::fechaInscripcion,
                Comparator.nullsLast(Comparator.<LocalDate>reverseOrder())));
        return resultado;
    }

    private List<DTProgramaInscripto> programasDe(String nickname) {
        List<DTProgramaInscripto> resultado = new ArrayList<>();
        for (Object[] fila : estudianteRepository.programasConFecha(nickname)) {
            resultado.add(new DTProgramaInscripto((String) fila[0], (LocalDate) fila[1]));
        }
        return resultado;
    }

    private static DTUsuario aDT(Usuario u) {
        return new DTUsuario(u.getNickname(), u.getMail(), u.getNombreU(), u.getApellido(), u.getFechaNac(), u.getImagen());
    }
}
