package Logica.services;

import Logica.entities.cursos.Instituto;
import Logica.entities.usuarios.Usuario;
import Logica.repositories.DocenteRepository;
import Logica.repositories.InstitutoRepository;
import Logica.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reglas de negocio de Usuario: nickname y mail unicos, el instituto
 * tiene que existir si el usuario es Docente, etc. No sabe nada de
 * EntityManager: para eso estan los Repository.
 */
public class UsuarioService {

    // Carpeta especial del Servidor Central donde se guardan las imagenes.
    private static final String CARPETA_IMAGENES = "imagenes_usuarios";

    private final UsuarioRepository usuarioRepository;
    private final InstitutoRepository institutoRepository;
    private final DocenteRepository docenteRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, InstitutoRepository institutoRepository, DocenteRepository docenteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.institutoRepository = institutoRepository;
        this.docenteRepository = docenteRepository;
    }

    /**
     * Caso de uso "Alta de Usuario". Si viene un instituto, el usuario es
     * Docente; si no, Estudiante.
     *
     * Tira IllegalArgumentException (no comprobada) porque asi lo hacia
     * ControllerV1 y asi lo espera la vista Swing: la firma de
     * IController.altaUsuario no declara throws.
     */
    public void registrar(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen) {

        if (usuarioRepository.existeNickname(nickname) || usuarioRepository.existeMail(mail)) {
            throw new IllegalArgumentException("El Nickname o el Email ya se encuentran registrados en el sistema.");
        }

        boolean esDocente = instituto != null && !instituto.trim().isEmpty();

        // Si es Docente, el instituto tiene que existir: se valida ANTES de
        // persistir nada, para poder frenar el alta con un mensaje claro.
        if (esDocente) {
            Instituto institutoEntity = institutoRepository.buscarPorNombre(instituto.trim());
            if (institutoEntity == null) {
                throw new IllegalArgumentException("El instituto seleccionado no existe: " + instituto);
            }
        }

        // Copiamos la imagen (si se selecciono una) a la carpeta del
        // Servidor Central y guardamos la ruta final junto al usuario.
        String rutaImagenFinal = guardarImagenUsuario(nickname, imagen);

        if (esDocente) {
            usuarioRepository.guardarDocente(nickname, mail, nombre, apellido, fechaNac, rutaImagenFinal, instituto.trim());
        } else {
            usuarioRepository.guardarEstudiante(nickname, mail, nombre, apellido, fechaNac, rutaImagenFinal);
        }
    }

    /**
     * Caso de uso "Modificar Usuario". nickname y mail son la clave y no
     * se tocan: solo se actualizan los datos basicos.
     */
    public void modificar(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception {
        Usuario u = usuarioRepository.buscarPorId(nickname, mail);
        if (u == null) {
            throw new Exception("El usuario no existe (puede haber sido eliminado por otro administrador).");
        }
        usuarioRepository.actualizarDatosBasicos(nickname, mail, nombre, apellido, fechaNac);
    }

    public String[] obtenerDataUsuario(String nickname, String mail) {
        Usuario u = usuarioRepository.buscarPorId(nickname, mail);
        if (u == null) {
            return null;
        }
        return new String[]{
                u.getNickname(),
                u.getMail(),
                u.getNombreU(),
                u.getApellido(),
                u.getFechaNac() != null ? u.getFechaNac().toString() : "",
                u.getImagen()
        };
    }

    public List<String[]> listarUsuariosTabla() {
        return aTabla(usuarioRepository.listarTodos());
    }

    public boolean esDocente(String nickname) {
        return docenteRepository.existe(nickname);
    }

    /**
     * {0}=nickname, {1}=mail -- así queda cada fila de la tabla de usuarios.
     */
    private List<String[]> aTabla(List<Usuario> lista) {
        List<String[]> resultado = new ArrayList<>();
        for (Usuario u : lista) {
            resultado.add(new String[]{u.getNickname(), u.getMail()});
        }
        return resultado;
    }

    /**
     * Copia la imagen elegida por el administrador (ruta local en su PC) a
     * la carpeta de imagenes del Servidor Central, renombrandola con el
     * nickname para evitar colisiones.
     *
     * @return la ruta final a persistir, o null si no se eligio imagen.
     */
    private String guardarImagenUsuario(String nickname, String rutaImagenOrigen) {
        if (rutaImagenOrigen == null || rutaImagenOrigen.isEmpty()) {
            return null;
        }

        java.io.File origen = new java.io.File(rutaImagenOrigen);
        if (!origen.exists()) {
            return null;
        }

        // Validacion de formato por seguridad (ademas del filtro de la vista)
        String nombreOrigen = origen.getName().toLowerCase();
        String extension = nombreOrigen.substring(nombreOrigen.lastIndexOf('.') + 1);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new IllegalArgumentException("Formato de imagen no soportado. Use JPG o PNG.");
        }

        try {
            java.io.File carpetaDestino = new java.io.File(CARPETA_IMAGENES);
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }

            java.io.File destino = new java.io.File(carpetaDestino, nickname + "." + extension);
            java.nio.file.Files.copy(
                    origen.toPath(),
                    destino.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            return destino.getPath();
        } catch (java.io.IOException ex) {
            throw new RuntimeException("No se pudo guardar la imagen del usuario: " + ex.getMessage(), ex);
        }
    }
}