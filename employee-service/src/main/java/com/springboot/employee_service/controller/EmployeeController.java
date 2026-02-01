// Package declaration → groups related classes together.
package com.springboot.employee_service.controller;

import com.springboot.employee_service.response.EmployeeResponse; // DTO returned to client (Employee + Address).
import com.springboot.employee_service.service.EmployeeService;  // Service layer containing business logic.
import lombok.RequiredArgsConstructor;                          // Lombok → generates constructor for final fields.
import org.springframework.http.HttpStatus;                     // Represents HTTP status codes.
import org.springframework.http.ResponseEntity;                 // Represents HTTP response with body + status.
import org.springframework.web.bind.annotation.GetMapping;      // Maps HTTP GET requests to controller methods.
import org.springframework.web.bind.annotation.PathVariable;    // Extracts values from URL path.
import org.springframework.web.bind.annotation.RestController;  // Marks this class as a REST controller (returns JSON).

// @RestController → Marks this class as a REST API controller.
// Combines @Controller + @ResponseBody → methods return JSON responses instead of HTML views.
// @RequiredArgsConstructor → Lombok generates constructor for final fields (dependency injection).
@RestController
@RequiredArgsConstructor
public class EmployeeController {

    // ================================
    // Dependency Injection
    // ================================
    // EmployeeService is injected automatically via constructor (thanks to @RequiredArgsConstructor).
    // This allows the controller to delegate business logic to the service layer.
    private final EmployeeService employeeService;

    // ================================
    // GET Endpoint: Fetch Employee Details
    // ================================
    // URL: GET /employees/{id}
    // @PathVariable → extracts "id" from the URL.
    // Calls EmployeeService.getEmployeeById(id).
    // Returns ResponseEntity<EmployeeResponse> → structured JSON response with HTTP status.
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable int id) {
        // Step 1: Call service layer to fetch employee + address details.
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);

        // Step 2: Wrap response in ResponseEntity with HTTP 200 OK status.
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
    }
}