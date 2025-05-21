/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import controlador.controladorPrincipal;
import javax.swing.JOptionPane;

/**
 *
 * @author eloy.castro
 */
public class Nimesuki {

    public static void main(String[] args) {
        try {
            FlatLaf.registerCustomDefaultsSource("themes");
            FlatLightLaf.setup();
            controladorPrincipal.iniciar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
