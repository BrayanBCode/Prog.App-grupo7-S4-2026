/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorCentral.Logica.entities.cursos;

import ServidorCentral.Logica.entities.usuarios.Docente;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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
        name = "INSTITUTO_NOMBRE",
        joinColumns = @JoinColumn(name = "INSTITUTO_NOMBRE", referencedColumnName = "NOMBRE"),
        inverseJoinColumns = {
            @JoinColumn(name = "DOCENTE_INSTITUTO", referencedColumnName = "NICKNAME"),
            @JoinColumn(name = "DOCENTE_MAIL", referencedColumnName = "MAIL")
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
