package com.training.callum.whoms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application entrypoint for the warehouse-management-service.
 */
@SpringBootApplication
public final class WarehouseOperationsManagementApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOperationsManagementApplication.class);

    private WarehouseOperationsManagementApplication() {
        // prevent instantiation
    }

    public static void main(String[] args) {
        LOGGER.info("Starting WarehouseOperationsManagementApplication");
        SpringApplication.run(WarehouseOperationsManagementApplication.class, args);
    }
}