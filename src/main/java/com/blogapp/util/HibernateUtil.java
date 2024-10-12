package com.blogapp.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Properties;
import java.io.FileInputStream;

public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static final String PERSISTENCE_UNIT_NAME = "blogapp";
    private static EntityManagerFactory entityManagerFactory;

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            try {
                logger.info("Initializing EntityManagerFactory");
                Properties props = new Properties();
                String configPath = HibernateUtil.class.getClassLoader().getResource("database.properties").getPath();
                logger.info("Loading properties from: " + configPath);
                props.load(new FileInputStream(configPath));
                logger.info("Properties loaded successfully");
                
                // Log the properties (be careful not to log sensitive information in production)
                for (String key : props.stringPropertyNames()) {
                    logger.info(key + ": " + (key.contains("password") ? "********" : props.getProperty(key)));
                }
                
                // Create a new properties object for Hibernate configuration
                Properties hibernateProps = new Properties();
                hibernateProps.setProperty("javax.persistence.jdbc.url", props.getProperty("jdbc.url"));
                hibernateProps.setProperty("javax.persistence.jdbc.user", props.getProperty("jdbc.user"));
                hibernateProps.setProperty("javax.persistence.jdbc.password", props.getProperty("jdbc.password"));
                
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, hibernateProps);
                logger.info("EntityManagerFactory initialized successfully");
            } catch (Exception ex) {
                logger.error("Initial EntityManagerFactory creation failed", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            logger.info("Closing EntityManagerFactory");
            entityManagerFactory.close();
        }
    }

    private HibernateUtil() {}
}
