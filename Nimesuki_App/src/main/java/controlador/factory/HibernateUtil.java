/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.factory;

import modelo.dao.AnimeDAO;
import modelo.dao.FavoritosDAO;
import modelo.dao.UsuarioDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author eloy.castro
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static void buildSessionFactory(String ip, String user, String pass) throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        configuration.addAnnotatedClass(modelo.vo.Usuario.class);
        configuration.addAnnotatedClass(modelo.vo.Anime.class);
        configuration.addAnnotatedClass(modelo.vo.Favoritos.class);

        configuration.setProperty("hibernate.connection.url",
                "jdbc:mysql://" + ip + ":3306/nimesuki?zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=UTC&connectTimeout=1000&socketTimeout=1000");
        configuration.setProperty("hibernate.connection.username", user);
        configuration.setProperty("hibernate.connection.password", pass);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        if (sessionFactory != null) {
            sessionFactory.close();
        }

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory no inicializado. Llama a buildSessionFactory primero.");
        }
        return sessionFactory;
    }

    /**
     * *************** PARA GESTIONAR LAS TRANSACCIONES **************
     */
    public static Transaction beginTx(Session s) {
        if (s.getTransaction() == null || !s.getTransaction().isActive()) {
            return s.beginTransaction();
        }
        return sessionFactory.getCurrentSession().getTransaction();
    }

    public static void commitTx(Session s) {
        if (s.getTransaction().isActive()) {
            s.getTransaction().commit();
        }
    }

    public static void rollbackTx(Session s) {
        if (s.getTransaction().isActive()) {
            s.getTransaction().rollback();
        }
    }

    /**
     * ********************** INCORPORA LOS MÉTODOS PARA CREAR LOS OBJETOS DAO
     * *******
     */
    public static AnimeDAO getAnimeDAO() {
        return new AnimeDAO();
    }

    public static FavoritosDAO getFavoritosDAO() {
        return new FavoritosDAO();
    }

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }
}
