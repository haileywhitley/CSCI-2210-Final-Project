package oop.ayearout.finalProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Clinic Scheduler Spring Boot application.
 *
 * <p>This class bootstraps the Spring context, configures the
 * embedded server, and connects to the SQLite database on startup.
 *
 * @author Abbey Yearout
 * @version Final Project
 * @bugs None
 */
@SpringBootApplication
public class ClinicSchedulerApplication {

    /**
     * Launches the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ClinicSchedulerApplication.class, args);
    }
}
