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
    
    void altaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto) throws Exception;
    void modificarUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac) throws Exception;
    void crearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta)throws Exception;
    List<String[]> listarUsuariosTabla();  
    String[] obtenerDataUsuario(String nickname,String mail);
    List<String> obtenerEdicionesYProgramas(String nickname);
    boolean esDocente(String nickname);
    List<String> obtenerDataDocente(String nickname);
    List<String> listarNombreProgramas();
    List<String> listarNombresCursos();
    void agregarCursoPrograma(String nombreP, String nombreC) throws Exception;
    List<String[]> listarDocentesTabla();
    List<String> listarNombresInstitutos();
    List<String[]> listarCursosTabla(String nombreInstituto);
    void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception;
    List<String> obtenerDataPrograma(String nombrePrograma)throws Exception;
    void modificarPorgrama(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) throws Exception;
    
}