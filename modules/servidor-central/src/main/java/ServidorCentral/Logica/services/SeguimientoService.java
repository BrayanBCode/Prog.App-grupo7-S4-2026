package ServidorCentral.Logica.services;

import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.repositories.SeguimientoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;

import java.util.List;

/** Reglas de los casos de uso "Seguir a un Usuario" y "Dejar de seguir a un Usuario". */
public class SeguimientoService {

    private final UsuarioRepository usuarioRepository;
    private final SeguimientoRepository seguimientoRepository;

    public SeguimientoService(UsuarioRepository usuarioRepository, SeguimientoRepository seguimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.seguimientoRepository = seguimientoRepository;
    }

    /**
     * El usuario {@code seguidor} empieza a seguir a {@code seguido} (ambos por nickname).
     * Los dos tienen que existir, no se puede seguir a uno mismo ni seguir dos veces al mismo usuario.
     *
     * @return la nueva cantidad de seguidores de {@code seguido} (el web actualiza el contador sin recargar)
     * @throws ReglaNegocioException con el mensaje para el usuario
     */
    public int seguir(String seguidor, String seguido) {
        if (seguidor == null || seguidor.isBlank() || seguido == null || seguido.isBlank()) {
            throw new ReglaNegocioException("Debe indicar el usuario que desea seguir.");
        }
        if (seguidor.equals(seguido)) {
            throw new ReglaNegocioException("No es posible seguirse a uno mismo.");
        }
        if (!usuarioRepository.existeNickname(seguidor)) {
            throw new ReglaNegocioException("El usuario " + seguidor + " no existe.");
        }
        if (!usuarioRepository.existeNickname(seguido)) {
            throw new ReglaNegocioException("El usuario " + seguido + " no existe.");
        }
        if (seguimientoRepository.existe(seguidor, seguido)) {
            throw new ReglaNegocioException("Ya está siguiendo a " + seguido + ".");
        }
        seguimientoRepository.guardar(seguidor, seguido);
        return seguimientoRepository.seguidoresDe(seguido).size();
    }

    /**
     * El usuario {@code seguidor} deja de seguir a {@code seguido}; tiene que estar siguiendolo.
     *
     * @return la nueva cantidad de seguidores de {@code seguido}
     * @throws ReglaNegocioException con el mensaje para el usuario
     */
    public int dejarDeSeguir(String seguidor, String seguido) {
        if (seguidor == null || seguidor.isBlank() || seguido == null || seguido.isBlank()) {
            throw new ReglaNegocioException("Debe indicar el usuario que desea dejar de seguir.");
        }
        if (!seguimientoRepository.existe(seguidor, seguido)) {
            throw new ReglaNegocioException("No está siguiendo a " + seguido + ".");
        }
        seguimientoRepository.eliminar(seguidor, seguido);
        return seguimientoRepository.seguidoresDe(seguido).size();
    }

    public boolean estaSiguiendo(String seguidor, String seguido) {
        return seguimientoRepository.existe(seguidor, seguido);
    }

    public List<String> listarSeguidos(String nickname) {
        return seguimientoRepository.seguidosDe(nickname);
    }

    public List<String> listarSeguidores(String nickname) {
        return seguimientoRepository.seguidoresDe(nickname);
    }
}
