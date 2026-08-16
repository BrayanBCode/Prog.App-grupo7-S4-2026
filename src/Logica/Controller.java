/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.time.LocalDate;
import javax.persistence.EntityManager;
import Persistencia.Conexion;
/**
 *
 * @author maida
 */
public class Controller implements IController {


    @Override
    public void AltaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto) {
        
        // 1. Determinar y crear la entidad correspondiente
        Usuario usuario;
        
        if (instituto != null && !instituto.trim().isEmpty()) {
            usuario = new Docente(nickname, mail, nombre, apellido, fechaNac, instituto);
        } else {
            usuario = new Estudiante(nickname, mail, nombre, apellido, fechaNac);
        }

        // 2. Obtener el EntityManager
        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            // 3. Iniciar la transacción
            em.getTransaction().begin();
            
            // 4. Persistir el objeto (JPA detecta automáticamente si es Docente o Estudiante)
            em.persist(usuario);
            
            // 5. Confirmar los cambios en la Base de Datos
            em.getTransaction().commit();
            
        } catch (Exception e) {
            // Si ocurre un error, deshacer la operación
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // 6. Cerrar siempre el EntityManager
            em.close();
        }
    }
}


