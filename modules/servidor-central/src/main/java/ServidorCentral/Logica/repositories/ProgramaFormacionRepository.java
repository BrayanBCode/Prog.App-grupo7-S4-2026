package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.programaFormacion.InscripcionPrograma;
import ServidorCentral.Logica.entities.programaFormacion.ProgramaFormacion;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class ProgramaFormacionRepository {
    private final Conexion conexion;

    public ProgramaFormacionRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    public List<String> obtenerPorCurso(String nombreCurso) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT p.nombre FROM ProgramaFormacion p JOIN p.cursos c WHERE c.nombre = :nombreCurso ORDER BY p.nombre", String.class)
                    .setParameter("nombreCurso", nombreCurso)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public ProgramaFormacion obtenerPorNombre(String nombrePrograma) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(ProgramaFormacion.class, nombrePrograma);
        } finally {
            em.close();
        }
    }

    public boolean existe(String nombrePrograma) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(ProgramaFormacion.class, nombrePrograma) != null;
        } finally {
            em.close();
        }
    }

    public List<ProgramaFormacion> listarTodos() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM ProgramaFormacion p ORDER BY p.nombre", ProgramaFormacion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombres() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p.nombre FROM ProgramaFormacion p ORDER BY p.nombre", String.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void guardar(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            ProgramaFormacion nuevo = new ProgramaFormacion(nombre, descripcion, fechaInicio, fechaFin, fechaAlta);
            em.persist(nuevo);

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
     * El nombre es la @Id del programa: no se modifica, se usa para
     * ubicarlo. Solo cambian descripcion y fechas.
     */
    public void actualizar(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombre);
            programa.setDescripcion(descripcion);
            programa.setFechaInicio(fechaInicio);
            programa.setFechaFin(fechaFin);
            em.merge(programa);

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
     * Agrega un curso ya existente al programa. Busca ambos aca adentro
     * para que queden managed dentro de la misma transaccion.
     */
    public boolean existeInscripcion(String nickname, String nombrePrograma) {
        EntityManager em = conexion.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(i) FROM InscripcionPrograma i WHERE i.estudiante.nickname = :nick AND i.pFormacion.nombre = :programa", Long.class)
                    .setParameter("nick", nickname)
                    .setParameter("programa", nombrePrograma)
                    .getSingleResult();
            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    /** Persiste la inscripcion del estudiante al programa en la fecha indicada. */
    public void inscribir(String nickname, String mail, String nombrePrograma, LocalDate fechaInscripcion) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Estudiante estudiante = em.createQuery(
                            "SELECT e FROM Estudiante e WHERE e.nickname = :nick AND e.Mail = :mail", Estudiante.class)
                    .setParameter("nick", nickname)
                    .setParameter("mail", mail)
                    .getSingleResult();
            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombrePrograma);

            InscripcionPrograma inscripcion = new InscripcionPrograma();
            inscripcion.setEstudiante(estudiante);
            inscripcion.setpFormacion(programa);
            inscripcion.setFechaInscripcion(fechaInscripcion);
            em.persist(inscripcion);

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

    public void agregarCurso(String nombrePrograma, String nombreCurso) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombrePrograma);
            Curso curso = em.find(Curso.class, nombreCurso);

            programa.getCursos().add(curso);
            em.merge(programa);

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
     * Devuelve los nombres de los cursos que ya integran el programa.
     * Se consulta con JPQL (y no con programa.getCursos()) para no
     * depender de una coleccion lazy sobre una entidad ya desconectada.
     */
    public List<String> nombresCursosDelPrograma(String nombrePrograma) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c.nombre FROM ProgramaFormacion p JOIN p.cursos c WHERE p.nombre = :nombre ORDER BY c.nombre", String.class)
                    .setParameter("nombre", nombrePrograma)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
