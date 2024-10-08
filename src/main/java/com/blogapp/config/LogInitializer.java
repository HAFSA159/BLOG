package com.blogapp.config;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LogInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String logsDir = System.getProperty("user.dir") + File.separator + "logs";
        File logsDirFile = new File(logsDir);
        if (!logsDirFile.exists()) {
            logsDirFile.mkdirs();
        }
        System.setProperty("log.dir", logsDir);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed
    }
}