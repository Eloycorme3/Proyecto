/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static org.apache.logging.log4j.util.LoaderUtil.getClassLoader;

/**
 *
 * @author eloy.castro
 */
public class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";

    public static void guardarDatos(String ip, String usuario, String pass) throws IOException {
        Properties props = new Properties();
        props.setProperty("ip", ip);
        props.setProperty("user", usuario);
        props.setProperty("password", pass);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Datos de conexión");
        }
    }

    public static String[] cargarDatos() throws IOException {
        Properties props = new Properties();

        try (InputStream input = getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                guardarDatos("127.0.0.1", "root", "root");
            }
            props.load(input);
        }

        String ip = props.getProperty("ip", "");
        String usuario = props.getProperty("user", "");
        String pass = props.getProperty("password", "");
        return new String[]{ip, usuario, pass};
    }
}