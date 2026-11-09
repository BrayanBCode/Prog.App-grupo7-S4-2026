package ServidorCentral.Logica.repositories;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;
import ServidorCentral.Logica.entities.cursos.ValoracionCurso;
import ServidorCentral.Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/** Acceso a datos de las valoraciones de cursos. Las reglas viven en ValoracionCursoService. */
public class ValoracionCursoRepository {

    private final Conexion conexion;

    public ValoracionCursoRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /** Puntaje que el estudiante le dio al curso, o null si todavia no lo valoro. */
    public Integer puntajeDe(String nickname, String curso) {
        EntityManager em = conexion.getEntityManager();
        try {
            List<Integer> r = em.createQuery(
                            "SELECT v.puntaje FROM ValoracionCurso v WHERE v.estudiante = :n AND v.curso = :c", Integer.class)
                    .setParameter("n", nickname)
                    .setParameter("c", curso)
                    .getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally {
            em.close();
        }
    }

    /** Todos los puntajes del curso (uno por estudiante que lo valoro). */
    public List<Integer> puntajesDe(String curso) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery("SELECT v.puntaje FROM ValoracionCurso v WHERE v.curso = :c", Integer.class)
                    .setParameter("c", curso)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Si el estudiante fue aceptado en alguna edicion del curso (es decir, si participa del curso). */
    public boolean participa(String nickname, String curso) {
        EntityManager em = conexion.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(i) FROM InscripcionEdicion i WHERE i.estudiante.nickname = :n"
                                    + " AND i.edicionCurso.curso.nombre = :c AND i.estado = :aceptada", Long.class)
                    .setParameter("n", nickname)
                    .setParameter("c", curso)
                    .setParameter("aceptada", EstadoInscripcion.ACEPTADA)
                    .getSingleResult();
            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    /** Nombres de los cursos en los que el estudiante participa (aceptado en alguna edicion), en orden alfabetico. */
    public List<String> cursosDondeParticipa(String nickname) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT i.edicionCurso.curso.nombre FROM InscripcionEdicion i"
                                    + " WHERE i.estudiante.nickname = :n AND i.estado = :aceptada"
                                    + " ORDER BY i.edicionCurso.curso.nombre", String.class)
                    .setParameter("n", nickname)
                    .setParameter("aceptada", EstadoInscripcion.ACEPTADA)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Guarda la valoracion. Si el estudiante ya habia valorado el curso, la base la rechaza (restriccion unica). */
    public void guardar(String nickname, String curso, int puntaje, LocalDate fecha) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new ValoracionCurso(nickname, curso, puntaje, fecha));
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
