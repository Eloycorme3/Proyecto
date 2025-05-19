/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.nimesuki.datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 *
 * @author eloy.castro
 */
public class DynamicEntityManagerFactory {

    private static final Map<String, EntityManagerFactory> cache = new ConcurrentHashMap<>();

    public static EntityManager createEntityManager(DataSource dataSource, String key) {
        EntityManagerFactory emf = cache.computeIfAbsent(key, k -> {
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setPackagesToScan("proyecto.nimesuki.modelo");
            factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

            Map<String, Object> props = new HashMap<>();
            props.put("hibernate.hbm2ddl.auto", "none");
            props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

            factoryBean.setJpaPropertyMap(props);
            factoryBean.afterPropertiesSet();

            return factoryBean.getObject();
        });

        return emf.createEntityManager();
    }
}
