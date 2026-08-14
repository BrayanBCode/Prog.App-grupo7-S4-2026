/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    //Metodos
    public String getNombre(){return super.getNombreU();} //El super es para heredar el comportamiento del padre (poder acceder al atributo Nombre)
    public List<InscripcionEdicion> getInscripciones(){return inscripciones;}
    
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
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Estudiante[ id=" + id + " ]";
    }
    
}
