package Logica.repositories;

import Logica.entities.usuarios.Estudiante;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class EstudianteRepository {

    private final Conexion conexion;

    public EstudianteRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /**
     * La clave de Estudiante es compuesta (nickname + Mail), por eso se
     * busca con una consulta y no con em.find().
     */
    public Estudiante buscarPorNicknameYMail(String nickname, String mail) {
        EntityManager em = conexion.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                    "SELECT e FROM Estudiante e WHERE e.nickname = :nick AND e.Mail = :mail", Estudiante.class);
            query.setParameter("nick", nickname);
            query.setParameter("mail", mail);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public List<Estudiante> listarTodos() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Estudiante e", Estudiante.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombresEdicionesInscriptas(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT ie.edicionCurso.nombre FROM InscripcionEdicion ie WHERE ie.estudiante.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombresProgramasInscriptos(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT ip.pFormacion.nombre FROM InscripcionPrograma ip WHERE ip.estudiante.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
