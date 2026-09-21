package Logica.repositories;

import Logica.entities.usuarios.Docente;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class DocenteRepository {

    private final Conexion conexion;

    public DocenteRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /**
     * Busca un Docente por su nickname. Docente no tiene una @Id simple
     * (su clave es compuesta: nickname + mail), asi que no alcanza con
     * em.find() como en Instituto o Curso — hace falta una consulta.
     *
     * OJO con las mayusculas: JPQL usa el nombre del ATRIBUTO de la clase
     * Java, no el de la columna de la base. "nickname" en minuscula esta
     * bien porque asi se llama el campo en Usuario/Docente.
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
                return null; // no existe ningun docente con ese nickname
            }
        } finally {
            em.close();
        }
    }

    public boolean existe(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(d) FROM Docente d WHERE d.nickname = :nick", Long.class)
                    .setParameter("nick", nickname)
                    .getSingleResult();
            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public List<Docente> listarTodos() {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Docente d", Docente.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Solo los docentes que integran el instituto elegido: es la base de
     * la regla "solo se pueden registrar cursos asociados al Instituto
     * que integran" (que ademas se revalida en CursoService).
     */
    public List<Docente> listarPorInstituto(String nombreInstituto) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT d FROM Docente d JOIN d.institutos i WHERE i.nombre = :inst", Docente.class)
                    .setParameter("inst", nombreInstituto)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombresInstitutos(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT i.nombre FROM Docente d JOIN d.institutos i WHERE d.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombresCursos(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c.nombre FROM Curso c WHERE c.docente.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombresEdiciones(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT e.nombre FROM EdicionCurso e JOIN e.docentes d WHERE d.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> nombresProgramas(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT p.nombre FROM ProgramaFormacion p JOIN p.cursos c WHERE c.docente.nickname = :nick", String.class)
                    .setParameter("nick", nickname)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
