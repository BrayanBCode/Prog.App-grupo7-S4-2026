/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.controller;

import Logica.*;
import Logica.usuarios.Docente;
import Logica.usuarios.Estudiante;
import Logica.cursos.ProgramaFormacion;
import Logica.usuarios.Usuario;
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
        
        // Determinar y crear la entidad correspondiente
        Usuario usuario;
        
        if (instituto != null && !instituto.trim().isEmpty()) {
            usuario = new Docente(nickname, mail, nombre, apellido, fechaNac, instituto);
        } else {
            usuario = new Estudiante(nickname, mail, nombre, apellido, fechaNac);
        }

        // Obtener el EntityManager
        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            // Iniciar la transacción
            em.getTransaction().begin();
            
            // Persistir el objeto (JPA detecta automáticamente si es Docente o Estudiante)
            em.persist(usuario);
            
            // Confirmar los cambios en la Base de Datos
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
        
    @Override 
    public void CrearPrograma( String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) {
        
        EntityManager em = Conexion.getInstancia().getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            
            ProgramaFormacion pf = em.find(ProgramaFormacion.class, nombre);
            if (pf != null) {
                throw new Exception("Ya existe un programa de formación registrado con el nombre: " + nombre);
            }
            
            
            ProgramaFormacion nuevoPrograma = new ProgramaFormacion( nombre, descripcion, fechaInicio, fechaFin, fechaAlta);
            
            
            em.persist(nuevoPrograma);
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            
            try {
                throw e;
            } catch (Exception ex) {
                System.getLogger(Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        } finally {
            em.close();
        }
    }   
        
        
    

    }

    

   
    

