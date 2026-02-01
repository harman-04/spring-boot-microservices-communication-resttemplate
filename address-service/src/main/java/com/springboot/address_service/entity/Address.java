// Package declaration → groups related classes together.
package com.springboot.address_service.entity;

import jakarta.persistence.*;          // JPA annotations for entity mapping.
import lombok.AllArgsConstructor;     // Lombok → generates constructor with all fields.
import lombok.Data;                   // Lombok → generates getters, setters, toString, equals, hashCode.
import lombok.NoArgsConstructor;      // Lombok → generates default no-argument constructor.

// @Entity → Marks this class as a JPA entity (mapped to a database table).
// @Table(name = "address") → Specifies the table name in the database.
// @Data → Lombok generates boilerplate code (getters/setters, equals, hashCode, toString).
// @AllArgsConstructor → Constructor with all fields.
// @NoArgsConstructor → Default constructor.
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address {

    // ================================
    // Primary Key (id)
    // ================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // @Id → Marks this field as the primary key.
    // @GeneratedValue(strategy = GenerationType.IDENTITY) → Auto-increment ID in the database.
    // @Column(name = "id") → Maps this field to the "id" column in the address table.
    private int id;

    // ================================
    // City
    // ================================
    @Column(name = "city")
    // Maps this field to the "city" column in the address table.
    private String city;

    // ================================
    // State
    // ================================
    @Column(name = "state")
    // Maps this field to the "state" column in the address table.
    private String state;

    // ================================
    // Employee ID (Foreign Key Reference)
    // ================================
    @Column(name = "employee_id")
    // Represents the ID of the employee associated with this address.
    // This creates a logical relationship between Employee and Address tables.
    // Note: Here it's just an integer field, but in advanced setups you could use @ManyToOne
    // to establish a proper JPA relationship with Employee entity.
    private int employeeId;
}