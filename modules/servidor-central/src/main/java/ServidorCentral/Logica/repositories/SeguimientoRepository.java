package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.usuarios.Seguimiento;
import ServidorCentral.Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.util.List;

/** Acceso a datos de Seguimiento (quien sigue a quien). Las reglas viven en SeguimientoService. */
public class SeguimientoRepository {

    private final Conexion conexion;

    public SeguimientoRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    public boolean existe(String seguidor, String seguido) {
        EntityManager em = conexion.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(s) FROM Seguimiento s WHERE s.seguidor = :a AND s.seguido = :b", Long.class)
                    .setParameter("a", seguidor)
                    .setParameter("b", seguido)
                    .getSingleResult();
            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public void guardar(String seguidor, String seguido) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Seguimiento(seguidor, seguido));
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

    /** Nicknames de los usuarios que {@code nickname} sigue. */
    public List<String> seguidosDe(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s.seguido FROM Seguimiento s WHERE s.seguidor = :n ORDER BY s.seguido", String.class)
                    .setParameter("n", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Nicknames de los usuarios que siguen a {@code nickname}. */
    public List<String> seguidoresDe(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s.seguidor FROM Seguimiento s WHERE s.seguido = :n ORDER BY s.seguidor", String.class)
                    .setParameter("n", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
