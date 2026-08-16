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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author maida
 */
@Entity
public class EdicionCurso implements Serializable {
       //Forainge key
       @OneToMany(mappedBy="edicionCurso")
       private List<InscripcionEdicion> inscripciones = new ArrayList<>();
       //Forainge key
       @ManyToMany(mappedBy="edicionesC")
       private List<Docente>docentes = new ArrayList<>();
       
       @ManyToOne 
       private Curso curso;
       
    //Atributos
    @Id private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupo = 0;
    private LocalDate fechaPublicacion;
    
    //Metodos
    
    public void setCurso(Curso curso){this.curso = curso;}
    public List<Docente> getDocentes(){return docentes;}
    
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
        if (!(object instanceof EdicionCurso)) {
            return false;
        }
        EdicionCurso other = (EdicionCurso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.EdicionCurso[ id=" + id + " ]";
    }
    
}
