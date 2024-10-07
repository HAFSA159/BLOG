package com.blogapp.util;

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.blogapp.config.LoggerConfig;

public class HibernateUtil {
    private static final Logger logger = LoggerConfig.getLogger(HibernateUtil.class);
    private static final Supplier<SessionFactory> SESSION_FACTORY_SUPPLIER = HibernateUtil::buildSessionFactory;
    private static final Optional<SessionFactory> sessionFactory = Optional.ofNullable(SESSION_FACTORY_SUPPLIER.get());

    private static SessionFactory buildSessionFactory() {
        try {
            logger.info("Building Hibernate SessionFactory");
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Optional<SessionFactory> getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        logger.info("Shutting down Hibernate SessionFactory");
        getSessionFactory().ifPresent(SessionFactory::close);
    }
}