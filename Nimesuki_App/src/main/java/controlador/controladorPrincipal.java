/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.HibernateUtil;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import modelo.dao.AnimeDAO;
import modelo.dao.FavoritosDAO;
import modelo.dao.UsuarioDAO;
import modelo.vo.Favoritos;
import modelo.vo.Usuario;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import vista.GestorUsuario;
import vista.GestorYConsultas;
import vista.Login;

/**
 *
 * @author eloy.castro
 */
public class controladorPrincipal {

    public static Session session;
    //declara los objetos DAO
    public static AnimeDAO aniDAO;
    public static FavoritosDAO favDAO;
    public static UsuarioDAO usuDAO;
    public static GestorUsuario gestorUsuario = new GestorUsuario();
    public static GestorYConsultas consultas = new GestorYConsultas();
    public static Login login = new Login();
    static DefaultComboBoxModel modeloComboUsuariosFavoritos = new DefaultComboBoxModel();
    static DefaultComboBoxModel modeloComboAnimesFavoritos = new DefaultComboBoxModel();
    static SpinnerNumberModel modeloSppinerValoracion = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSppinerCapActual = new SpinnerNumberModel();
    public static String nombreUsuarioLogeado = "";

    public static void iniciar() {
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        consultas.getComboAnimesFavoritos().setModel(modeloComboAnimesFavoritos);
        consultas.getComboAnimesFavoritos().setPreferredSize(new Dimension(55, consultas.getComboAnimesFavoritos().getPreferredSize().height));
        consultas.getComboFavoritosUsuarios().setModel(modeloComboUsuariosFavoritos);
        consultas.getComboTipoUsuario().addItem("USER");
        consultas.getComboTipoUsuario().addItem("ADMIN");
        consultas.getSpValoracion().setModel(modeloSppinerValoracion);
        consultas.getSpCapActual().setModel(modeloSppinerCapActual);
        modeloSppinerValoracion.setMinimum(0);
        modeloSppinerValoracion.setMaximum(5);
        modeloSppinerCapActual.setMinimum(0);
    }

