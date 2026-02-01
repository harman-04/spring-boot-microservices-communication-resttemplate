// Package declaration → groups related classes together.
package com.springboot.employee_service.service;

import com.springboot.employee_service.entity.Employee;              // Employee entity mapped to DB table.
import com.springboot.employee_service.repository.EmployeeRepository; // Repository for Employee CRUD operations.
import com.springboot.employee_service.response.AddressResponse;    // DTO for address data (from Address Service).
import com.springboot.employee_service.response.EmployeeResponse;   // DTO for employee data (combined with address).
import org.modelmapper.ModelMapper;                                // Library for mapping between Entity ↔ DTO.
import org.springframework.beans.factory.annotation.Autowired;     // Enables dependency injection.
import org.springframework.stereotype.Service;                     // Marks this class as a Spring-managed service bean.
import org.springframework.web.client.RestTemplate;                // Utility for making REST API calls to other services.

// @Service → Marks this class as a service component.
// Service classes contain business logic and are managed by Spring’s IoC container.
@Service
public class EmployeeService {

    // ================================
    // Dependencies (Injected by Spring)
    // ================================
    @Autowired
    private EmployeeRepository employeeRepository; // Handles DB operations for Employee entity.

    @Autowired
    private ModelMapper modelMapper;               // Maps Employee entity → EmployeeResponse DTO.

    @Autowired
    private RestTemplate restTemplate;             // Calls external microservice (Address Service).

    // ================================
    // Method: getEmployeeById
    // ================================
    // Purpose:
    // - Fetch employee details from DB.
    // - Map entity → DTO using ModelMapper.
    // - Call Address Service via REST API to fetch employee’s address.
    // - Combine both into EmployeeResponse DTO.
    public EmployeeResponse getEmployeeById(int id) {
        // Step 1: Fetch employee from DB.
        // If not found → throw RuntimeException.
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Step 2: Map Employee entity → EmployeeResponse DTO.
        // Avoids manual field copying.
        EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);

        // Step 3: Call Address Service (another microservice).
        // URL: http://localhost:8081/address-service/address/{id}
        // Pass employee ID → fetch corresponding address.
        AddressResponse addressResponse = restTemplate.getForObject(
                "http://localhost:8081/address-service/address/{id}",
                AddressResponse.class,
                id
        );

        // Step 4: Attach address data to EmployeeResponse DTO.
        employeeResponse.setAddressResponse(addressResponse);

        // Step 5: Return combined response (Employee + Address).
        return employeeResponse;
    }
}