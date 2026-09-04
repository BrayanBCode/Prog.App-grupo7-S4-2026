/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java t
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
    void altaInstituto(String nombre) throws Exception;
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
    List<String[]> listarDocentesPorInstituto(String nombreInstituto);
    List<String> listarNombresInstitutos();
    List<String[]> listarCursosTabla(String nombreInstituto);
    void altaCurso(String nombre, String descripcion, int duracion, float cantHoras, int cantCreditos, String url, LocalDate fechaRegistro, String nombreInstituto, String nicknameDocente, List<String> nombresPrevias) throws Exception;
    void altaEdicionCurso(String nombreEdicion, String nombreCurso, LocalDate fechaInicio, LocalDate fechaFin, int cupo, List<String> nicknamesDocentes) throws Exception;
    List<String> obtenerDataPrograma(String nombrePrograma)throws Exception;
    void modificarPorgrama(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) throws Exception;
    List<String[]> listarEstudiantesTabla();
    String obtenerEdicionVigente(String nombreCurso);
    void inscribirEstudianteEdicion(String nickname, String mail, String nombreEdicion, LocalDate fechaInscripcion) throws Exception;
    List<String> listarCursosPorInstituto(String nombreInstituto);
    List<String> listarEdicionesCurso(String nombreCurso);
    String[] obtenerEdicionCurso(String nombreCurso)throws Exception;
    String[] obtenerDataCurso(String nombreCurso) throws Exception;
    List<String> listarProgramasPorCurso(String nombreCurso);
    
}