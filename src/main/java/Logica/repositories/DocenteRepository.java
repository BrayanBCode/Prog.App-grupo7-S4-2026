package Logica.repositories;

import Logica.entities.usuarios.Docente;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class DocenteRepository {

    private final Conexion conexion;

    public DocenteRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /**
     * Busca un Docente por su nickname. Docente no tiene una @Id simple
     * (su clave es compuesta: nickname + mail), así que no alcanza con
     * em.find() como en Instituto o Curso — hace falta una consulta.
     *
     * OJO con las mayúsculas: JPQL usa el nombre del ATRIBUTO de la clase
     * Java, no el de la columna de la base. "nickname" en minúscula está
     * bien porque así se llama el campo en Usuario/Docente.
     */
    public Docente buscarPorNickname(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            TypedQuery<Docente> query = em.createQuery(
                    "SELECT d FROM Docente d WHERE d.nickname = :nickname", Docente.class);
            query.setParameter("nickname", nickname);

            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null; // no existe ningún docente con ese nickname
            }
        } finally {
            em.close();
        }
    }
}