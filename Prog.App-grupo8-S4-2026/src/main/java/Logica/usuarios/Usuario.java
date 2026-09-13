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
   private String Imagen;
   
   //Constructor para JPA
   public Usuario() {}

    // Constructor completo
    public Usuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen) {
        this.nickname = nickname;
        this.Mail = mail;
        this.Nombre = nombre;
        this.Apellido = apellido;
        this.fechaNac = fechaNac;
        this.Imagen = imagen;
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
   public String getImagen(){return Imagen;}
   public String getNombreU(){return Nombre;}

    // Setters de los datos modificables (nickname y Mail NO se exponen: son @Id y no deben cambiar)
    public void setNombreU(String nombre) { this.Nombre = nombre; }
    public void setApellido(String apellido) { this.Apellido = apellido; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }
    public void setImagen(String imagen){this.Imagen = imagen;}

}