/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tecnologoinf.Logica;

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
    
    public IController getUserControler() {
        return new Controller();
    }
    
}
