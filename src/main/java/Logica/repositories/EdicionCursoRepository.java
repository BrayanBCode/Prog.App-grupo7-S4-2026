package Logica.repositories;

import Logica.entities.cursos.Curso;
import Logica.entities.cursos.EdicionCurso;
import Logica.entities.cursos.InscripcionEdicion;
import Logica.entities.usuarios.Docente;
import Logica.entities.usuarios.Estudiante;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EdicionCursoRepository {
    private final Conexion conexion;

    public EdicionCursoRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    public List<String> obtenerNombres(String nombreCurso) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT e.nombre FROM EdicionCurso e WHERE e.curso.nombre = :nombreCurso", String.class)
                    .setParameter("nombreCurso", nombreCurso)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public EdicionCurso obtenerPorNombres(String nombreEdicion) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(EdicionCurso.class, nombreEdicion);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve el nombre de la edicion vigente (la que esta en curso hoy)
     * de un curso, o null si no hay ninguna.
     */
    public String obtenerNombreEdicionVigente(String nombreCurso, LocalDate hoy) {
        EntityManager em = conexion.getEntityManager();
        try {
            List<String> resultado = em.createQuery(
                            "SELECT e.nombre FROM EdicionCurso e WHERE e.curso.nombre = :curso AND e.fechaInicio <= :hoy AND e.fechaFin >= :hoy", String.class)
                    .setParameter("curso", nombreCurso)
                    .setParameter("hoy", hoy)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    public long contarInscriptos(String nombreEdicion) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(i) FROM InscripcionEdicion i WHERE i.edicionCurso.nombre = :edicion", Long.class)
                    .setParameter("edicion", nombreEdicion)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public boolean existeInscripcion(String nickname, String nombreEdicion) {
        EntityManager em = conexion.getEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(i) FROM InscripcionEdicion i WHERE i.estudiante.nickname = :nick AND i.edicionCurso.nombre = :edicion", Long.class)
                    .setParameter("nick", nickname)
                    .setParameter("edicion", nombreEdicion)
                    .getSingleResult();
            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Persiste la edicion y la vincula con sus docentes, TODO en la misma
     * transaccion y buscando el Curso y los Docentes aca adentro, para
     * que queden "vivos" (managed) al momento de guardar.
     *
     * La relacion ManyToMany EdicionCurso<->Docente la maneja Docente
     * (lado dueño), asi que la edicion se agrega ahi, no al reves.
     */
    public void guardar(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Curso curso = em.find(Curso.class, nombreCurso);

            EdicionCurso edicion = new EdicionCurso(nombreEdicion, curso, fechaInicio, fechaFin, cupo, LocalDate.now());
            em.persist(edicion);

            if (nicknamesDocentes != null) {
                for (String nickname : nicknamesDocentes) {
                    Docente docente = em.createQuery(
                                    "SELECT d FROM Docente d WHERE d.nickname = :nick", Docente.class)
                            .setParameter("nick", nickname)
                            .getSingleResult();
                    docente.getEdicionesC().add(edicion);
                    em.merge(docente);
                }
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
     * Registra la inscripcion de un estudiante a una edicion. Las
     * validaciones (cupo, inscripcion repetida) ya las hizo el Service.
     */
    public void inscribir(String nickname, String mail, String nombreEdicion, LocalDate fechaInscripcion) {
        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Estudiante estudiante = em.createQuery(
                            "SELECT e FROM Estudiante e WHERE e.nickname = :nick AND e.Mail = :mail", Estudiante.class)
                    .setParameter("nick", nickname)
                    .setParameter("mail", mail)
                    .getSingleResult();

            EdicionCurso edicion = em.find(EdicionCurso.class, nombreEdicion);

            InscripcionEdicion inscripcion = new InscripcionEdicion();
            inscripcion.setEstudiante(estudiante);
            inscripcion.setEdicionCurso(edicion);
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
}
