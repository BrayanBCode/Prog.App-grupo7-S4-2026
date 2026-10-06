package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.Categoria;
import ServidorCentral.Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.util.List;

/** Acceso a datos de Categoria: solo lee y escribe, las reglas viven en CategoriaService. */
public class CategoriaRepository {

    private final Conexion conexion;

    public CategoriaRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /** @return la Categoria con ese nombre (es su @Id), o null si no existe. */
    public Categoria buscarPorNombre(String nombre) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(Categoria.class, nombre);
        } finally {
            em.close();
        }
    }

    public List<String> obtenerNombres() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery("SELECT c.nombre FROM Categoria c ORDER BY c.nombre", String.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void guardar(String nombre) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Categoria(nombre));
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
