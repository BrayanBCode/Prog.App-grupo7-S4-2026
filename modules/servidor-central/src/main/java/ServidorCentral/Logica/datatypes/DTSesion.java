package ServidorCentral.Logica.datatypes;

import ServidorCentral.Logica.seguridad.Rol;

import java.io.Serializable;

/**
 * Datos del usuario que acaba de iniciar sesion. Es lo unico que el Servidor
 * Web guarda en la HttpSession (nunca la contrasena ni entidades JPA).
 * Es una clase con getters (y no un record) para poder usarla desde EL/JSP:
 * ${usuarioActual.nombre}.
 */
public final class DTSesion implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nickname;
    private final String mail;
    private final String nombre;
    private final String apellido;
    private final String imagen;
    private final Rol rol;

    public DTSesion(String nickname, String mail, String nombre, String apellido, String imagen, Rol rol) {
        this.nickname = nickname;
        this.mail = mail;
        this.nombre = nombre;
        this.apellido = apellido;
        this.imagen = imagen;
        this.rol = rol;
    }

    public String getNickname() { return nickname; }
    public String getMail() { return mail; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getImagen() { return imagen; }
    public Rol getRol() { return rol; }
}
