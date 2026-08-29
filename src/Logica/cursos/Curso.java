/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java 
 */
package Logica.cursos;

import Logica.programaFormacion.ProgramaFormacion;
import Logica.usuarios.Docente;

import java.util.List;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;

/**
 *
 * @author maida
 */
@Entity
public class Curso implements Serializable {
    //Atributos
    @Id private String nombre;
    private String descripcion;
    private int duracion;
    private float canthoras;
    private int cantCreditos;
    private LocalDate fechaRegistro;
    private String url;
    
    //Forainge key
    @ManyToOne
    private Instituto instituto;
    
    @ManyToOne
    private Docente docente;
    
    @OneToMany(mappedBy="curso")
    private List<EdicionCurso> edCursos= new ArrayList<>();
    
    @ManyToMany(mappedBy="cursos")
    private List<ProgramaFormacion> pFormaciones = new ArrayList<>();
    
    //AutoRelacion foraing key
    
    @ManyToMany
    private List<Curso> previas = new ArrayList<>();
    
    //Lado Inverso
    @ManyToMany(mappedBy="previas")
    private List<Curso> esPreviaDe = new ArrayList<>();
    
    //Metodos
    public List<Curso> getPrevias(){return previas;}
    public String getNombreC(){return nombre;}
    public Instituto getInstituto() {return instituto;}
    public String getDescripcion(){return descripcion;}
    public int getDuracion(){return duracion;}
    public float getCantHoras(){return canthoras;}
    public int getCantCreditos(){return cantCreditos;}
    public LocalDate getFechaRegistro(){return fechaRegistro;}
    public String getUrl(){return url;}
    public Docente getDocente(){return docente;}
    public List<Curso> getEsPreviaDe(){return esPreviaDe;}
    
    
    //Metodos
    public void setNombreC(String nombre){this.nombre = nombre;}
    public void setInstituto(Instituto instituto){this.instituto = instituto;}
    public void setDocente(Docente docente){this.docente = docente;}
    public void setDescripcion(String descripcion){this.descripcion = descripcion;}
    public void setDuracion(int duracion){this.duracion = duracion;}
    public void setCantHoras(float canthoras){this.canthoras = canthoras;}
    public void setCantCreditos(int cantCreditos){this.cantCreditos = cantCreditos;}
    public void setFechaRegistro(LocalDate fechaRegistro){this.fechaRegistro = fechaRegistro;}
    public void setUrl(String url){this.url = url;}

    // Constructor vacío obligatorio para JPA (al declarar el completo de abajo,
    // Java deja de generar uno implícito, así que hay que escribirlo a mano)
    public Curso() {}

    // Constructor para Alta de Curso
    public Curso(String nombre, String descripcion, int duracion, float canthoras, int cantCreditos,
                 LocalDate fechaRegistro, String url, Instituto instituto, Docente docente) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.canthoras = canthoras;
        this.cantCreditos = cantCreditos;
        this.fechaRegistro = fechaRegistro;
        this.url = url;
        this.instituto = instituto;
        this.docente = docente;
    }
  
}