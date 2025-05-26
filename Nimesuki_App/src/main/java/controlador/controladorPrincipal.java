/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.HibernateUtil;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import modelo.dao.AnimeDAO;
import modelo.dao.FavoritosDAO;
import modelo.dao.UsuarioDAO;
import modelo.vo.Anime;
import modelo.vo.Favoritos;
import modelo.vo.FavoritosPK;
import modelo.vo.Usuario;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import vista.GestorAdmin;
import vista.GestorNombreAnime;
import vista.GestorUsuario;
import vista.GestorYConsultas;
import vista.Login;
import vista.ReportAdmin;

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
    public static ReportAdmin reportAdmin = new ReportAdmin();
    static DefaultComboBoxModel modeloComboUsuariosFavoritos = new DefaultComboBoxModel();
    static DefaultComboBoxModel modeloComboAnimesFavoritos = new DefaultComboBoxModel();
    static SpinnerNumberModel modeloSpinnerValoracion = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerCapActual = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerCapTotales = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerAnhoSalida = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerAnhoInicio = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerAnhoFin = new SpinnerNumberModel();
    public static String nombreUsuarioLogeado = "";
    public static String nombreUsuarioAdmin = "";
    public static String nombreAnimeGestor = "";
    public static GestorAdmin ga;
    public static GestorNombreAnime gNa;

    public static void iniciar() {
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        consultas.getComboAnimesFavoritos().setModel(modeloComboAnimesFavoritos);
        consultas.getComboAnimesFavoritos().setPreferredSize(new Dimension(55, consultas.getComboAnimesFavoritos().getPreferredSize().height));
        consultas.getComboFavoritosUsuarios().setModel(modeloComboUsuariosFavoritos);
        consultas.getComboTipoUsuario().addItem("USER");
        consultas.getComboTipoUsuario().addItem("ADMIN");
        consultas.getSpValoracion().setModel(modeloSpinnerValoracion);
        consultas.getSpCapActual().setModel(modeloSpinnerCapActual);
        consultas.getSpCapTotales().setModel(modeloSpinnerCapTotales);
        consultas.getSpAnhoSalida().setModel(modeloSpinnerAnhoSalida);
        reportAdmin.getSpAnhoInicioParametro().setModel(modeloSpinnerAnhoInicio);
        reportAdmin.getSpAnhoFinParametro().setModel(modeloSpinnerAnhoFin);
        modeloSpinnerValoracion.setMinimum(0);
        modeloSpinnerValoracion.setMaximum(5);
        modeloSpinnerCapActual.setMinimum(0);
        modeloSpinnerCapTotales.setMinimum(0);
        modeloSpinnerAnhoSalida.setMinimum(1960);
        modeloSpinnerAnhoSalida.setMaximum(LocalDate.now().getYear());//En el futuro cambiarlo para animes que saldrán más adelante
        modeloSpinnerAnhoInicio.setMinimum(1960);
        modeloSpinnerAnhoInicio.setMaximum(LocalDate.now().getYear());
        modeloSpinnerAnhoFin.setMinimum(1960);
        modeloSpinnerAnhoFin.setMaximum(LocalDate.now().getYear());
    }

    public static void buildConnection(String ip, String user, String pass) throws Exception {
        HibernateUtil.buildSessionFactory(ip, user, pass);
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
        comprobarExistenciaUsuarios();
        comprobarExistenciaAnimes();
        cargarCombos();
        consultas.setVisible(true);
        consultas.setLocationRelativeTo(null);
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
        consultas.getSpAnhoSalida().setValue(1960);
        consultas.getTxtDescripcion().setText("");
        consultas.getTxtImagen().setText("");
        consultas.getSpCapTotales().setValue(0);
        consultas.getSpValoracion().setValue(0);
        consultas.getSpCapActual().setValue(0);
    }

    public static void iniciarReportAdmin() {
        reiniciarReportAdmin();
        reportAdmin.setVisible(true);
        reportAdmin.setLocationRelativeTo(null);
    }

    public static void reiniciarReportAdmin() {
        reportAdmin.getSpAnhoInicioParametro().setValue(1960);
        reportAdmin.getSpAnhoFinParametro().setValue(1960);
        reportAdmin.getTxtNombreParametro().setText("");
    }

    public static void iniciarModAdmin(Frame parent) {
        nombreUsuarioAdmin = consultas.getTxtNombreUsuario().getText();
        ga = new GestorAdmin(parent, true);
        ga.getComboTipoUsuario().addItem("USER");
        ga.getComboTipoUsuario().addItem("ADMIN");
        ga.setVisible(true);
        ga.setLocationRelativeTo(null);
    }

    public static void iniciarModAnime(Frame parent) {
        nombreAnimeGestor = consultas.getTxtNombreAnime().getText().trim();
        gNa = new GestorNombreAnime(parent, true);
        gNa.setVisible(true);
        gNa.setLocationRelativeTo(null);
    }

    public static Usuario loginUsuario() {
        Usuario u = null;
        login.getLblLogin().setText("");
        char[] pw = login.getTxtPasswordLogin().getPassword();
        String contrasenha = new String(pw);
        if (contrasenha.contains(" ")) {
            login.getLblLogin().setText("Login incorrecto");
        } else if (login.getTxtNombreLogin().getText().contains(" ")) {
            login.getLblLogin().setText("Login incorrecto");
        } else {
            if (login.getTxtNombreLogin().getText().isEmpty() || contrasenha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Faltan datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    if (usuDAO != null) {
                        String nombre = login.getTxtNombreLogin().getText();
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
                    } else {
                        JOptionPane.showMessageDialog(null, "Configuración no encontrada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos o buscar usuario", "Error", JOptionPane.ERROR_MESSAGE);
                    u = null;
                }
            }
        }
        return u;
    }

    public static void cerrarSesionUsuario() {
        nombreUsuarioLogeado = "";
    }

    public static void cerrarSesionUsuarioADMIN() {
        nombreUsuarioAdmin = "";
    }

    public static void modificarNombreUsuario() {
        if (gestorUsuario.getTxtNombreUsuario().getText().contains(" ")) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede contener espacios en blanco.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestorUsuario.getTxtNombreUsuario().getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nuevoNombre = gestorUsuario.getTxtNombreUsuario().getText();
            Usuario uExistente = usuDAO.buscarUsuarioPorNombre(session, nuevoNombre);
            if (uExistente == null) {
                Usuario uLogueado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
                if (uLogueado != null) {
                    usuDAO.modificarNombreUsuario(session, uLogueado, nuevoNombre);
                    JOptionPane.showMessageDialog(null, "Nombre de usuario modificado con éxito.");
                    nombreUsuarioLogeado = nuevoNombre;
                    gestorUsuario.getTxtNombreUsuario().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al loguearse.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nombre de usuario ya existente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarNombreUsuarioAdmin() {
        if (ga.getTxtNombreUsuario().getText().contains(" ")) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede contener espacios en blanco.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ga.getTxtNombreUsuario().getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nuevoNombre = ga.getTxtNombreUsuario().getText();
            Usuario uLogAdmin = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioAdmin);
            if (uLogAdmin != null) {
                if (!uLogAdmin.getNombre().equals(nuevoNombre)) {
                    Usuario uExistente = usuDAO.buscarUsuarioPorNombre(session, nuevoNombre);
                    if (uExistente == null) {
                        usuDAO.modificarNombreUsuario(session, uLogAdmin, nuevoNombre);
                        JOptionPane.showMessageDialog(null, "Nombre de usuario modificado con éxito.");
                        nombreUsuarioAdmin = nuevoNombre;
                        consultas.getTxtNombreUsuario().setText(nuevoNombre);
                        ga.getTxtNombreUsuario().setText("");
                        cargarComboUsuarios();
                    } else {
                        JOptionPane.showMessageDialog(null, "Nombre de usuario ya existente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No puede ponerse el mismo nombre de usuario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarContrasenhaUsuario() {
        char[] pw = gestorUsuario.getTxtPasswordUsuario().getPassword();
        String nuevaContrasenha = new String(pw);
        if (nuevaContrasenha.contains(" ")) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede contener espacios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nuevaContrasenha.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String contrasenhaEncriptada = BCrypt.hashpw(nuevaContrasenha, BCrypt.gensalt());
            Usuario uLogueado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
            if (uLogueado != null) {
                usuDAO.modificarContrasenhaUsuario(session, uLogueado, contrasenhaEncriptada);
                JOptionPane.showMessageDialog(null, "Cotraseña modificada con éxito.");
                gestorUsuario.getTxtPasswordUsuario().setText("");
                gestorUsuario.getCbVerPassword().setSelected(false);
                gestorUsuario.getTxtPasswordUsuario().setEchoChar('*');
            } else {
                JOptionPane.showMessageDialog(null, "Usuario a modificar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarContrasenhaUsuarioAdmin() {
        char[] pw = ga.getTxtPasswordUsuario().getPassword();
        String nuevaContrasenha = new String(pw);
        if (nuevaContrasenha.contains(" ")) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede contener espacios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nuevaContrasenha.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String contrasenhaEncriptada = BCrypt.hashpw(nuevaContrasenha, BCrypt.gensalt());
            Usuario uLogAdmin = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioAdmin);
            if (uLogAdmin != null) {
                if (BCrypt.checkpw(nuevaContrasenha, uLogAdmin.getContrasenha())) {
                    JOptionPane.showMessageDialog(null, "No puede poner la misma contraseña que tenía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    usuDAO.modificarContrasenhaUsuario(session, uLogAdmin, contrasenhaEncriptada);
                    JOptionPane.showMessageDialog(null, "Cotraseña modificada con éxito.");
                    consultas.getTxtPasswordFavoritos().setText(contrasenhaEncriptada);
                    ga.getTxtPasswordUsuario().setText("");
                    ga.getCbVerPassword().setSelected(false);
                    ga.getTxtPasswordUsuario().setEchoChar('*');
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario a modificar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarTipoUsuarioAdmin() {
        String tipo = (String) ga.getComboTipoUsuario().getSelectedItem();
        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean permisosDegradados = false;
        try {
            HibernateUtil.beginTx(session);
            if (ga.getComboTipoUsuario().getItemCount() > 0) {
                Usuario uLogAdmin = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioAdmin);
                if (uLogAdmin != null) {
                    if (!uLogAdmin.getTipo().equals(tipo)) {
                        usuDAO.modificarTipoUsuario(session, uLogAdmin, tipo);
                        JOptionPane.showMessageDialog(null, "Tipo de usuario modificado con éxito.");
                        consultas.getComboTipoUsuario().setSelectedItem(tipo);
                        if (nombreUsuarioAdmin.equals(nombreUsuarioLogeado)) {
                            permisosDegradados = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Tipo de usuario repetido. No modificado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            HibernateUtil.commitTx(session);
            if (permisosDegradados) {
                cerrarSesionUsuarioADMIN();
                cerrarSesionUsuario();
                ga.setVisible(false);
                consultas.setVisible(false);
                iniciarLogin();
            }
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darBajaUsuario() {
        try {
            HibernateUtil.beginTx(session);
            Usuario uLogueado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
            if (uLogueado != null) {
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
                    List<Favoritos> favoritos = favDAO.obtenerFavoritosPorUsuario(session, uLogueado);
                    for (Favoritos favorito : favoritos) {
                        favDAO.borrarFavorito(session, favorito);
                    }
                    usuDAO.borrarUsuario(session, uLogueado);
                    JOptionPane.showMessageDialog(null, "Usuario borrado con éxito.");
                    vaciarTxtGestor();
                    cerrarSesionUsuario();
                    gestorUsuario.setVisible(false);
                    controladorPrincipal.iniciarLogin();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario a borrar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void vaciarTxtGestor() {
        gestorUsuario.getTxtNombreUsuario().setText("");
        gestorUsuario.getTxtPasswordUsuario().setText("");
        gestorUsuario.getCbVerPassword().setSelected(false);
        gestorUsuario.getTxtPasswordUsuario().setEchoChar('*');
    }

    public static void cargarComboAnimes() {
        try {
            aniDAO.cargarCombo(session, modeloComboAnimesFavoritos);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void cargarComboUsuarios() {
        try {
            usuDAO.cargarCombo(session, modeloComboUsuariosFavoritos);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void cargarCombos() {
        try {
            cargarComboUsuarios();
            cargarComboAnimes();
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*public static boolean buscarEloy() {
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
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return encontrado;
    }*/

 /*public static void iniciarDatos() {
        try {
            HibernateUtil.beginTx(session);
            String contrasenha = "123";
            String contrasenhaEncriptada = BCrypt.hashpw(contrasenha, BCrypt.gensalt());
            usuDAO.guardarEloy(session, "Eloy", contrasenhaEncriptada, "USER");
            String contrasenha2 = "456";
            String contrasenhaEncriptada2 = BCrypt.hashpw(contrasenha2, BCrypt.gensalt());
            usuDAO.guardarEloy(session, "EloyAdmin", contrasenhaEncriptada2, "ADMIN");
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, "Error: ", e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    private static void vaciarTxtUsuarioFavoritos() {
        consultas.getTxtIdUsuario().setText("");
        consultas.getTxtPasswordFavoritos().setText("");
        consultas.getComboTipoUsuario().setSelectedItem("USER");
    }

    public static void comprobarExistenciaUsuarios() {
        if (consultas.getTxtNombreUsuario().getText().isEmpty()) {
            consultas.getBtnAltaUsuario().setEnabled(false);
            consultas.getBtnModUsuarioFavoritos().setEnabled(false);
            consultas.getBtnBajaUsuario().setEnabled(false);
            consultas.getTxtPasswordFavoritos().setEnabled(true);
            consultas.getComboTipoUsuario().setEnabled(true);
            vaciarTxtUsuarioFavoritos();
            return;
        }
        try {
            String nombre = consultas.getTxtNombreUsuario().getText();
            Usuario u = usuDAO.buscarUsuarioPorNombre(session, nombre);
            if (u != null) {
                consultas.getBtnAltaUsuario().setEnabled(false);
                consultas.getBtnModUsuarioFavoritos().setEnabled(true);
                consultas.getBtnBajaUsuario().setEnabled(true);
                consultas.getTxtPasswordFavoritos().setEnabled(false);
                consultas.getComboTipoUsuario().setEnabled(false);
                consultas.getTxtIdUsuario().setText(String.valueOf(u.getIdUsuario()));
                consultas.getTxtPasswordFavoritos().setText(u.getContrasenha());
                consultas.getComboTipoUsuario().setSelectedItem(u.getTipo());
            } else {
                vaciarTxtUsuarioFavoritos();
                consultas.getBtnAltaUsuario().setEnabled(true);
                consultas.getBtnModUsuarioFavoritos().setEnabled(false);
                consultas.getBtnBajaUsuario().setEnabled(false);
                consultas.getTxtPasswordFavoritos().setEnabled(true);
                consultas.getComboTipoUsuario().setEnabled(true);
            }
        } catch (NumberFormatException e) {
            vaciarTxtUsuarioFavoritos();
            consultas.getBtnAltaUsuario().setEnabled(false);
            consultas.getBtnModUsuarioFavoritos().setEnabled(true);
            consultas.getBtnBajaUsuario().setEnabled(true);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darAltaUsuario() {
        char[] pw = consultas.getTxtPasswordFavoritos().getPassword();
        String contrasenha = new String(pw);
        if (contrasenha.contains(" ")) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede contener espacios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (consultas.getTxtNombreUsuario().getText().contains(" ")) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede contener espacios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (consultas.getTxtNombreUsuario().getText().isEmpty() || contrasenha.isEmpty() || consultas.getComboTipoUsuario().getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombre = consultas.getTxtNombreUsuario().getText();
            Usuario uExistente = usuDAO.buscarUsuarioPorNombre(session, nombre);
            if (uExistente == null) {
                String contrasenhaEncriptada = BCrypt.hashpw(contrasenha, BCrypt.gensalt());
                String tipo = (String) consultas.getComboTipoUsuario().getSelectedItem();
                if (tipo != null) {
                    Usuario u = new Usuario(nombre, contrasenhaEncriptada, tipo);
                    usuDAO.darAltaUsuario(session, u);
                    JOptionPane.showMessageDialog(null, "Usuario creado con éxito.");
                    cargarComboUsuarios();
                    comprobarExistenciaUsuarios();
                } else {
                    JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nombre de usuario ya existente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
            consultas.getTxtIdUsuario().setText("");
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darBajaUsuarioFavoritos() {
        try {
            HibernateUtil.beginTx(session);
            String nombre = consultas.getTxtNombreUsuario().getText();
            Usuario u = usuDAO.buscarUsuarioPorNombre(session, nombre);
            Usuario uLogueado = usuDAO.buscarUsuarioPorNombre(session, nombreUsuarioLogeado);
            if (u != null) {
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
                    List<Favoritos> favoritos = favDAO.obtenerFavoritosPorUsuario(session, u);
                    for (Favoritos favorito : favoritos) {
                        favDAO.borrarFavorito(session, favorito);
                    }
                    usuDAO.borrarUsuario(session, u);
                    JOptionPane.showMessageDialog(null, "Usuario borrado con éxito.");
                    comprobarExistenciaUsuarios();
                    consultas.getTxtNombreUsuario().setText("");
                    cargarComboUsuarios();

                    if (uLogueado.getNombre().equals(nombre)) {
                        cerrarSesionUsuario();
                        consultas.setVisible(false);
                        controladorPrincipal.iniciarLogin();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario a borrar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darAltaFavoritos() {
        if (consultas.getComboAnimesFavoritos().getItemCount() == 0 || consultas.getComboFavoritosUsuarios().getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);

            Anime a = (Anime) consultas.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) consultas.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                int valoracion = Integer.parseInt(consultas.getSpValoracion().getValue().toString());
                int capActual = Integer.parseInt(consultas.getSpCapActual().getValue().toString());
                FavoritosPK fPK = new FavoritosPK(a.getIdAnime(), u.getIdUsuario());
                Favoritos f = new Favoritos(fPK, a, u, valoracion, capActual);

                favDAO.darAltaFavorito(session, f);
                JOptionPane.showMessageDialog(null, "Anime añadido a favoritos.");
                comprobarExistenciaFavoritos();
            } else {
                JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarFavoritos() {
        if (consultas.getComboAnimesFavoritos().getItemCount() == 0 || consultas.getComboFavoritosUsuarios().getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);

            Anime a = (Anime) consultas.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) consultas.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                int nuevaValoracion = Integer.parseInt(consultas.getSpValoracion().getValue().toString());
                int nuevoCapActual = Integer.parseInt(consultas.getSpCapActual().getValue().toString());
                Favoritos f = favDAO.buscarFavorito(session, a, u);

                favDAO.modificarFavorito(session, f, nuevaValoracion, nuevoCapActual);
                JOptionPane.showMessageDialog(null, "Anime modificado en favoritos.");
                comprobarExistenciaFavoritos();
            } else {
                JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darBajaFavoritos() {
        try {
            HibernateUtil.beginTx(session);
            Anime a = (Anime) consultas.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) consultas.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                Favoritos f = favDAO.buscarFavorito(session, a, u);

                favDAO.darBajaFavorito(session, f);
                JOptionPane.showMessageDialog(null, "Anime eliminado de favoritos.");
                comprobarExistenciaFavoritos();
            } else {
                JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void comprobarExistenciaFavoritos() {
        try {
            Anime a = (Anime) consultas.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) consultas.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                Favoritos f = favDAO.buscarFavorito(session, a, u);
                if (f != null) {
                    consultas.getBtnAltaFavorito().setEnabled(false);
                    consultas.getBtnBajaFavorito().setEnabled(true);
                    consultas.getBtnModFavorito().setEnabled(true);
                    modeloSpinnerCapActual.setMaximum(f.getAnime().getCapTotales());
                    modeloSpinnerCapActual.setValue(f.getCapActual());
                    modeloSpinnerValoracion.setValue(f.getValoracion());
                } else {
                    consultas.getBtnAltaFavorito().setEnabled(true);
                    consultas.getBtnBajaFavorito().setEnabled(false);
                    consultas.getBtnModFavorito().setEnabled(false);
                    modeloSpinnerCapActual.setMaximum(a.getCapTotales());
                    modeloSpinnerCapActual.setValue(0);
                    modeloSpinnerValoracion.setValue(0);
                }
            } else {
                consultas.getBtnAltaFavorito().setEnabled(false);
                consultas.getBtnBajaFavorito().setEnabled(false);
                consultas.getBtnModFavorito().setEnabled(false);
                modeloSpinnerCapActual.setValue(0);
                modeloSpinnerValoracion.setValue(0);
            }
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void vaciarTxtAnime() {
        consultas.getTxtIdAnime().setText("");
        consultas.getTxtCategorias().setText("");
        consultas.getSpAnhoSalida().setValue(1960);
        consultas.getTxtDescripcion().setText("");
        consultas.getTxtImagen().setText("");
        modeloSpinnerCapTotales.setValue(0);
    }

    public static void comprobarExistenciaAnimes() {
        if (consultas.getTxtNombreAnime().getText().trim().isEmpty()) {
            consultas.getBtnCambiarNombreAnime().setEnabled(false);
            consultas.getBtnAltaAnime().setEnabled(false);
            consultas.getBtnModAnime().setEnabled(false);
            consultas.getBtnBajaAnime().setEnabled(false);
            vaciarTxtAnime();
            return;
        }
        try {
            String nombre = consultas.getTxtNombreAnime().getText().trim();
            Anime a = aniDAO.buscarAnimePorNombre(session, nombre);
            if (a != null) {
                consultas.getBtnCambiarNombreAnime().setEnabled(true);
                consultas.getBtnAltaAnime().setEnabled(false);
                consultas.getBtnModAnime().setEnabled(true);
                consultas.getBtnBajaAnime().setEnabled(true);
                consultas.getTxtIdAnime().setText(String.valueOf(a.getIdAnime()));
                consultas.getTxtCategorias().setText(a.getCategorias());
                consultas.getSpAnhoSalida().setValue(a.getAnhoSalida());
                consultas.getTxtDescripcion().setText(a.getDescripcion());
                consultas.getTxtImagen().setText(a.getImagen());
                modeloSpinnerCapTotales.setValue(a.getCapTotales());
            } else {
                vaciarTxtAnime();
                consultas.getBtnCambiarNombreAnime().setEnabled(false);
                consultas.getBtnAltaAnime().setEnabled(true);
                consultas.getBtnModAnime().setEnabled(false);
                consultas.getBtnBajaAnime().setEnabled(false);
            }
        } catch (NumberFormatException e) {
            vaciarTxtAnime();
            consultas.getBtnCambiarNombreAnime().setEnabled(true);
            consultas.getBtnAltaAnime().setEnabled(false);
            consultas.getBtnModAnime().setEnabled(true);
            consultas.getBtnBajaAnime().setEnabled(true);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darAltaAnime() {
        if (consultas.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombreAnime = consultas.getTxtNombreAnime().getText().trim();
            String categorias = consultas.getTxtCategorias().getText();
            int anhoSalida = Integer.parseInt(consultas.getSpAnhoSalida().getValue().toString());
            String descripcion = consultas.getTxtDescripcion().getText();
            String imagen = consultas.getTxtImagen().getText();
            int capTotales = Integer.parseInt(consultas.getSpCapTotales().getValue().toString());
            Anime a = new Anime(nombreAnime, categorias, anhoSalida, descripcion, imagen, capTotales);
            aniDAO.darAltaAnime(session, a);
            JOptionPane.showMessageDialog(null, "Anime añadido con éxito.");
            comprobarExistenciaAnimes();
            cargarComboAnimes();
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modAnime() {
        if (consultas.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombreAnime = consultas.getTxtNombreAnime().getText().trim();
            Anime a = aniDAO.buscarAnimePorNombre(session, nombreAnime);
            if (a != null) {
                String nuevasCategorias = consultas.getTxtCategorias().getText();
                int nuevoAnhoSalida = Integer.parseInt(consultas.getSpAnhoSalida().getValue().toString());
                String nuevaDescripcion = consultas.getTxtDescripcion().getText();
                String nuevaImagen = consultas.getTxtImagen().getText();
                int nuevoCapTotales = Integer.parseInt(consultas.getSpCapTotales().getValue().toString());
                a.setCategorias(nuevasCategorias);
                a.setAnhoSalida(nuevoAnhoSalida);
                a.setDescripcion(nuevaDescripcion);
                a.setImagen(nuevaImagen);
                a.setCapTotales(nuevoCapTotales);
                aniDAO.modificarAnime(session, a);
                JOptionPane.showMessageDialog(null, "Anime modificado con éxito.");
                favDAO.modificarFavoritosCambioEpisodios(session, a);
                cargarComboAnimes();
            } else {
                JOptionPane.showMessageDialog(null, "Anime a modificar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darBajaAnime() {
        if (consultas.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombreAnime = consultas.getTxtNombreAnime().getText().trim();
            Anime a = aniDAO.buscarAnimePorNombre(session, nombreAnime);
            if (a != null) {
                List<Favoritos> favoritos = favDAO.buscarFavoritosPorAnime(session, a);
                if (favoritos != null) {
                    for (Favoritos favorito : favoritos) {
                        favDAO.borrarFavorito(session, favorito);
                    }
                }
                aniDAO.darBajaAnime(session, a);
                JOptionPane.showMessageDialog(null, "Anime eliminado con éxito.");
                cargarComboAnimes();
            } else {
                JOptionPane.showMessageDialog(null, "Anime a eliminar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (NumberFormatException e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarNombreAnime(Dialog dialog) {
        if (gNa.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nuevoNombre = gNa.getTxtNombreAnime().getText();
            Anime a = aniDAO.buscarAnimePorNombre(session, nombreAnimeGestor);
            if (a != null) {
                if (!a.getNombre().equals(nuevoNombre)) {
                    Anime aExistente = aniDAO.buscarAnimePorNombre(session, nuevoNombre);
                    if (aExistente == null) {
                        aniDAO.modificarNombreAnime(session, a, nuevoNombre);
                        JOptionPane.showMessageDialog(null, "Nombre del anime modificado con éxito.");
                        nombreAnimeGestor = nuevoNombre;
                        consultas.getTxtNombreAnime().setText(nuevoNombre);
                        dialog.dispose();
                        cargarComboAnimes();
                    } else {
                        JOptionPane.showMessageDialog(null, "Anime ya existente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No puede ponerse el mismo nombre.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Anime a modificar no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            HibernateUtil.commitTx(session);
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
