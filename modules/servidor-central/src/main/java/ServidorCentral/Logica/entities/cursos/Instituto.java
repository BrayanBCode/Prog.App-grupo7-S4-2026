/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorCentral.Logica.entities.cursos;

import ServidorCentral.Logica.entities.usuarios.Docente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maida
 */
@Entity
public class Instituto implements Serializable {
    //Atributos
    @Id private String nombre;
    
    //Forainge key
    @ManyToMany
    @JoinTable(
            name = "instituto_docente",
            joinColumns = @JoinColumn(name = "instituto_nombre", referencedColumnName = "NOMBRE"),
            inverseJoinColumns = {
                    @JoinColumn(name = "docente_nickname", referencedColumnName = "NICKNAME"),
                    @JoinColumn(name = "docente_mail", referencedColumnName = "MAIL")
            }
    )
    private List<Docente> docentes = new ArrayList<>();
    @OneToMany(mappedBy = "instituto")
    private List<Curso> cursos = new ArrayList<>();

    public Instituto() {}

    public Instituto(String name) {
        this.nombre = name;
    }

    public void setNombre(String nombre){this.nombre = nombre;}
    public String getNombre() {return nombre;}

    // Lado dueño de la relación ManyToMany con Docente: sin este getter no
    // había forma de vincular nunca un Docente a un Instituto.
    public List<Docente> getDocentes(){return docentes;}
    public List<Curso> getCursos(){return cursos;}

}
