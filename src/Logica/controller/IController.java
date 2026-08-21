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
    void ConsultaUsuario();            // Not Implemented
    void ModificarUsuario();           // Not Implemented
    void AltaCurso();                  // Not Implemented
    void ConsultaCurso();              // Not Implemented
    void AltaEdiciónCurso();           // Not Implemented
    void ConsolutaEdicionCurso();      // Not Implemented
    void InscripcionEdicionCurso();    // Not Implemented
    void CrearProgramaFormacion();     // Not Implemented 
    void AgregarCursoAProgFormacion(); // Not Implemented 
    void ConsoltaProgramaFormacion();  // Not Implemented 
    void AltaInstituto();              // Not Implemented
       
    void CrearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta);
    List<String[]> listarUsuariosTabla();  
    String[] obtenerDataUsuario(String nickname,String mail);
    List<String> obtenerEdicionesYProgramas(String nickname);
    boolean esDocente(String nickname);
    List<String> obtenerDataDocente(String nickname);
    List<String> listarNombreProgramas();
    List<String> listarNombreCursos();
    void agregarCursoPrograma(String nombreP,String nombreC);

}
