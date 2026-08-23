/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
    
    
    //Metodos
    public void setNombreC(String nombre){this.nombre = nombre;}
    public void setInstituto(Instituto instituto){this.instituto = instituto;}
    public void setDocente(Docente docente){this.docente = docente;}
    
  
}
