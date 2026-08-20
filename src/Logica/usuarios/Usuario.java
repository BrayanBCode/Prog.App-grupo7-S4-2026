/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.usuarios;

import java.time.LocalDate;
import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author maida
 */
@Entity
@IdClass(UsuarioID.class)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Usuario implements Serializable {
    //Atributos
   @Id private String nickname;
   @Id private String Mail; 
   private String Apellido;
   private String Nombre;
   private LocalDate fechaNac;
   
   //Constructor para JPA
   public Usuario() {}

    // Constructor completo
    public Usuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) {
        this.nickname = nickname;
        this.Mail = mail;
        this.Nombre = nombre;
        this.Apellido = apellido;
        this.fechaNac = fechaNac;
    }
    // Getters para los campos ID
    public String getNickname() { return nickname; }
    public String getMail() { return Mail; }

    // Método que devuelve el objeto de clave compuesta UsuarioID
    public UsuarioID getId() {
        return new UsuarioID(nickname, Mail);
    }
    public String getApellido(){return Apellido;}
    public LocalDate getFechaNac(){return fechaNac;}
   
   public String getNombreU(){return Nombre;}
   
    
}
