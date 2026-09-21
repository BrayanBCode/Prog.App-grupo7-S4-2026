package Logica.repositories;

import Logica.entities.cursos.Curso;
import Logica.entities.programaFormacion.ProgramaFormacion;
import Persistencia.Conexion;

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
