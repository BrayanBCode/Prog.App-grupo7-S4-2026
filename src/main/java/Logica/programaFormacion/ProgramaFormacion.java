 package Logica.programaFormacion;

import Logica.cursos.Curso;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class ProgramaFormacion implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // --- ATRIBUTOS ---
    @Id 
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaAlta;
    
    // --- RELACIONES ---
    @OneToMany(mappedBy = "pFormacion")
    private List<InscripcionPrograma> pInscripciones = new ArrayList<>();
    
   
    @ManyToMany
    private List<Curso> cursos = new ArrayList<>();
    
    // --- CONSTRUCTORES ---
    public ProgramaFormacion() {}

    public ProgramaFormacion(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
    }
    
    // --- GETTERS Y SETTERS ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public List<Curso> getCursos() { return cursos; }
    public void setCursos(List<Curso> cursos) { this.cursos = cursos; }

    public List<InscripcionPrograma> getPInscripciones() { return pInscripciones; }
    public void setPInscripciones(List<InscripcionPrograma> pInscripciones) { this.pInscripciones = pInscripciones; }
    
    
    
    // --- EQUALS, HASHCODE Y TOSTRING ---
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaFormacion)) {
            return false;
        }
        ProgramaFormacion other = (ProgramaFormacion) object;
        if ((this.nombre == null && other.nombre != null) || 
            (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
    public void agregarCurso(Curso curso){
    if (curso == null) {
        throw new IllegalArgumentException("Seleccione un curso");
    }
    if (this.cursos.contains(curso)) {
        throw new IllegalArgumentException("El curso ya está integrado en este programa");
    }
    this.cursos.add(curso);
    
    // Mantiene la consistencia bidireccional en memoria
    if (!curso.getProgramas().contains(this)) {
        curso.getProgramas().add(this);
    }
}
}