    public static void iniciarSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        //crear los objetos DAO    
        aniDAO = HibernateUtil.getAnimeDAO();
        favDAO = HibernateUtil.getFavoritosDAO();
        usuDAO = HibernateUtil.getUsuarioDAO();
    }

    public static void cerrarSession() {
        session.close();
    }

    public static void iniciarLogin() {
        reiniciarLogin();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }

    private static void reiniciarLogin() {
        login.getTxtNombreLogin().setText("");
        login.getTxtPasswordLogin().setText("");
        login.getLblLogin().setText("");
        login.getCbVerPasswordLogin().setSelected(false);
        login.getTxtPasswordLogin().setEchoChar('*');
    }

    public static void iniciarGestorUsuarios() {
        reiniciarGestorUsuarios();
        gestorUsuario.setVisible(true);
        gestorUsuario.setLocationRelativeTo(null);
    }

    private static void reiniciarGestorUsuarios() {
        gestorUsuario.getTxtNombreUsuario().setText("");
        gestorUsuario.getTxtPasswordUsuario().setText("");
        gestorUsuario.getCbVerPassword().setSelected(false);
        gestorUsuario.getTxtPasswordUsuario().setEchoChar('*');
    }

    public static void iniciarConsultas() {
        reiniciarConsultas();
        consultas.setVisible(true);
        consultas.setLocationRelativeTo(null);
        cargarComboUsuarios();
    }

    private static void reiniciarConsultas() {
        consultas.getTxtIdUsuario().setText("");
        consultas.getTxtNombreUsuario().setText("");
        consultas.getTxtPasswordFavoritos().setText("");
        consultas.getCbVerPasswordFavoritos().setSelected(false);
        consultas.getTxtPasswordFavoritos().setEchoChar('*');
        consultas.getTxtIdAnime().setText("");
        consultas.getTxtNombreAnime().setText("");
        consultas.getTxtCategorias().setText("");
        consultas.getTxtAnhoSalida().setText("");
        consultas.getTxtDescripcion().setText("");
        consultas.getTxtImagen().setText("");
        consultas.getTxtCapTotales().setText("");
        consultas.getSpValoracion().setValue(0);
        consultas.getSpCapActual().setValue(0);
    }

    public static Usuario loginUsuario() {
        Usuario u = null;
        login.getLblLogin().setText("");

        if (login.getTxtNombreLogin().getText().trim().isEmpty() || login.getTxtPasswordLogin().getPassword().length == 0) {
            JOptionPane.showMessageDialog(null, "Faltan datos");
        } else {
            try {
                String nombre = login.getTxtNombreLogin().getText();
                char[] pw = login.getTxtPasswordLogin().getPassword();
                String contrasenha = new String(pw);
                u = usuDAO.buscarUsuarioPorNombre(session, nombre);
                if (u != null) {
                    if (BCrypt.checkpw(contrasenha, u.getContrasenha())) {
                        nombreUsuarioLogeado = nombre;
                    } else {
                        login.getLblLogin().setText("Login incorrecto");
                        u = null;
                    }
                } else {
                    login.getLblLogin().setText("Login incorrecto");
                }
            } catch (Exception e) {
                Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
            }
        }
        return u;
    }

    public static void cerrarSesionUsuario() {
        nombreUsuarioLogeado = "";
    }

    public static void modificarNombreUsuario() {
        if (gestorUsuario.getTxtNombreUsuario().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos");
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nuevoNombre = gestorUsuario.getTxtNombreUsuario().getText().trim();
            Usuario uLogueado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
            if (uLogueado != null) {
                usuDAO.modificarNombreUsuario(session, uLogueado, nuevoNombre);
                JOptionPane.showMessageDialog(null, "Nombre de usuario modificado con éxito.");
                nombreUsuarioLogeado = nuevoNombre;
                gestorUsuario.getTxtNombreUsuario().setText("");
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void modificarContrasenhaUsuario() {
        if (String.valueOf(gestorUsuario.getTxtPasswordUsuario().getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos");
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            char[] pw = gestorUsuario.getTxtPasswordUsuario().getPassword();
            String nuevaContrasenha = new String(pw);
            String contrasenhaEncriptada = BCrypt.hashpw(nuevaContrasenha.trim(), BCrypt.gensalt());
            Usuario uLogueado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
            if (uLogueado != null) {
                usuDAO.modificarContrasenhaUsuario(session, uLogueado, contrasenhaEncriptada);
                JOptionPane.showMessageDialog(null, "Cotraseña modificada con éxito.");
                gestorUsuario.getTxtPasswordUsuario().setText("");
                gestorUsuario.getCbVerPassword().setSelected(false);
                gestorUsuario.getTxtPasswordUsuario().setEchoChar('*');
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void darBajaUsuario() {
        try {
            HibernateUtil.beginTx(session);
            Usuario uCreado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
            if (uCreado != null) {
                int opcion = JOptionPane.showOptionDialog(
                        null,
                        "¿Quieres borrar la cuenta de usuario?",
                        "Borrar usuario",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Sí", "Cancelar"},
                        "Cancelar"
                );

                if (opcion == 0) {
                    usuDAO.borrarUsuario(session, uCreado);
                    JOptionPane.showMessageDialog(null, "Usuario borrado con éxito.");
                    vaciarTxtGestor();
                    cerrarSesionUsuario();
                    controladorPrincipal.iniciarLogin();
                    gestorUsuario.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Datos incorrectos.");
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico");
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void vaciarTxtGestor() {
        gestorUsuario.getTxtNombreUsuario().setText("");
        gestorUsuario.getTxtPasswordUsuario().setText("");
        gestorUsuario.getCbVerPassword().setSelected(false);
    }

    public static void cargarComboAnimes() {
        try {
            Usuario u = (Usuario) consultas.getComboFavoritosUsuarios().getSelectedItem();
            favDAO.cargarComboPorUsuario(session, modeloComboAnimesFavoritos, u);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
        }
    }

    public static void cargarComboUsuarios() {
        try {
            usuDAO.cargarCombo(session, modeloComboUsuariosFavoritos);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static boolean buscarEloy() {
        boolean encontrado = false;
        try {
            Usuario u;
            u = usuDAO.buscarUsuarioPorNombre(session, "Eloy");
            if (u != null) {
                encontrado = true;
            } else {
                encontrado = false;
            }
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
        }
        return encontrado;
    }

    public static void iniciarDatos() {
        try {
            HibernateUtil.beginTx(session);
            String contrasenha = "123";
            String contrasenhaEncriptada = BCrypt.hashpw(contrasenha.trim(), BCrypt.gensalt());
            usuDAO.guardarEloy(session, "Eloy", contrasenhaEncriptada, "USER");
            String contrasenha2 = "456";
            String contrasenhaEncriptada2 = BCrypt.hashpw(contrasenha2.trim(), BCrypt.gensalt());
            usuDAO.guardarEloy(session, "EloyAdmin", contrasenhaEncriptada2, "ADMIN");
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
        }
    }

    public static void fijarCapitulos() {
        try {
            Favoritos f = (Favoritos) consultas.getComboAnimesFavoritos().getSelectedItem();
            if (f != null) {
                modeloSppinerCapActual.setMaximum(f.getAnime().getCapTotales());
                modeloSppinerCapActual.setValue(f.getCapActual());
            }
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
        }
    }

    public static void fijarValoracion() {
        try {
            Favoritos f = (Favoritos) consultas.getComboAnimesFavoritos().getSelectedItem();
            if (f != null) {
                modeloSppinerValoracion.setValue(f.getValoracion());
            }
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
        }
    }

}
