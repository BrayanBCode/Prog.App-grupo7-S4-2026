/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author maida
 */
@Entity
public class Docente extends Usuario {
    //Forainge key
    @ManyToMany
    private List<EdicionCurso> edicionesC= new ArrayList<>();
    
    @ManyToMany(mappedBy = "docentes")
    private List<Instituto> institutos = new ArrayList<>();
    
    @OneToMany(mappedBy="docente")
    private List<Curso> cursos = new ArrayList<>();
    // Constructor vacío necesario para JPA
    public Docente() {
        super();
    }

    // Constructor que llama a Usuario y asigna el instituto
    public Docente(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String institutoTrabajo) {
        super(nickname, mail, nombre, apellido, fechaNac);
        this.institutoTrabajo = institutoTrabajo;
    }
    
    private String institutoTrabajo;
    
}
