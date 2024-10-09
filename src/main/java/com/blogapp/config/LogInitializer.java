package com.blogapp.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.nio.file.Paths;

@WebListener
public class LogInitializer implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(LogInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        
        // Get the path to the project's root directory
        String projectRoot = System.getProperty("user.dir");
        
        // Create a 'logs' directory in the project root
        String logPath = Paths.get(projectRoot, "logs").toString();
        File logDir = new File(logPath);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        
        System.setProperty("log.dir", logPath);
        System.out.println("Log directory set to: " + logPath);
        logger.info("LogInitializer: Application context initialized. Log directory: {}", logPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("LogInitializer: Application context destroyed");
    }
}