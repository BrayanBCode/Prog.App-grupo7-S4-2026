package ServidorCentral.Logica.services;

import ServidorCentral.Logica.repositories.SeguimientoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;

import java.util.List;

/** Reglas del caso de uso "Seguir a un Usuario". */
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
     */
    public void seguir(String seguidor, String seguido) throws Exception {
        if (seguidor == null || seguidor.isBlank() || seguido == null || seguido.isBlank()) {
            throw new Exception("Debe indicar el usuario que desea seguir.");
        }
        if (seguidor.equals(seguido)) {
            throw new Exception("No es posible seguirse a uno mismo.");
        }
        if (!usuarioRepository.existeNickname(seguidor)) {
            throw new Exception("El usuario " + seguidor + " no existe.");
        }
        if (!usuarioRepository.existeNickname(seguido)) {
            throw new Exception("El usuario " + seguido + " no existe.");
        }
        if (seguimientoRepository.existe(seguidor, seguido)) {
            throw new Exception("Ya está siguiendo a " + seguido + ".");
        }
        seguimientoRepository.guardar(seguidor, seguido);
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
