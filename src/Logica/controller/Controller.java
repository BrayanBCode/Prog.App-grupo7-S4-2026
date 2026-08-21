package Logica.controller;

import Logica.cursos.*;
import Logica.usuarios.Docente;
import Logica.usuarios.Estudiante;
import Logica.programaFormacion.ProgramaFormacion;
import Logica.usuarios.Usuario;
import Logica.usuarios.UsuarioID;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import Persistencia.Conexion;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Controller implements IController {
    
    EntityManager em = Conexion.getInstancia().getEntityManager();
    
    @Override
    public List<String> obtenerDataDocente(String nickname) {

        try {
            // Cursos creados por el docente
            List<String> cursos = this.em.createQuery(
                "SELECT c.nombre FROM Curso c WHERE c.docente.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Ediciones asociadas al docente (relación ManyToMany)
            List<String> ediciones = this.em.createQuery(
                "SELECT DISTINCT e.nombre FROM EdicionCurso e JOIN e.docentes d WHERE d.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Programas de Formación que contienen cursos dictados por este docente
            List<String> programas = this.em.createQuery(
                "SELECT DISTINCT p.nombre FROM ProgramaFormacion p JOIN p.cursos c WHERE c.docente.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Formatea resultados para la vista
            List<String> resultado = new ArrayList<>();

            resultado.add("--- CURSOS ---");
            if (cursos.isEmpty()) resultado.add("(Sin cursos registrados)");
            else cursos.forEach(c -> resultado.add("- " + c));

            resultado.add("\n--- EDICIONES DE CURSOS ---");
            if (ediciones.isEmpty()) resultado.add("(Sin ediciones asignadas)");
            else ediciones.forEach(e -> resultado.add("- " + e));

            resultado.add("\n--- PROGRAMAS DE FORMACIÓN ---");
            if (programas.isEmpty()) resultado.add("(Sin programas vinculados)");
            else programas.forEach(p -> resultado.add("- " + p));

            return resultado;
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> obtenerEdicionesYProgramas(String nickname) {
        try {
            //  Ediciones de curso del estudiante
            List<String> ediciones = this.em.createQuery(
                "SELECT ie.edicionCurso.nombre FROM InscripcionEdicion ie WHERE ie.estudiante.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Programas de formación del estudiante
            List<String> programas = this.em.createQuery(
                "SELECT ip.pFormacion.nombre FROM InscripcionPrograma ip WHERE ip.estudiante.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Une resultados en una sola lista
            List<String> resultado = new ArrayList<>(ediciones);
            resultado.addAll(programas);
            return resultado;
        } finally {
            this.em.close();
        }
    }

    @Override
    public boolean esDocente(String nickname) {
        try {
            Long cantidad = this.em.createQuery(
                "SELECT COUNT(d) FROM Docente d WHERE d.nickname = :nick", Long.class)
                .setParameter("nick", nickname)
                .getSingleResult();

            return cantidad > 0; // Retorna TRUE si existe como docente
        } finally {
            this.em.close();
        }
    }

    @Override
    public String[] obtenerDataUsuario(String nickname, String mail) {
        try {
            UsuarioID id = new UsuarioID(nickname, mail);
            Usuario u = this.em.find(Usuario.class, id);
            if (u != null) {
                return new String[]{
                    u.getNickname(),
                    u.getMail(),
                    u.getNombreU(),
                    u.getApellido(),
                    u.getFechaNac() != null ? u.getFechaNac().toString() : ""
                };
            }
            return null;
        } finally {
            this.em.close();
        }
    }

    @Override
    public List<String[]> listarUsuariosTabla() {
        try {
            List<Usuario> lista = this.em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
            List<String[]> resultado = new ArrayList<>();
            for (Usuario u : lista) {
                resultado.add(new String[]{ u.getNickname(), u.getMail() });
            }
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.em.close();
        }
    }

    @Override
public void AltaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto) {
    try {
        // Busca si existe el NICKNAME en Docente o Estudiante (devolvemos String)
        List<String> nickDocente = this.em.createQuery("SELECT d.nickname FROM Docente d WHERE d.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

        List<String> nickEstudiante = this.em.createQuery("SELECT e.nickname FROM Estudiante e WHERE e.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

        // Buscar si existe el MAIL en Docente o Estudiante
        List<String> mailDocente = this.em.createQuery("SELECT d.Mail FROM Docente d WHERE d.Mail = :Mail", String.class)
                .setParameter("Mail", mail)
                .getResultList();

        List<String> mailEstudiante = this.em.createQuery("SELECT e.Mail FROM Estudiante e WHERE e.Mail = :Mail", String.class)
                .setParameter("Mail", mail)
                .getResultList();

        // Validar que todas las listas estén vacías
        if (nickDocente.isEmpty() && nickEstudiante.isEmpty() && mailDocente.isEmpty() && mailEstudiante.isEmpty()) {

            Usuario usuario;
            if (instituto != null && !instituto.trim().isEmpty()) {
                usuario = new Docente(nickname, mail, nombre, apellido, fechaNac, instituto);
            } else {
                usuario = new Estudiante(nickname, mail, nombre, apellido, fechaNac);
            }

            this.em.getTransaction().begin();
            this.em.persist(usuario);
            this.em.getTransaction().commit();

        } else {
            // Lanzamos una excepción no comprobada para que la interfaz gráfica (Swing) la capture y muestre la alerta al administrador
            throw new IllegalArgumentException("El Nickname o el Email ya se encuentran registrados en el sistema.");
        }

    } catch (Exception e) {
        if (this.em.getTransaction().isActive()) {
            this.em.getTransaction().rollback();
        }
        throw e; // Re-lanza la excepción hacia la vista Swing
    } finally {
        this.em.close();
    }
}

@Override
public void CrearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) throws Exception {
    try {
        ProgramaFormacion pf = this.em.find(ProgramaFormacion.class, nombre);
        if (pf != null) {
            throw new Exception("Ya existe un programa de formación registrado con el nombre: " + nombre);
        }
        this.em.getTransaction().begin();
        ProgramaFormacion nuevoPrograma = new ProgramaFormacion(nombre, descripcion, fechaInicio, fechaFin, fechaAlta);
        this.em.persist(nuevoPrograma);
        this.em.getTransaction().commit();
    } finally {
        if (this.em.getTransaction().isActive()) {
            this.em.getTransaction().rollback();
        }
        this.em.close();
    }
}

@Override
public void agregarCursoPrograma(String nombreP, String nombreC) throws Exception {
        try{ 
            ProgramaFormacion programa = this.em.find(ProgramaFormacion.class,nombreP);
            if(programa==null){
                throw new Exception("No existe un programa de formacion: "+ nombreP);
            }
            Curso curso = this.em.find(Curso.class, nombreC);
            if(curso == null){
                throw new Exception("No existe un curso con ese nombre: "+ nombreC);
            }
            if(programa.getCursos().contains(curso)){
                throw new Exception("El curso ya se encuentra en el programa de formacion seleccionado");
            }
            this.em.getTransaction().begin();
            programa.getCursos().add(curso);
            this.em.merge(programa);
            this.em.getTransaction().commit();
        }
        catch(Exception e){
            if(this.em.getTransaction().isActive());{
            this.em.getTransaction().rollback();
        }
            throw e;
        }finally{
            this.em.close();
        }
    }
    
    
    @Override
    public List<String> listarNombreProgramas(){
        try{
            return this.em.createQuery("SELECT p.nombre FROM ProgramaFormacion p ORDER BY p.nombre",String.class).getResultList();
        }finally {
            this.em.close();
        }
        
    }
     
   
    @Override
    public List<String> listarNombresCursos() {
    try {
        return this.em.createQuery(
            "SELECT c.nombre FROM Curso c ORDER BY c.nombre", String.class)
            .getResultList();
    } finally {
        this.em.close();
    }
}
}


