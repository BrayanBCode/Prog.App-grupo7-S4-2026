package Logica.repositories;

import Logica.entities.cursos.Curso;
import Logica.entities.cursos.Instituto;
import Logica.entities.usuarios.Docente;
import Persistencia.Conexion;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class CursoRepository {

    private final Conexion conexion;

    public CursoRepository(Conexion conexion) {
        this.conexion = conexion;
    }

    /**
     * @return el Curso si existe, o null si no hay ninguno con ese nombre.
     * Se usa para las validaciones rápidas del Service (ej. "¿ya existe?").
     */
    public Curso buscarPorNombre(String nombre) {
        EntityManager em = conexion.getEntityManager();
        try {
            return em.find(Curso.class, nombre);
        } finally {
            em.close();
        }
    }

    /**
     * Arma y persiste un Curso completo, TODO dentro del mismo
     * EntityManager: busca el Instituto, el Docente y cada previa acá
     * adentro (no recibe esos objetos ya armados desde afuera), así
     * quedan todos "vivos" (managed) en el momento de guardar.
     *
     * El Service ya validó antes que el instituto y el docente existen
     * (para poder tirar un mensaje de error específico y amigable) — acá
     * los volvemos a buscar, sí, es una pequeña repetición, pero es el
     * precio de mantener la transacción consistente. Para un proyecto de
     * este tamaño es una repetición aceptable.
     */
    public void guardar(String nombre, String descripcion, int duracion, float cantHoras,
                        int cantCreditos, String url, String nombreInstituto,
                        String nicknameDocente, List<String> previas) {

        EntityManager em = conexion.getEntityManager();
        try {
            em.getTransaction().begin();

            Instituto instituto = em.find(Instituto.class, nombreInstituto);

            TypedQuery<Docente> queryDocente = em.createQuery(
                    "SELECT d FROM Docente d WHERE d.nickname = :nickname", Docente.class);
            queryDocente.setParameter("nickname", nicknameDocente);
            Docente docente = queryDocente.getSingleResult();

            Curso curso = new Curso();
            curso.setNombreC(nombre);
            curso.setDescripcion(descripcion);
            curso.setDuracion(duracion);
            curso.setCanthoras(cantHoras);
            curso.setCantCreditos(cantCreditos);
            curso.setUrl(url);
            curso.setFechaRegistro(LocalDate.now());
            curso.setInstituto(instituto);
            curso.setDocente(docente);

            if (previas != null) {
                for (String nombrePrevia : previas) {
                    Curso previa = em.find(Curso.class, nombrePrevia);
                    if (previa != null) {
                        curso.getPrevias().add(previa);
                    }
                }
            }

            em.persist(curso);
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