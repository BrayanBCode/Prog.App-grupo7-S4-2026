package persistencia;
/**
 *
 * @author maida
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Conexion {
    private static Conexion instancia;
    private EntityManagerFactory emf;

    private Conexion() {
        //Aqui se crea la EntityManager bajo el nombre de nuestra DB
        emf = Persistence.createEntityManagerFactory("DataBase"); 
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}