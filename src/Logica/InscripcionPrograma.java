/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.time.LocalDate;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author maida
 */
@Entity
public class InscripcionPrograma implements Serializable {
    //Atributos
    private LocalDate fechaInscripcion;
    
    //Forainge key
    @ManyToOne
     @JoinColumn(name="ProgramaFormacion_id",nullable=false)
    private ProgramaFormacion pFormacion;
    
    @ManyToOne
    @JoinColumn(name= "Estudiante_id",nullable = false)
    private Estudiante estudiante;
    
    //Metodos
    public void setEstudiante(Estudiante estudiante){this.estudiante=estudiante;}
    public void setpFormacion(ProgramaFormacion pformacion){this.pFormacion= pformacion;}
    
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
        if (!(object instanceof InscripcionPrograma)) {
            return false;
        }
        InscripcionPrograma other = (InscripcionPrograma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.InscripcionPrograma[ id=" + id + " ]";
    }
    
}
