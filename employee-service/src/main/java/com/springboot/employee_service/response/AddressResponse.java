// Package declaration → groups related classes together.
package com.springboot.employee_service.response;

import lombok.AllArgsConstructor; // Lombok → generates constructor with all fields.
import lombok.Data;               // Lombok → generates getters, setters, toString, equals, hashCode.
import lombok.NoArgsConstructor;  // Lombok → generates default no-argument constructor.

// @Data → Lombok generates boilerplate code (getters/setters, equals, hashCode, toString).
// @AllArgsConstructor → Constructor with all fields.
// @NoArgsConstructor → Default constructor.
// This class is a DTO (Data Transfer Object) used to represent address data
// fetched from another microservice (Address Service).
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    // ================================
    // Address ID
    // ================================
    // Represents the unique identifier of the address.
    // Typically matches the employee’s ID when fetched from Address Service.
    private int id;

    // ================================
    // City
    // ================================
    // Represents the city where the employee lives.
    private String city;

    // ================================
    // State
    // ================================
    // Represents the state where the employee lives.
    private String state;
}