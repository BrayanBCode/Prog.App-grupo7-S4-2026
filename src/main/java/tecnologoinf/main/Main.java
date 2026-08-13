/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tecnologoinf.main;

import tecnologoinf.Logica.Fabrica;
import tecnologoinf.Logica.IController;
import tecnologoinf.Presentacion.JFInicio;
/**
 *
 * @author briha
 */
public class Main {

    public static void main(String[] args) {
        var f = Fabrica.getInstance();
        IController c = f.getUserControler();
        c.hola();
        var prueba = new JFInicio();
        prueba.setVisible(true);        
        
    }
}
