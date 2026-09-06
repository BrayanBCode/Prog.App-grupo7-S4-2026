package Logica.cursos;

import Logica.usuarios.Estudiante;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class InscripcionEdicion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ESTUDIANTE_NICKNAME", referencedColumnName = "NICKNAME"),
            @JoinColumn(name = "ESTUDIANTE_MAIL", referencedColumnName = "MAIL")
    })
    private Estudiante estudiante;

    @ManyToOne
    private EdicionCurso edicionCurso;

    private LocalDate fechaInscripcion;

    // Constructores
    public InscripcionEdicion() {}

    public InscripcionEdicion(LocalDate fechaInscripcion, EdicionCurso edicionCurso, Estudiante estudiante) {
        this.fechaInscripcion = fechaInscripcion;
        this.edicionCurso = edicionCurso;
        this.estudiante = estudiante;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public Estudiante getEstudiante() { return estudiante; }

    public void setEdicionCurso(EdicionCurso edicionCurso) { this.edicionCurso = edicionCurso; }
    public EdicionCurso getEdicionCurso() { return edicionCurso; }

    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InscripcionEdicion)) {
            return false;
        }
        InscripcionEdicion other = (InscripcionEdicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.InscripcionEdicion[ id=" + id + " ]";
    }
}