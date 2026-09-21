package Logica.repositories;

import Logica.entities.cursos.Instituto;
import Logica.entities.usuarios.Docente;
import Logica.entities.usuarios.Estudiante;
import Logica.entities.usuarios.Usuario;
import Logica.entities.usuarios.UsuarioID;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository de Usuario (Docente + Estudiante). Es la unica clase que
 * habla con la base para estas entidades: no valida nada, solo lee y
 * escribe. Las reglas ("¿ya existe el nickname?") viven en UsuarioService.
 *
 * OJO: Usuario es abstracta y usa TABLE_PER_CLASS, ademas de clave
 * compuesta (nickname + Mail). Por eso, para los chequeos de existencia
 * consultamos por separado Docente y Estudiante -- igual que hacia
 * ControllerV1 -- en vez de contar sobre Usuario.
 */
public class UsuarioRepository {

    private final Conexion conexion;

    public UsuarioRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    public boolean existeNickname(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            List<String> docentes = em.createQuery(
                            "SELECT d.nickname FROM Docente d WHERE d.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
            List<String> estudiantes = em.createQuery(
                            "SELECT e.nickname FROM Estudiante e WHERE e.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
            return !docentes.isEmpty() || !estudiantes.isEmpty();
        } finally {
            em.close();
        }
    }

    public boolean existeMail(String mail) {
        EntityManager em = conexion.getEntityManager();
        try {
            List<String> docentes = em.createQuery(
                            "SELECT d.Mail FROM Docente d WHERE d.Mail = :mail", String.class)
                    .setParameter("mail", mail)
                    .getResultList();
            List<String> estudiantes = em.createQuery(
                            "SELECT e.Mail FROM Estudiante e WHERE e.Mail = :mail", String.class)
                    .setParameter("mail", mail)
                    .getResultList();
            return !docentes.isEmpty() || !estudiantes.isEmpty();
        } finally {
            em.close();
        }
    }

    /**
     * @return el Usuario (Docente o Estudiante) con esa clave compuesta, o null.
     */
    public Usuario buscarPorId(String nickname, String mail) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(Usuario.class, new UsuarioID(nickname, mail));
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Da de alta un Estudiante. Todo dentro de la misma transaccion.
     */
    public void guardarEstudiante(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Estudiante estudiante = new Estudiante(nickname, mail, nombre, apellido, fechaNac, imagen);
            estudiante.setImagen(imagen);
            em.persist(estudiante);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    /**
     * Da de alta un Docente y lo vincula al Instituto en la misma
     * transaccion. Instituto.docentes es el lado DUEÑO de la relacion
     * ManyToMany: si no se agrega ahi, el docente queda creado pero
     * suelto (y nunca podria dictar cursos).
     */
    public void guardarDocente(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen, String nombreInstituto) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Docente docente = new Docente(nickname, mail, nombre, apellido, fechaNac, imagen);
            docente.setImagen(imagen);
            em.persist(docente);

            Instituto instituto = em.find(Instituto.class, nombreInstituto);
            if (instituto != null) {
                instituto.getDocentes().add(docente);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza SOLO los datos modificables: nickname y Mail son la @Id
     * compuesta y no se tocan nunca.
     */
    public void actualizarDatosBasicos(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario u = em.find(Usuario.class, new UsuarioID(nickname, mail));
            u.setNombreU(nombre);
            u.setApellido(apellido);
            u.setFechaNac(fechaNac);
            em.merge(u);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
