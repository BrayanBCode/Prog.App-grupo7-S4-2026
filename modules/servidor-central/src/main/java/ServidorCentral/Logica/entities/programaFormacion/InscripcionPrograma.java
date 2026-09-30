/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorCentral.Logica.entities.programaFormacion;


import ServidorCentral.Logica.entities.usuarios.Estudiante;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author maida
 */
@Entity
public class InscripcionPrograma implements Serializable {
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ESTUDIANTE_NICKNAME", referencedColumnName = "NICKNAME"),
        @JoinColumn(name = "ESTUDIANTE_MAIL", referencedColumnName = "MAIL")
    })
    private Estudiante estudiante;

    @ManyToOne
    private ProgramaFormacion pFormacion;

    private java.time.LocalDate fechaInscripcion;

    public InscripcionPrograma() {}
    //Metodos
    public void setEstudiante(Estudiante estudiante){this.estudiante=estudiante;}
    public void setpFormacion(ProgramaFormacion pformacion){this.pFormacion= pformacion;}
    public void setFechaInscripcion(LocalDate fechaInsc){this.fechaInscripcion= fechaInsc;}
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
