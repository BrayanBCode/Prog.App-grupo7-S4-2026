/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.programaFormacion;



import Logica.programaFormacion.ProgramaFormacion;
import Logica.usuarios.Estudiante;
import java.time.LocalDate;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

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
