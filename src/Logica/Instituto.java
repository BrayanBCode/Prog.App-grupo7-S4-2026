/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
       
       @ManyToOne
       private Facultad facultad;
       
       @OneToMany(mappedBy = "instituto")
       private List<Curso> cursos = new ArrayList<>();
       
      public void setNombre(String nombre){this.nombre = nombre;}
      
      public void setFacultad(Facultad facultad){this.facultad = facultad;}
      

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Instituto)) {
            return false;
        }
        Instituto other = (Instituto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Instituto[ id=" + id + " ]";
    }
    
}
