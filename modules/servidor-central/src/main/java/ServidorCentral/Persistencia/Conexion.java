package ServidorCentral.Persistencia;
/**
 *
 * @author maida
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

public class Conexion {
    private static Conexion instancia;
    private EntityManagerFactory emf;

    private Conexion() {
        //Aqui se crea la EntityManager bajo el nombre de nuestra DB
        emf = Persistence.createEntityManagerFactory("DataBase");
    }

    private Conexion(Map<String, ?> propiedades) {
        emf = Persistence.createEntityManagerFactory("DataBase", propiedades);
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /**
     * Conexion aparte (no la unica de la aplicacion) con otras propiedades que las de persistence.xml.
     * La usan los tests de los repositories para trabajar contra una base en memoria en vez de MySQL.
     */
    public static Conexion conPropiedades(Map<String, ?> propiedades) {
        return new Conexion(propiedades);
    }

    public EntityManager getEntityManager() {return emf.createEntityManager();}

    /** Cierra la conexion (los tests la cierran al terminar cada uno). */
    public void cerrar() {
        emf.close();
    }
}
