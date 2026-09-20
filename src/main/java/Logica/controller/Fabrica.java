/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.controller;
/*
 *
 * @author maida
 */

public class Fabrica {
    private static Fabrica INSTANCE;
    
    private Fabrica() {};
            
    public static Fabrica getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Fabrica();
        }
        return INSTANCE;
    }
    
    public IController getUserControler(ControllerVersion version) {
        if(version == ControllerVersion.v2) {
            return new ControllerV2();
        }

        return new ControllerV1();
    }
    
}
