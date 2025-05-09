/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import controlador.controladorPrincipal;

/**
 *
 * @author eloy.castro
 */
public class Nimesuki {

    public static void main(String[] args) {
        try {
            FlatLaf.registerCustomDefaultsSource("themes");

            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        controladorPrincipal.iniciar();
    }
}
