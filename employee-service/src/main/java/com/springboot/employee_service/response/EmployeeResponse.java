// Package declaration → groups related classes together.
package com.springboot.employee_service.response;

import lombok.AllArgsConstructor; // Lombok → generates constructor with all fields.
import lombok.Data;               // Lombok → generates getters, setters, toString, equals, hashCode.
import lombok.NoArgsConstructor;  // Lombok → generates default no-argument constructor.

// @Data → Lombok generates boilerplate code (getters/setters, equals, hashCode, toString).
// @AllArgsConstructor → Constructor with all fields.
// @NoArgsConstructor → Default constructor.
// This class is a DTO (Data Transfer Object) used to represent employee data
// combined with address data fetched from another microservice.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    // ================================
    // Employee ID
    // ================================
    // Unique identifier for the employee.
    private int id;

    // ================================
    // Employee Name
    // ================================
    // Full name of the employee.
    private String name;

    // ================================
    // Employee Email
    // ================================
    // Email address of the employee.
    private String email;

    // ================================
    // Employee Age
    // ================================
    // Age of the employee.
    // Note: Stored as String here, could be changed to int for numeric operations.
    private String age;

    // ================================
    // Address Response
    // ================================
    // Nested DTO object that holds address details.
    // This is fetched from the Address Service microservice via RestTemplate.
    private AddressResponse addressResponse;
}