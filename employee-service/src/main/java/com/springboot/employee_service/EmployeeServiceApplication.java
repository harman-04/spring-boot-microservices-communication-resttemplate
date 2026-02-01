// Package declaration → groups related classes together.
package com.springboot.employee_service;

import org.springframework.boot.SpringApplication;             // Provides a way to launch the Spring Boot application.
import org.springframework.boot.autoconfigure.SpringBootApplication; // Marks this class as the main Spring Boot app.

// @SpringBootApplication → This is a special annotation that combines three important features:
// 1. @Configuration → Allows defining beans and configuration inside this class.
// 2. @EnableAutoConfiguration → Automatically configures Spring Boot based on dependencies (Web, JPA, Lombok, etc.).
// 3. @ComponentScan → Automatically scans the package and sub-packages for Spring components (@Controller, @Service, @Repository, etc.).
//
// In short: It tells Spring Boot "This is the main application class, start everything from here."
@SpringBootApplication
public class EmployeeServiceApplication {

    // ================================
    // Main Method → Entry Point
    // ================================
    // When you run this method, Spring Boot starts:
    // - Creates an application context (Spring container).
    // - Configures beans (like EmployeeService, EmployeeRepository, EmployeeController).
    // - Loads entities (like Employee).
    // - Starts an embedded server (Tomcat by default).
    // - Applies Spring MVC configuration (REST endpoints).
    // - Makes your endpoints (/employees/{id}) available.
    //
    // In this project:
    // - EmployeeController exposes REST endpoints.
    // - EmployeeService fetches employee data + calls Address Service.
    // - EmployeeRepository interacts with the database.
    // - DTOs (EmployeeResponse, AddressResponse) structure the response.
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}