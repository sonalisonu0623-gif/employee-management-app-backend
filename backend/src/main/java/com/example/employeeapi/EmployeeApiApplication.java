package com.example.employeeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class EmployeeApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApiApplication.class, args);
        logger.info("======================================================");
        logger.info("  Employee Management API started successfully!");
        logger.info("  API Base URL: http://localhost:8080/api/employees");
        logger.info("======================================================");
    }
}
