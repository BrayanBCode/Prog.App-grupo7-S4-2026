/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.usuarios;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author maida
 */
public class UsuarioID implements Serializable{
    private String nickname;
    private String Mail;

    // Constructor vacío obligatorio
    public UsuarioID() {}

    public UsuarioID(String nickname, String mail) {
        this.nickname = nickname;
        this.Mail = mail;
    }

    // Getters y Setters
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getMail() { return Mail; }
    public void setMail(String mail) { this.Mail = mail; }

    // En claves compuestas es OBLIGATORIO implementar equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioID usuarioID = (UsuarioID) o;
        return Objects.equals(nickname, usuarioID.nickname) && Objects.equals(Mail, usuarioID.Mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, Mail);
    }
}
