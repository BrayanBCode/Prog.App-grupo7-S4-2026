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

    private final Conexion conexion = Conexion.getInstancia();

    @Override
    public List<String> obtenerDataDocente(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            // Cursos creados por el docente
            List<String> cursos = em.createQuery(
                "SELECT c.nombre FROM Curso c WHERE c.docente.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Ediciones asociadas al docente (relación ManyToMany)
            List<String> ediciones = em.createQuery(
                "SELECT DISTINCT e.nombre FROM EdicionCurso e JOIN e.docentes d WHERE d.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Programas de Formación que contienen cursos dictados por este docente
            List<String> programas = em.createQuery(
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
        EntityManager em = conexion.getEntityManager();
        try {
            //  Ediciones de curso del estudiante
            List<String> ediciones = em.createQuery(
                "SELECT ie.edicionCurso.nombre FROM InscripcionEdicion ie WHERE ie.estudiante.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Programas de formación del estudiante
            List<String> programas = em.createQuery(
                "SELECT ip.pFormacion.nombre FROM InscripcionPrograma ip WHERE ip.estudiante.nickname = :nick", String.class)
                .setParameter("nick", nickname)
                .getResultList();

            // Une resultados en una sola lista
            List<String> resultado = new ArrayList<>(ediciones);
            resultado.addAll(programas);
            return resultado;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean esDocente(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                "SELECT COUNT(d) FROM Docente d WHERE d.nickname = :nick", Long.class)
                .setParameter("nick", nickname)
                .getSingleResult();

            return cantidad > 0; // Retorna TRUE si existe como docente
        } finally {
            em.close();
        }
    }

    @Override
    public String[] obtenerDataUsuario(String nickname, String mail) {
        EntityManager em = conexion.getEntityManager();
        try {
            UsuarioID id = new UsuarioID(nickname, mail);
            Usuario u = em.find(Usuario.class, id);
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
            em.close();
        }
    }

    @Override
    public List<String[]> listarUsuariosTabla() {
        EntityManager em = conexion.getEntityManager();
        try {
            List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
            List<String[]> resultado = new ArrayList<>();
            for (Usuario u : lista) {
                resultado.add(new String[]{ u.getNickname(), u.getMail() });
            }
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void AltaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto) {
        EntityManager em = conexion.getEntityManager();
        try {
            // Busca si existe el NICKNAME en Docente o Estudiante (devolvemos String)
            List<String> nickDocente = em.createQuery("SELECT d.nickname FROM Docente d WHERE d.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();

            List<String> nickEstudiante = em.createQuery("SELECT e.nickname FROM Estudiante e WHERE e.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();

            // Buscar si existe el MAIL en Docente o Estudiante
            List<String> mailDocente = em.createQuery("SELECT d.Mail FROM Docente d WHERE d.Mail = :Mail", String.class)
                    .setParameter("Mail", mail)
                    .getResultList();

            List<String> mailEstudiante = em.createQuery("SELECT e.Mail FROM Estudiante e WHERE e.Mail = :Mail", String.class)
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

                em.getTransaction().begin();
                em.persist(usuario);
                em.getTransaction().commit();

            } else {
                // Lanzamos una excepción no comprobada para que la interfaz gráfica (Swing) la capture y muestre la alerta al administrador
                throw new IllegalArgumentException("El Nickname o el Email ya se encuentran registrados en el sistema.");
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e; // Re-lanza la excepción hacia la vista Swing
        } finally {
            em.close();
        }
    }

    @Override
    public void ModificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception {
        EntityManager em = conexion.getEntityManager();
        try {
            UsuarioID id = new UsuarioID(nickname, mail);
            Usuario u = em.find(Usuario.class, id);

            if (u == null) {
                throw new Exception("El usuario no existe (puede haber sido eliminado por otro administrador).");
            }

            em.getTransaction().begin();
            // nickname y mail son la clave (@Id) y no se tocan: solo se actualizan los datos básicos
            u.setNombreU(nombre);
            u.setApellido(apellido);
            u.setFechaNac(fechaNac);
            em.merge(u);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void CrearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) throws Exception {
        EntityManager em = conexion.getEntityManager();
        try {
            ProgramaFormacion pf = em.find(ProgramaFormacion.class, nombre);
            if (pf != null) {
                throw new Exception("Ya existe un programa de formación registrado con el nombre: " + nombre);
            }
            em.getTransaction().begin();
            ProgramaFormacion nuevoPrograma = new ProgramaFormacion(nombre, descripcion, fechaInicio, fechaFin, fechaAlta);
            em.persist(nuevoPrograma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> listarNombreProgramas() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery("SELECT p.nombre FROM ProgramaFormacion p ORDER BY p.nombre", String.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> listarNombresCursos() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                "SELECT c.nombre FROM Curso c ORDER BY c.nombre", String.class)
                .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<String[]> listarDocentesTabla() {
        EntityManager em = conexion.getEntityManager();
        try {
            List<Docente> lista = em.createQuery("SELECT d FROM Docente d", Docente.class).getResultList();
            List<String[]> resultado = new ArrayList<>();
            for (Docente d : lista) {
                // {0}=nickname (lo usamos como identificador), {1}=texto a mostrar en la lista
                resultado.add(new String[]{ d.getNickname(), d.getNombreU() + " " + d.getApellido() + " (" + d.getNickname() + ")" });
            }
            return resultado;
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> listarNombresInstitutos() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                "SELECT i.nombre FROM Instituto i ORDER BY i.nombre", String.class)
                .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<String[]> listarCursosTabla(String nombreInstituto) {
        EntityManager em = conexion.getEntityManager();
        try {
            // Acá se usa la relación Curso -> Instituto para filtrar
            List<Curso> lista = em.createQuery(
                "SELECT c FROM Curso c WHERE c.instituto.nombre = :inst ORDER BY c.nombre", Curso.class)
                .setParameter("inst", nombreInstituto)
                .getResultList();

            List<String[]> resultado = new ArrayList<>();
            for (Curso c : lista) {
                resultado.add(new String[]{ c.getNombreC(), c.getDescripcion() });
            }
            return resultado;
        } finally {
            em.close();
        }
    }

    @Override
    public void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception {
        EntityManager em = conexion.getEntityManager();
        try {
            // El nombre de la edición es único (es su @Id)
            EdicionCurso existente = em.find(EdicionCurso.class, nombreEdicion);
            if (existente != null) {
                throw new Exception("Ya existe una edición de curso registrada con el nombre: " + nombreEdicion);
            }

            Curso curso = em.find(Curso.class, nombreCurso);
            if (curso == null) {
                throw new Exception("El curso seleccionado no existe.");
            }

            if (fechaFin.isBefore(fechaInicio)) {
                throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio.");
            }

            em.getTransaction().begin();

            EdicionCurso edicion = new EdicionCurso(nombreEdicion, curso, fechaInicio, fechaFin, cupo, LocalDate.now());
            em.persist(edicion);

            // La relación ManyToMany EdicionCurso<->Docente la maneja Docente
            // (lado dueño), así que hay que agregar la edición ahí, no al revés.
            for (String nickname : nicknamesDocentes) {
                Docente docente = em.createQuery(
                        "SELECT d FROM Docente d WHERE d.nickname = :nick", Docente.class)
                        .setParameter("nick", nickname)
                        .getSingleResult();
                docente.getEdicionesC().add(edicion);
                em.merge(docente);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    @Override
    public void agregarCursoPrograma(String nombreP, String nombreC) throws Exception {
        EntityManager em = conexion.getEntityManager();
        try {
            //BUSCO EL PROGRAMA EN LA LISTA
            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombreP);
            //VERIFICO SI ES VACIO
            if (programa == null) {
                throw new Exception("No existe un programa de formacion: " + nombreP);
            }
            //VEO SI TIENE CURSOS
            Curso curso = em.find(Curso.class, nombreC);
            if (curso == null) {
                throw new Exception("No existe un curso con ese nombre: " + nombreC);
            }
            //SI EL PROGRAMA CONTIENE EL CURSO A INGRESAR LE MANDO 
            if (programa.getCursos().contains(curso)) {
                throw new Exception("El curso ya se encuentra en el programa de formacion seleccionado");
            }
            //AGREGO EL CURSO A LA LISTA DE LOS PROGRAMA
            em.getTransaction().begin();
            programa.getCursos().add(curso);
            em.merge(programa);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override 
    public List<String> obtenerDataPrograma(String nombrePrograma) throws Exception{
        EntityManager em = conexion.getEntityManager();
        try{
            //BUSCO EL PROGRAMA
            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombrePrograma);
            if(programa == null){
                throw new Exception("No Existe un Programa de Formacion con ese nombre"+nombrePrograma);
            }
        //CREO UNA LISTA RESULTADO Y LE AGREGO LA DATA A OBTENER
        List<String> resultado = new ArrayList<>();
        resultado.add("Nombre: "+ programa.getNombre());
        resultado.add("Descripcion: " + programa.getDescripcion());
        resultado.add("Fecha Inicio: "+ programa.getFechaInicio());
        resultado.add("Fecha Fin"+ programa.getFechaFin());
        resultado.add("Fecha Alta"+ programa.getFechaAlta());

        resultado.add("Cursos");
        //CREO LA LISTA CURSO PARA MOSTRAR LOS CURSOS ASIGNADOS AL PROGRAMA
        List<Curso> curso = new ArrayList<>();
        if(curso.isEmpty()){
            resultado.add("Sin Cursos Registrados");
        }else{
            for(Curso c: curso){
                resultado.add("-"+c.getNombreC());
            }
            }
        return resultado;
        }
        finally {
            em.close();
        }
    }
}
