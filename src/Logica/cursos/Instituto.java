/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.cursos;

import Logica.usuarios.Docente;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;

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
       private List<Docente>docentes = new ArrayList<>();
       
       
       @OneToMany(mappedBy = "instituto")
       private List<Curso> cursos = new ArrayList<>();
       
      public void setNombre(String nombre){this.nombre = nombre;}
      public String getNombre() {return nombre;}
      
      

}
