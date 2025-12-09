package io.github.prittspadelord.application;

import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.web.SpringServletContainerInitializer;

import java.io.File;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class SpringContactsApplication {

    static void main() {
        Tomcat tomcat = new Tomcat();

        Connector connector = tomcat.getConnector();
        connector.setPort(Integer.parseInt(System.getenv("PORT")));
        connector.getProtocolHandler().setExecutor(new VirtualThreadTaskExecutor());
        tomcat.setConnector(connector);

        Context tomcatContext = tomcat.addContext("", new File(".").getAbsolutePath());
        tomcatContext.addServletContainerInitializer(
            new SpringServletContainerInitializer(),
            Set.of(ContactsAppServletInitializer.class)
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info("Gracefully shutting down the server...");
                tomcat.stop();
                tomcat.getServer().await();
                deleteDirectory(new File("tomcat." + System.getenv("PORT")));
                log.info("Shutdown complete");
            }
            catch(LifecycleException e) {
                log.error("Failed to stop the server due to {} with message: {}", e.getClass().getName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }));

        try {
            tomcat.start();
            tomcat.getServer().await();
        }
        catch(LifecycleException e) {
            log.error("Failed to start the server due to {} with message: {}", e.getClass().getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void deleteDirectory(File directory) {
        for(File subDirectory: Objects.requireNonNull(directory.listFiles())) {
            deleteDirectory(subDirectory);
        }

        boolean _ = directory.delete();
    }
}
