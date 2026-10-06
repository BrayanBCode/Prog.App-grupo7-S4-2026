package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.Instituto;
import ServidorCentral.Logica.entities.usuarios.Docente;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.entities.usuarios.Usuario;
import ServidorCentral.Logica.entities.usuarios.UsuarioID;
import ServidorCentral.Persistencia.Conexion;

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

    /**
     * Para el Inicio de Sesion: el visitante escribe su nickname O su correo.
     * Usuario es abstracta (TABLE_PER_CLASS), por eso se consulta Docente y
     * Estudiante por separado, igual que en existeNickname / existeMail.
     *
     * @return el Docente o Estudiante que coincida, o null si no hay ninguno.
     */
    public Usuario buscarPorNicknameOMail(String identificador) {
        EntityManager em = conexion.getEntityManager();
        try {
            List<Docente> docentes = em.createQuery(
                            "SELECT d FROM Docente d WHERE d.nickname = :id OR d.Mail = :id", Docente.class)
                    .setParameter("id", identificador)
                    .setMaxResults(1)
                    .getResultList();
            if (!docentes.isEmpty()) {
                return docentes.get(0);
            }
            List<Estudiante> estudiantes = em.createQuery(
                            "SELECT e FROM Estudiante e WHERE e.nickname = :id OR e.Mail = :id", Estudiante.class)
                    .setParameter("id", identificador)
                    .setMaxResults(1)
                    .getResultList();
            return estudiantes.isEmpty() ? null : estudiantes.get(0);
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
    public void guardarEstudiante(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen,String contraseña) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Estudiante estudiante = new Estudiante(nickname, mail, nombre, apellido, fechaNac, imagen,contraseña);
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
    public void guardarDocente(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String imagen, String nombreInstituto,String Contraseña) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Docente docente = new Docente(nickname, mail, nombre, apellido, fechaNac, imagen,Contraseña);
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
