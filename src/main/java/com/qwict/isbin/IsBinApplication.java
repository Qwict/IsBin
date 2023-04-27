package com.qwict.isbin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
public class IsBinApplication {
    public static final Properties appProps = new Properties();
    private static String port;

    public static void main(String[] args) {
        SpringApplication isBinApplication = new SpringApplication(IsBinApplication.class);

        String resourceName = "application.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.printf("INFO -- JavaServerMain -- main -- Loading application.properties from %s%n", resourceName);
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            appProps.load(resourceStream);
            port = appProps.getProperty("application.port");
        } catch (IOException e) {
            System.out.println("ERROR -- UserServiceImpl -- UserServiceImpl -- Could not load application.properties" + e.getMessage());
            System.exit(1);
        }

        if (port == null) {
            System.out.println("ERROR -- UserServiceImpl -- UserServiceImpl -- Could not find application.port in application.properties " +
                    "\n\t-> configure it and restart the server"
            );
            System.exit(1);
        }

        System.out.printf("INFO -- JavaServerMain -- main -- Starting JavaServer on port %s%n", port);


        isBinApplication.setDefaultProperties(Collections
                .singletonMap("server.port", port));
        isBinApplication.run(args);
    }

}