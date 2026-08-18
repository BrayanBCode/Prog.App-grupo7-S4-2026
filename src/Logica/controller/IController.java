/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Logica.controller;

import java.time.LocalDate;

/**
 *
 * @author maida
 */
public interface IController {
    
    void AltaUsuario(String nickname, String mail, String nombre, String apellido, LocalDate fechaNac, String instituto);
    void CrearPrograma(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta);
}
