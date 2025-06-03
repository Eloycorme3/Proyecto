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
import vista.Consultas;
import vista.GestorAdminUsuario;
import vista.GestorNombreAnime;
import vista.GestorUsuario;
import vista.GestorAdministrador;
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
    public static GestorAdministrador gestorAdministrador = new GestorAdministrador();
    public static Login login = new Login();
    public static ReportAdmin reportAdmin = new ReportAdmin();
    public static Consultas consultas = new Consultas();
    static DefaultComboBoxModel modeloComboUsuariosFavoritos = new DefaultComboBoxModel();
    static DefaultComboBoxModel modeloComboAnimesFavoritos = new DefaultComboBoxModel();
    static SpinnerNumberModel modeloSpinnerValoracion = new SpinnerNumberModel(0, 0, 5, 0.5);
    static SpinnerNumberModel modeloSpinnerCapActual = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerCapTotales = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerAnhoSalida = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerAnhoInicio = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerAnhoFin = new SpinnerNumberModel();
    static SpinnerNumberModel modeloSpinnerLimiteAnimes = new SpinnerNumberModel();
    public static String nombreUsuarioLogeado = "";
    public static String nombreUsuarioAdmin = "";
    public static String nombreAnimeGestor = "";
    public static GestorAdminUsuario ga;
    public static GestorNombreAnime gNa;

    public static void iniciar() {
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        gestorAdministrador.getComboAnimesFavoritos().setModel(modeloComboAnimesFavoritos);
        gestorAdministrador.getComboAnimesFavoritos().setPreferredSize(new Dimension(55, gestorAdministrador.getComboAnimesFavoritos().getPreferredSize().height));
        gestorAdministrador.getComboFavoritosUsuarios().setModel(modeloComboUsuariosFavoritos);
        gestorAdministrador.getComboTipoUsuario().addItem("USER");
        gestorAdministrador.getComboTipoUsuario().addItem("ADMIN");
        gestorAdministrador.getSpValoracion().setModel(modeloSpinnerValoracion);
        gestorAdministrador.getSpCapActual().setModel(modeloSpinnerCapActual);
        gestorAdministrador.getSpCapTotales().setModel(modeloSpinnerCapTotales);
        gestorAdministrador.getSpAnhoSalida().setModel(modeloSpinnerAnhoSalida);
        reportAdmin.getSpAnhoInicio().setModel(modeloSpinnerAnhoInicio);
        reportAdmin.getSpAnhoFin().setModel(modeloSpinnerAnhoFin);
        reportAdmin.getSpLimiteAnimes().setModel(modeloSpinnerLimiteAnimes);
//        modeloSpinnerValoracion.setMinimum(0);
//        modeloSpinnerValoracion.setMaximum(5);
//        modeloSpinnerValoracion.setStepSize(0.5);
        modeloSpinnerCapActual.setMinimum(0);
        modeloSpinnerCapTotales.setMinimum(0);
        modeloSpinnerAnhoSalida.setMinimum(1960);
        modeloSpinnerAnhoSalida.setMaximum(LocalDate.now().getYear());//En el futuro cambiarlo para animes que saldrán más adelante
        modeloSpinnerAnhoInicio.setMinimum(1960);
        modeloSpinnerAnhoInicio.setMaximum(LocalDate.now().getYear());
        modeloSpinnerAnhoFin.setMinimum(1960);
        modeloSpinnerAnhoFin.setMaximum(LocalDate.now().getYear());
        modeloSpinnerLimiteAnimes.setMinimum(0);
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

    public static void iniciarGestorAdministrador() {
        reiniciarGestorAdministrador();
        comprobarExistenciaUsuarios();
        comprobarExistenciaAnimes();
        cargarCombos();
        gestorAdministrador.setVisible(true);
        gestorAdministrador.setLocationRelativeTo(null);
    }

    private static void reiniciarGestorAdministrador() {
        gestorAdministrador.getTxtIdUsuario().setText("");
        gestorAdministrador.getTxtNombreUsuario().setText("");
        gestorAdministrador.getTxtPasswordFavoritos().setText("");
        gestorAdministrador.getCbVerPasswordFavoritos().setSelected(false);
        gestorAdministrador.getTxtPasswordFavoritos().setEchoChar('*');
        gestorAdministrador.getTxtIdAnime().setText("");
        gestorAdministrador.getTxtNombreAnime().setText("");
        gestorAdministrador.getTxtCategorias().setText("");
        gestorAdministrador.getSpAnhoSalida().setValue(1960);
        gestorAdministrador.getTxtDescripcion().setText("");
        gestorAdministrador.getTxtImagen().setText("");
        gestorAdministrador.getSpCapTotales().setValue(0);
        gestorAdministrador.getSpValoracion().setValue(0);
        gestorAdministrador.getSpCapActual().setValue(0);
    }

    public static void iniciarReportAdmin() {
        reiniciarReportAdmin();
        reportAdmin.setVisible(true);
        reportAdmin.setLocationRelativeTo(null);
    }

    public static void reiniciarReportAdmin() {
        reportAdmin.getSpAnhoInicio().setValue(1960);
        reportAdmin.getSpAnhoFin().setValue(1960);
        reportAdmin.getTxtNombreAnime().setText("");
        reportAdmin.getTxtNombreUsuario().setText("");
        reportAdmin.getSpLimiteAnimes().setValue(0);
    }

    public static void iniciarConsultas() {
        reiniciarConsultas();
        consultas.setVisible(true);
        consultas.setLocationRelativeTo(null);
    }

    public static void reiniciarConsultas() {
        consultas.getTxtUsuarioPrimerListado().setText("");
        consultas.getTxtValoracionPrimerListado().setText("");
        consultas.getTxtAnimeSegundoListado().setText("");
        consultas.getTxtCapitulosMaxSegundoListado().setText("");
        consultas.getTaPrimerListado().setText("");
        consultas.getTaSegundoListado().setText("");
        consultas.getTaPrimerListadoCompleto().setText("");
        consultas.getTaSegundoListadoCompleto().setText("");
    }

    public static void iniciarModAdmin(Frame parent) {
        nombreUsuarioAdmin = gestorAdministrador.getTxtNombreUsuario().getText();
        ga = new GestorAdminUsuario(parent, true);
        ga.getComboTipoUsuario().addItem("USER");
        ga.getComboTipoUsuario().addItem("ADMIN");
        ga.setVisible(true);
        ga.setLocationRelativeTo(null);
    }

    public static void iniciarModAnime(Frame parent) {
        nombreAnimeGestor = gestorAdministrador.getTxtNombreAnime().getText().trim();
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

    public static Usuario registrarUsuario(String secretPass) {
        Usuario u = null;
        login.getLblLogin().setText("");
        char[] pw = login.getTxtPasswordLogin().getPassword();
        String contrasenha = new String(pw);
        if (contrasenha.contains(" ")) {
            login.getLblLogin().setText("Registro incorrecto");
        } else if (login.getTxtNombreLogin().getText().contains(" ")) {
            login.getLblLogin().setText("Registro incorrecto");
        } else {
            if (login.getTxtNombreLogin().getText().isEmpty() || contrasenha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Faltan datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    if (usuDAO != null) {
                        String nombre = login.getTxtNombreLogin().getText();
                        Usuario uBuscado = usuDAO.buscarUsuarioPorNombre(session, nombre);
                        if (uBuscado == null) {
                            String contrasenhaHasheada = BCrypt.hashpw(contrasenha, BCrypt.gensalt());
                            if (secretPass.equals("luffyg5")) {
                                u = new Usuario(nombre, contrasenhaHasheada, "ADMIN");
                            } else {
                                u = new Usuario(nombre, contrasenhaHasheada, "USER");
                            }
                            usuDAO.darAltaUsuario(session, u);
                            nombreUsuarioLogeado = u.getNombre();
                        } else {
                            login.getLblLogin().setText("Registro incorrecto");
                            JOptionPane.showMessageDialog(null, "Nombre de usuario ya existente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
                        gestorAdministrador.getTxtNombreUsuario().setText(nuevoNombre);
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
                    gestorAdministrador.getTxtPasswordFavoritos().setText(contrasenhaEncriptada);
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
                        gestorAdministrador.getComboTipoUsuario().setSelectedItem(tipo);
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
                gestorAdministrador.setVisible(false);
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
        gestorAdministrador.getTxtIdUsuario().setText("");
        gestorAdministrador.getTxtPasswordFavoritos().setText("");
        gestorAdministrador.getComboTipoUsuario().setSelectedItem("USER");
    }

    public static void comprobarExistenciaUsuarios() {
        if (gestorAdministrador.getTxtNombreUsuario().getText().isEmpty()) {
            gestorAdministrador.getBtnAltaUsuario().setEnabled(false);
            gestorAdministrador.getBtnModUsuarioFavoritos().setEnabled(false);
            gestorAdministrador.getBtnBajaUsuario().setEnabled(false);
            gestorAdministrador.getTxtPasswordFavoritos().setEnabled(true);
            gestorAdministrador.getComboTipoUsuario().setEnabled(true);
            vaciarTxtUsuarioFavoritos();
            return;
        }
        try {
            String nombre = gestorAdministrador.getTxtNombreUsuario().getText();
            Usuario u = usuDAO.buscarUsuarioPorNombre(session, nombre);
            if (u != null) {
                gestorAdministrador.getBtnAltaUsuario().setEnabled(false);
                gestorAdministrador.getBtnModUsuarioFavoritos().setEnabled(true);
                gestorAdministrador.getBtnBajaUsuario().setEnabled(true);
                gestorAdministrador.getTxtPasswordFavoritos().setEnabled(false);
                gestorAdministrador.getComboTipoUsuario().setEnabled(false);
                gestorAdministrador.getTxtIdUsuario().setText(String.valueOf(u.getIdUsuario()));
                gestorAdministrador.getTxtPasswordFavoritos().setText(u.getContrasenha());
                gestorAdministrador.getComboTipoUsuario().setSelectedItem(u.getTipo());
            } else {
                vaciarTxtUsuarioFavoritos();
                gestorAdministrador.getBtnAltaUsuario().setEnabled(true);
                gestorAdministrador.getBtnModUsuarioFavoritos().setEnabled(false);
                gestorAdministrador.getBtnBajaUsuario().setEnabled(false);
                gestorAdministrador.getTxtPasswordFavoritos().setEnabled(true);
                gestorAdministrador.getComboTipoUsuario().setEnabled(true);
            }
        } catch (NumberFormatException e) {
            vaciarTxtUsuarioFavoritos();
            gestorAdministrador.getBtnAltaUsuario().setEnabled(false);
            gestorAdministrador.getBtnModUsuarioFavoritos().setEnabled(true);
            gestorAdministrador.getBtnBajaUsuario().setEnabled(true);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darAltaUsuario() {
        char[] pw = gestorAdministrador.getTxtPasswordFavoritos().getPassword();
        String contrasenha = new String(pw);
        if (contrasenha.contains(" ")) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede contener espacios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestorAdministrador.getTxtNombreUsuario().getText().contains(" ")) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario no puede contener espacios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestorAdministrador.getTxtNombreUsuario().getText().isEmpty() || contrasenha.isEmpty() || gestorAdministrador.getComboTipoUsuario().getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombre = gestorAdministrador.getTxtNombreUsuario().getText();
            Usuario uExistente = usuDAO.buscarUsuarioPorNombre(session, nombre);
            if (uExistente == null) {
                String contrasenhaEncriptada = BCrypt.hashpw(contrasenha, BCrypt.gensalt());
                String tipo = (String) gestorAdministrador.getComboTipoUsuario().getSelectedItem();
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
            gestorAdministrador.getTxtIdUsuario().setText("");
        } catch (Exception e) {
            HibernateUtil.rollbackTx(session);
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darBajaUsuarioFavoritos() {
        try {
            HibernateUtil.beginTx(session);
            String nombre = gestorAdministrador.getTxtNombreUsuario().getText();
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
                    gestorAdministrador.getTxtNombreUsuario().setText("");
                    cargarComboUsuarios();

                    if (uLogueado.getNombre().equals(nombre)) {
                        cerrarSesionUsuario();
                        gestorAdministrador.setVisible(false);
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
        if (gestorAdministrador.getComboAnimesFavoritos().getItemCount() == 0 || gestorAdministrador.getComboFavoritosUsuarios().getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);

            Anime a = (Anime) gestorAdministrador.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) gestorAdministrador.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                float valoracion = Float.parseFloat(gestorAdministrador.getSpValoracion().getValue().toString());
                int capActual = Integer.parseInt(gestorAdministrador.getSpCapActual().getValue().toString());
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
        if (gestorAdministrador.getComboAnimesFavoritos().getItemCount() == 0 || gestorAdministrador.getComboFavoritosUsuarios().getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);

            Anime a = (Anime) gestorAdministrador.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) gestorAdministrador.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                float nuevaValoracion = Float.parseFloat(gestorAdministrador.getSpValoracion().getValue().toString());
                int nuevoCapActual = Integer.parseInt(gestorAdministrador.getSpCapActual().getValue().toString());
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
            Anime a = (Anime) gestorAdministrador.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) gestorAdministrador.getComboFavoritosUsuarios().getSelectedItem();
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
            Anime a = (Anime) gestorAdministrador.getComboAnimesFavoritos().getSelectedItem();
            Usuario u = (Usuario) gestorAdministrador.getComboFavoritosUsuarios().getSelectedItem();
            if (a != null && u != null) {
                Favoritos f = favDAO.buscarFavorito(session, a, u);
                if (f != null) {
                    gestorAdministrador.getBtnAltaFavorito().setEnabled(false);
                    gestorAdministrador.getBtnBajaFavorito().setEnabled(true);
                    gestorAdministrador.getBtnModFavorito().setEnabled(true);
                    modeloSpinnerCapActual.setMaximum(f.getAnime().getCapTotales());
                    modeloSpinnerCapActual.setValue(f.getCapActual());
                    modeloSpinnerValoracion.setValue(f.getValoracion());
                } else {
                    gestorAdministrador.getBtnAltaFavorito().setEnabled(true);
                    gestorAdministrador.getBtnBajaFavorito().setEnabled(false);
                    gestorAdministrador.getBtnModFavorito().setEnabled(false);
                    modeloSpinnerCapActual.setMaximum(a.getCapTotales());
                    modeloSpinnerCapActual.setValue(0);
                    modeloSpinnerValoracion.setValue(0);
                }
            } else {
                gestorAdministrador.getBtnAltaFavorito().setEnabled(false);
                gestorAdministrador.getBtnBajaFavorito().setEnabled(false);
                gestorAdministrador.getBtnModFavorito().setEnabled(false);
                modeloSpinnerCapActual.setValue(0);
                modeloSpinnerValoracion.setValue(0);
            }
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void vaciarTxtAnime() {
        gestorAdministrador.getTxtIdAnime().setText("");
        gestorAdministrador.getTxtCategorias().setText("");
        gestorAdministrador.getSpAnhoSalida().setValue(1960);
        gestorAdministrador.getTxtDescripcion().setText("");
        gestorAdministrador.getTxtImagen().setText("");
        modeloSpinnerCapTotales.setValue(0);
    }

    public static void comprobarExistenciaAnimes() {
        if (gestorAdministrador.getTxtNombreAnime().getText().trim().isEmpty()) {
            gestorAdministrador.getBtnCambiarNombreAnime().setEnabled(false);
            gestorAdministrador.getBtnAltaAnime().setEnabled(false);
            gestorAdministrador.getBtnModAnime().setEnabled(false);
            gestorAdministrador.getBtnBajaAnime().setEnabled(false);
            vaciarTxtAnime();
            return;
        }
        try {
            String nombre = gestorAdministrador.getTxtNombreAnime().getText().trim();
            Anime a = aniDAO.buscarAnimePorNombre(session, nombre);
            if (a != null) {
                gestorAdministrador.getBtnCambiarNombreAnime().setEnabled(true);
                gestorAdministrador.getBtnAltaAnime().setEnabled(false);
                gestorAdministrador.getBtnModAnime().setEnabled(true);
                gestorAdministrador.getBtnBajaAnime().setEnabled(true);
                gestorAdministrador.getTxtIdAnime().setText(String.valueOf(a.getIdAnime()));
                gestorAdministrador.getTxtCategorias().setText(a.getCategorias());
                gestorAdministrador.getSpAnhoSalida().setValue(a.getAnhoSalida());
                gestorAdministrador.getTxtDescripcion().setText(a.getDescripcion());
                gestorAdministrador.getTxtImagen().setText(a.getImagen());
                modeloSpinnerCapTotales.setValue(a.getCapTotales());
            } else {
                vaciarTxtAnime();
                gestorAdministrador.getBtnCambiarNombreAnime().setEnabled(false);
                gestorAdministrador.getBtnAltaAnime().setEnabled(true);
                gestorAdministrador.getBtnModAnime().setEnabled(false);
                gestorAdministrador.getBtnBajaAnime().setEnabled(false);
            }
        } catch (NumberFormatException e) {
            vaciarTxtAnime();
            gestorAdministrador.getBtnCambiarNombreAnime().setEnabled(true);
            gestorAdministrador.getBtnAltaAnime().setEnabled(false);
            gestorAdministrador.getBtnModAnime().setEnabled(true);
            gestorAdministrador.getBtnBajaAnime().setEnabled(true);
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void darAltaAnime() {
        if (gestorAdministrador.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombreAnime = gestorAdministrador.getTxtNombreAnime().getText().trim();
            String categorias = gestorAdministrador.getTxtCategorias().getText();
            int anhoSalida = Integer.parseInt(gestorAdministrador.getSpAnhoSalida().getValue().toString());
            String descripcion = gestorAdministrador.getTxtDescripcion().getText();
            String imagen = gestorAdministrador.getTxtImagen().getText();
            int capTotales = Integer.parseInt(gestorAdministrador.getSpCapTotales().getValue().toString());
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
        if (gestorAdministrador.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombreAnime = gestorAdministrador.getTxtNombreAnime().getText().trim();
            Anime a = aniDAO.buscarAnimePorNombre(session, nombreAnime);
            if (a != null) {
                String nuevasCategorias = gestorAdministrador.getTxtCategorias().getText();
                int nuevoAnhoSalida = Integer.parseInt(gestorAdministrador.getSpAnhoSalida().getValue().toString());
                String nuevaDescripcion = gestorAdministrador.getTxtDescripcion().getText();
                String nuevaImagen = gestorAdministrador.getTxtImagen().getText();
                int nuevoCapTotales = Integer.parseInt(gestorAdministrador.getSpCapTotales().getValue().toString());
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
        if (gestorAdministrador.getTxtNombreAnime().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Faltan Datos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            HibernateUtil.beginTx(session);
            String nombreAnime = gestorAdministrador.getTxtNombreAnime().getText().trim();
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
                        gestorAdministrador.getTxtNombreAnime().setText(nuevoNombre);
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

    public static void ejecutarPrimerListado() {
        try {
            float valoracion;
            if (consultas.getTxtValoracionPrimerListado().getText().trim().isEmpty()) {
                valoracion = 0f;
            } else {
                valoracion = Float.parseFloat(consultas.getTxtValoracionPrimerListado().getText().trim());
                if (valoracion > 5 || valoracion < 0) {
                    consultas.getTaPrimerListado().setText("Valoración errónea.");
                    return;
                }
            }
            favDAO.cargarAreaAnimesFavoritosPorUsuarioYValoracionMinima(session, consultas.getTaPrimerListado(), consultas.getTxtUsuarioPrimerListado().getText().trim(), valoracion);
        } catch (NumberFormatException e) {
            consultas.getTaPrimerListado().setText("Error numérico.");
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void ejecutarSegundoListado() {
        try {
            int capitulos;
            if (consultas.getTxtCapitulosMaxSegundoListado().getText().trim().isEmpty()) {
                capitulos = -1;
            } else {
                capitulos = Integer.parseInt(consultas.getTxtCapitulosMaxSegundoListado().getText().trim());
                if (capitulos < 0) {
                   consultas.getTaSegundoListado().setText("No se encontraron coincidencias."); 
                   return;
                }
            }
            aniDAO.cargarAreaAnimesPorNombreYCapitulosMaximos(session, consultas.getTaSegundoListado(), consultas.getTxtAnimeSegundoListado().getText(), capitulos);
        } catch (NumberFormatException e) {
            consultas.getTaSegundoListado().setText("Error numérico.");
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void ejecutarPrimerListadoCompleto() {
        try {
            aniDAO.cargarAreaAnimes(session, consultas.getTaPrimerListadoCompleto());
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void ejecutarSegundoListadoCompleto() {
        try {
            usuDAO.cargarAreaUsuarios(session, consultas.getTaSegundoListadoCompleto());
        } catch (Exception e) {
            Logger.getLogger(controladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
