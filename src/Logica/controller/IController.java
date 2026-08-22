/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Logica.controller;

import Logica.usuarios.Estudiante;
import Logica.usuarios.Usuario;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author maida
 */
public interface IController {
    
    void AltaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto) throws Exception;
    void ModificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception;
    void CrearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta)throws Exception;
    List<String[]> listarUsuariosTabla();  
    String[] obtenerDataUsuario(String nickname,String mail);
    List<String> obtenerEdicionesYProgramas(String nickname);
    boolean esDocente(String nickname);
    List<String> obtenerDataDocente(String nickname);
    List<String> listarNombreProgramas();
    List<String> listarNombresCursos();
    void agregarCursoPrograma(String nombreP, String nombreC) throws Exception;

}
