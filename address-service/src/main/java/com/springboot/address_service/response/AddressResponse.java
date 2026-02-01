// Package declaration → groups related classes together.
package com.springboot.address_service.response;

import lombok.AllArgsConstructor; // Lombok → generates constructor with all fields.
import lombok.Data;               // Lombok → generates getters, setters, toString, equals, hashCode.
import lombok.NoArgsConstructor;  // Lombok → generates default no-argument constructor.

// @Data → Lombok generates boilerplate code (getters/setters, equals, hashCode, toString).
// @AllArgsConstructor → Constructor with all fields.
// @NoArgsConstructor → Default constructor.
// This class is a DTO (Data Transfer Object) used to represent address data
// that will be sent back to clients or other microservices.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    // ================================
    // Address ID
    // ================================
    // Unique identifier for the address record.
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