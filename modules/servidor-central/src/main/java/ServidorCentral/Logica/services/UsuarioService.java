package ServidorCentral.Logica.services;

import ServidorCentral.Logica.datatypes.DTImagen;
import ServidorCentral.Logica.excepciones.ReglaNegocioException;
import ServidorCentral.Logica.datatypes.DTSesion;
import ServidorCentral.Logica.datatypes.DTUsuario;
import ServidorCentral.Logica.datatypes.DTUsuarioResumen;
import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Logica.repositories.DocenteRepository;
import ServidorCentral.Logica.repositories.InstitutoRepository;
import ServidorCentral.Logica.repositories.UsuarioRepository;
import ServidorCentral.Logica.seguridad.Rol;
import ServidorCentral.Persistencia.AlmacenImagenes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reglas de negocio de Usuario: nickname y mail unicos, el instituto
 * tiene que existir si el usuario es Docente, etc. No sabe nada de
 * EntityManager: para eso estan los Repository.
 */
public class UsuarioService {

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
    public void registrar(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, String imagen, String contraseña) {
        registrar(nickname, mail, nombre, apellido, fechaNac, instituto, AlmacenImagenes.leer(imagen), contraseña);
    }

    /** Igual que el anterior, con la imagen ya leida (Servidor Web). */
    public void registrar(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto, DTImagen imagen, String contraseña) {

        if (usuarioRepository.existeNickname(nickname) || usuarioRepository.existeMail(mail)) {
            throw new ReglaNegocioException("El Nickname o el Email ya se encuentran registrados en el sistema.");
        }

        boolean esDocente = instituto != null && !instituto.trim().isEmpty();

        // Si es Docente, el instituto tiene que existir: se valida ANTES de
        // persistir nada, para poder frenar el alta con un mensaje claro.
        if (esDocente) {
            Instituto institutoEntity = institutoRepository.buscarPorNombre(instituto.trim());
            if (institutoEntity == null) {
                throw new ReglaNegocioException("El instituto seleccionado no existe: " + instituto);
            }
        }

        // Copiamos la imagen (si se selecciono una) a la carpeta del
        // Servidor Central y guardamos la ruta final junto al usuario.
        String rutaImagenFinal = guardarImagenUsuario(nickname, imagen);

        if (esDocente) {
            usuarioRepository.guardarDocente(nickname, mail, nombre, apellido, fechaNac, rutaImagenFinal, instituto.trim(), contraseña);
        } else {
            usuarioRepository.guardarEstudiante(nickname, mail, nombre, apellido, fechaNac, rutaImagenFinal, contraseña);
        }
    }

    private static final String MSG_CREDENCIALES_INVALIDAS =
            "Nickname/Email o contraseña incorrectos. Por favor, reingrese sus datos.";

    /**
     * Caso de uso "Inicio de Sesion" (actor: Visitante). Verifica nickname o mail
     * + contrasena y devuelve los datos de sesion con el ROL del usuario:
     * DOCENTE si esta en la tabla de docentes, ESTUDIANTE en caso contrario.
     * El Administrador no inicia sesion en la web, asi que nunca se devuelve ese rol.
     *
     * Usuario inexistente y contrasena incorrecta dan EL MISMO mensaje, para no
     * revelar cuales nicknames/mails existen.
     *
     * @throws IllegalArgumentException si los datos no son validos.
     */
    public DTSesion autenticar(String identificador, String contraseña) {
        if (identificador == null || identificador.trim().isEmpty()
                || contraseña == null || contraseña.isEmpty()) {
            throw new ReglaNegocioException(MSG_CREDENCIALES_INVALIDAS);
        }

        Usuario u = usuarioRepository.buscarPorNicknameOMail(identificador.trim());
        // Usuarios cargados antes de existir la contrasena tienen null: no pueden entrar.
        String guardada = (u == null) ? null : u.getContraseña();

        boolean ok = guardada != null && java.security.MessageDigest.isEqual(
                guardada.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                contraseña.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        if (!ok) {
            throw new ReglaNegocioException(MSG_CREDENCIALES_INVALIDAS);
        }

        Rol rol = (u instanceof Docente) ? Rol.DOCENTE : Rol.ESTUDIANTE;
        return new DTSesion(u.getNickname(), u.getMail(), u.getNombreU(), u.getApellido(), u.getImagen(), rol);
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

    /**
     * @return los datos del usuario, o null si no existe.
     */
    public DTUsuario obtenerDataUsuario(String nickname, String mail) {
        Usuario u = usuarioRepository.buscarPorId(nickname, mail);
        if (u == null) {
            return null;
        }
        return new DTUsuario(
                u.getNickname(),
                u.getMail(),
                u.getNombreU(),
                u.getApellido(),
                u.getFechaNac(),
                u.getImagen()
        );
    }

    public List<DTUsuarioResumen> listarUsuariosTabla() {
        return aTabla(usuarioRepository.listarTodos());
    }

    public boolean esDocente(String nickname) {
        return docenteRepository.existe(nickname);
    }

    /**
     * Cada fila de la tabla de usuarios: nickname + mail.
     */
    private List<DTUsuarioResumen> aTabla(List<Usuario> lista) {
        List<DTUsuarioResumen> resultado = new ArrayList<>();
        for (Usuario u : lista) {
            resultado.add(new DTUsuarioResumen(u.getNickname(), u.getMail()));
        }
        return resultado;
    }

    /**
     * Consulta de Usuario desde el sitio web, donde se navega por nickname (es unico).
     * @return los datos del usuario, o null si no existe.
     */
    public DTUsuario obtenerPorNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return null;
        }
        Usuario u = usuarioRepository.buscarPorNicknameOMail(nickname.trim());
        if (u == null || !u.getNickname().equals(nickname.trim())) {
            return null;
        }
        return obtenerDataUsuario(u.getNickname(), u.getMail());
    }

    /**
     * Copia la imagen elegida (ruta local) a la carpeta de imagenes, con el nickname
     * como nombre para evitar colisiones.
     *
     * @return la ruta relativa a persistir, o null si no se eligio imagen.
     */
    private String guardarImagenUsuario(String nickname, DTImagen imagen) {
        return AlmacenImagenes.guardar(imagen, AlmacenImagenes.USUARIOS, nickname);
    }
}
