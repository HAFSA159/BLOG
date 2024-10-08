package com.blogapp.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static final String PERSISTENCE_UNIT_NAME = "blogapp";
    private static EntityManagerFactory entityManagerFactory;

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            try {
                logger.info("Initializing EntityManagerFactory");
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
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