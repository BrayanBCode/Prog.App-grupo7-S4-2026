/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;
import java.time.LocalDate;
import java.io.Serializable;
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
public class ProgramaFormacion implements Serializable {
    //Atributos
    @Id private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    //Forainge key
    @OneToMany(mappedBy = "pFormacion")
    private List<InscripcionPrograma> pInscripciones = new ArrayList<>();
    
    @ManyToMany
    private List<Curso> cursos = new ArrayList<>();
    
    //Metodos
    public void setNombre(String nombre){this.nombre = nombre;}
    public List<Curso> getCursos(){return cursos;}
    
    
    private static final long serialVersionUID = 1L;
    
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
        if (!(object instanceof ProgramaFormacion)) {
            return false;
        }
        ProgramaFormacion other = (ProgramaFormacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.ProgramaFormacion[ id=" + id + " ]";
    }
    
}
