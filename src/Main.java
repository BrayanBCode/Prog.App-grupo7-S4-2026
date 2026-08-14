
import Logica.Curso;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.Estudiante;
import Logica.Facultad;
import Logica.InscripcionEdicion;
import Logica.InscripcionPrograma;
import Logica.Instituto;
import Logica.ProgramaFormacion;
import Presentacion.JFInicio;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author briha
 */
public class Main {
    public static void main() {
        new JFInicio().setVisible(true);
        // 1. Iniciar el EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TuPU"); // Nombre de tu Persistence Unit
        var em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // -------------------------------------------------------------
            // A. ENTIDADES INDEPENDIENTES
            // -------------------------------------------------------------
            // 1. Facultad e Instituto (1 a N)
            Facultad facultad = new Facultad();
            facultad.setNombre("FING");
            em.persist(facultad);

            Instituto inst = new Instituto();
            inst.setNombre("INCO");
            inst.setFacultad(facultad); // Asignar relación
            em.persist(inst);

            // 2. Docente y Estudiante (Heredan de Usuario)
            Docente docente = new Docente();
            // ... set atributos de Usuario/Docente ...
            em.persist(docente);

            Estudiante estudiante = new Estudiante();
            // ... set atributos de Usuario/Estudiante ...
            em.persist(estudiante);

            // -------------------------------------------------------------
            // B. CURSO Y AUTORRELACIÓN ("Es previa de")
            // -------------------------------------------------------------
            Curso curso1 = new Curso();
            curso1.setNombreC("Programacion 1");
            curso1.setInstituto(inst);
            curso1.setDocente(docente);
            em.persist(curso1);

            Curso curso2 = new Curso();
            curso2.setNombreC("Programacion 2");
            curso2.setInstituto(inst);
            curso2.setDocente(docente);
            
            // Probar autorrelación (P1 es previa de P2)
            curso2.getPrevias().add(curso1);
            em.persist(curso2);

            // -------------------------------------------------------------
            // C. EDICIÓN DE CURSO Y MUCHOS A MUCHOS (Docente - Edicion)
            // -------------------------------------------------------------
            EdicionCurso edicion = new EdicionCurso();
            edicion.setCurso(curso2);
            edicion.getDocentes().add(docente); // Relación ManyToMany
            em.persist(edicion);

            // -------------------------------------------------------------
            // D. PROGRAMA DE FORMACIÓN Y MUCHOS A MUCHOS (Programa - Curso)
            // -------------------------------------------------------------
            ProgramaFormacion programa = new ProgramaFormacion();
            programa.setNombre("Tecnicatura en BD");
            programa.getCursos().add(curso1);
            programa.getCursos().add(curso2);
            em.persist(programa);

            // -------------------------------------------------------------
            // E. CLASES DE ASOCIACIÓN (Inscripciones)
            // -------------------------------------------------------------
            InscripcionEdicion inscEdicion = new InscripcionEdicion();
            inscEdicion.setEstudiante(estudiante);
            inscEdicion.setEdicionCurso(edicion);
            em.persist(inscEdicion);

            InscripcionPrograma inscPrograma = new InscripcionPrograma();
            inscPrograma.setEstudiante(estudiante);
            inscPrograma.setpFormacion(programa);
            em.persist(inscPrograma);

            // Confirmar inserciones en la BD
            em.getTransaction().commit();
            System.out.println("✅ ¡Persistencia inicial ejecutada con éxito!");

            // -------------------------------------------------------------
            // 2. VERIFICACIÓN (Limpiar la caché de JPA para forzar consulta a BD)
            // -------------------------------------------------------------
            em.clear(); 

            System.out.println("\n--- PROBANDO RECONSULTAS DESDE BD ---");
            
            // Probar navegación en relaciones:
            Estudiante eBD = em.find(Estudiante.class, estudiante.getId());
            System.out.println("Estudiante recuperado: " + eBD.getNombre());
            System.out.println("Inscripciones a ediciones: " + eBD.getInscripciones().size());

            Curso c2BD = em.find(Curso.class, "Programacion 2");
            System.out.println("Previas de Prog 2: " + c2BD.getPrevias().get(0).getNombreC());

            ProgramaFormacion pBD = em.find(ProgramaFormacion.class, "Tecnicatura en BD");
            System.out.println("Cursos en el programa: " + pBD.getCursos().size());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
