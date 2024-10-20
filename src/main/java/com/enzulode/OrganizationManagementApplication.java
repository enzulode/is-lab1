package com.enzulode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Jar-level application entrypoint holder.
 */
@SpringBootApplication
public class OrganizationManagementApplication {

    /**
     * Jar-level application entrypoint.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OrganizationManagementApplication.class, args);
    }
}
