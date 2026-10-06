/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServidorCentral.Logica.controller;
/*
 *
 * @author maida
 */

import ServidorCentral.Logica.datatypes.ControllerVersion;

public class Fabrica {
    private static Fabrica INSTANCE;
    
    private Fabrica() {};
            
    public static Fabrica getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Fabrica();
        }
        return INSTANCE;
    }
    
    /**
     * Controller "viejo" (ControllerV1, devuelve String[]).
     * ControllerV2 ya no implementa IController porque devuelve DataTypes:
     * para usarlo pedirlo con {@link #getControllerV2()}.
     */
    public IControllerV2 getUserControler(ControllerVersion version) {
        if(version == ControllerVersion.v2) {
            throw new UnsupportedOperationException(
                    "ControllerV2 devuelve DataTypes y ya no implementa IController. Use Fabrica.getInstance().getControllerV2().");
        }

        return new ControllerV2();
    }

    /**
     * Controller en capas, que devuelve DataTypes.
     */
    public IControllerV2 getControllerV2() {
        return new ControllerV2();
    }
    
}
