package Logica.repositories;

import Logica.entities.cursos.Instituto;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Repository de Instituto: la ÚNICA clase que sabe hablar con la base de
 * datos para esta entidad. No tiene ninguna regla de negocio — eso es
 * trabajo del Service. Acá solo entra y sale información.
 */
public class InstitutoRepository {

    private final Conexion conexion;

    public InstitutoRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /**
     * Busca un Instituto por su nombre (es su @Id, así que alcanza con
     * em.find — no hace falta escribir ninguna consulta).
     *
     * @return el Instituto si existe, o null si no.
     */
    public Instituto buscarPorNombre(String nombre) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(Instituto.class, nombre);
        } finally {
            em.close();
        }
    }

    public List<String> obtenerNombres() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT i.nombre FROM Instituto i ORDER BY i.nombre", String.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
