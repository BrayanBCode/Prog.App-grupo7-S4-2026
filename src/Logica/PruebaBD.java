package Logica; // O el paquete donde tengas tu main

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PruebaBD {

    public static void main(String[] args) {
        System.out.println("Iniciando conexión con JPA...");
        
        try {
            // "DataBase" debe ser idéntico al name de tu persistence-unit
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DataBase");
            EntityManager em = emf.createEntityManager();

            System.out.println("¡CONEXIÓN EXITOSA! Las tablas han sido creadas.");

            em.close();
            emf.close();
        } catch (Exception e) {
            System.err.println("❌ Ocurrió un error al conectar con la Base de Datos:");
            e.printStackTrace();
        }
    }
}
