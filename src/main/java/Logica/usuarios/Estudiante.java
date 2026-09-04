/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.usuarios;

import Logica.cursos.InscripcionEdicion;
import Logica.programaFormacion.InscripcionPrograma;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author maida
 */
@Entity
public class Estudiante extends Usuario {
    //Forainge key
    @OneToMany(mappedBy = "estudiante")
    private List<InscripcionEdicion> inscripciones = new ArrayList<>();
    
    @OneToMany(mappedBy= "estudiante")
    private List<InscripcionPrograma> pInscripcion = new ArrayList<>();
    // Constructor vacío necesario para JPA
    public Estudiante() {
        super();
    }

    // Constructor completo que invoca a la clase padre (Usuario)
    public Estudiante(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen) {
        super(nickname, mail, nombre, apellido, fechaNac, imagen);
    }
    
    // Retorna la clave compuesta heredada de Usuario
    public UsuarioID getID() {
        return super.getId();
    }
    
    //Metodos
    public String getNombre(){return super.getNombreU();} //El super es para heredar el comportamiento del padre (poder acceder al atributo Nombre)
    public List<InscripcionEdicion> getInscripciones(){return inscripciones;}
    
    
    
}
