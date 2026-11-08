/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorCentral.Logica.entities.cursos;

import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.entities.usuarios.Docente;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String imagen;   // ruta relativa a la carpeta de imagenes (puede ser null)
    // Boolean y no boolean: en una base existente la columna se agrega vacia (null = abierta).
    private Boolean cerrada = false;
    private LocalDate fechaCierre;   // dia en que el docente la cerro (null si esta abierta)

    // Constructor vacío necesario para JPA
    public EdicionCurso() {
    }

    public EdicionCurso(String nombre, Curso curso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, LocalDate fechaPublicacion) {
        this.nombre = nombre;
        this.curso = curso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.fechaPublicacion = fechaPublicacion;
    }

    //Metodos
    public void setCurso(Curso curso){this.curso = curso;}
    public Curso getCurso(){return curso;}
    public List<Docente> getDocentes(){return docentes;}

    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}

    public LocalDate getFechaInicio(){return fechaInicio;}
    public void setFechaInicio(LocalDate fechaInicio){this.fechaInicio = fechaInicio;}

    public LocalDate getFechaFin(){return fechaFin;}
    public void setFechaFin(LocalDate fechaFin){this.fechaFin = fechaFin;}

    public int getCupo(){return cupo;}
    public void setCupo(int cupo){this.cupo = cupo;}

    public LocalDate getFechaPublicacion(){return fechaPublicacion;}
    public void setFechaPublicacion(LocalDate fechaPublicacion){this.fechaPublicacion = fechaPublicacion;}

    public String getImagen(){return imagen;}
    public void setImagen(String imagen){this.imagen = imagen;}

    public List<InscripcionEdicion> getInscripciones(){return inscripciones;}

    public boolean isCerrada(){return Boolean.TRUE.equals(cerrada);}

    public LocalDate getFechaCierre(){return fechaCierre;}

    /**
     * Cierra la edicion (letra 2026, "Ingresar resultados de evaluaciones"): se hace luego de ingresar todas
     * las evaluaciones, asi que exige nota en cada estudiante aceptado. Despues no se pueden modificar las notas.
     */
    public void cerrar() {
        if (isCerrada()) {
            throw new ReglaNegocioException("La edición ya está cerrada.");
        }
        boolean faltanNotas = inscripciones.stream()
                .anyMatch(i -> i.getEstado() == EstadoInscripcion.ACEPTADA && i.getNota() == null);
        if (faltanNotas) {
            throw new ReglaNegocioException("Faltan notas: hay que calificar a todos los aceptados antes de cerrar.");
        }
        this.cerrada = true;
        this.fechaCierre = LocalDate.now();
    }

    private static final long serialVersionUID = 1L;

    // equals/hashCode basados en 'nombre', que es el verdadero @Id de la entidad.
    // (Antes había un campo Long id" suelto, sin @Id y nunca asignado: quedaba
    // siempre null, así que TODAS las instancias de EdicionCurso se consideraban
    // "iguales" entre sí -> rompía contains()/remove() en listas y combos.)
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdicionCurso)) {
            return false;
        }
        EdicionCurso other = (EdicionCurso) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.EdicionCurso[ nombre=" + nombre + " ]";
    }
    
}