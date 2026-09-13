/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java 
 */
package Logica.usuarios;

import Logica.cursos.Curso;
import Logica.cursos.EdicionCurso;
import Logica.cursos.Instituto;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author maida
 */
@Entity
public class Docente extends Usuario {
    //Forainge key
    @ManyToMany
    private List<EdicionCurso> edicionesC= new ArrayList<>();
    
    @ManyToMany(mappedBy = "docentes")
    private List<Instituto> institutos = new ArrayList<>();
    
    @OneToMany(mappedBy="docente")
    private List<Curso> cursos = new ArrayList<>();
    // Constructor vacío necesario para JPA
    public Docente() {
        super();
    }

    // Constructor que llama a Usuario. La relación con el Instituto se arma
    // desde afuera (Controller) usando el lado dueño de la relación
    // (Instituto.getDocentes().add(docente)), no con un campo String suelto.
    public Docente(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen) {
        super(nickname, mail, nombre, apellido, fechaNac, imagen);
    }

    public List<EdicionCurso> getEdicionesC(){return edicionesC;}
    public List<Instituto> getInstitutos(){return institutos;}
    
}