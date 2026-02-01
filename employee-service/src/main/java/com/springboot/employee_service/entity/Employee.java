// Package declaration → groups related classes together.
package com.springboot.employee_service.entity;

import jakarta.persistence.*;          // JPA annotations for entity mapping.
import lombok.AllArgsConstructor;     // Lombok → generates constructor with all fields.
import lombok.Data;                   // Lombok → generates getters, setters, toString, equals, hashCode.
import lombok.NoArgsConstructor;      // Lombok → generates default no-argument constructor.

// @Entity → Marks this class as a JPA entity (mapped to a database table).
// @Table(name = "employee") → Specifies the table name in the database.
// @Data → Lombok generates boilerplate code (getters/setters, equals, hashCode, toString).
// @AllArgsConstructor → Constructor with all fields.
// @NoArgsConstructor → Default constructor.
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee {

    // ================================
    // Primary Key (id)
    // ================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // @Id → Marks this field as the primary key.
    // @GeneratedValue(strategy = GenerationType.IDENTITY) → Auto-increment ID in the database.
    // @Column(name = "id") → Maps this field to the "id" column in the employee table.
    private int id;

    // ================================
    // Employee Name
    // ================================
    @Column(name = "name")
    // Maps this field to the "name" column in the employee table.
    private String name;

    // ================================
    // Employee Email
    // ================================
    @Column(name = "email")
    // Maps this field to the "email" column in the employee table.
    private String email;

    // ================================
    // Employee Age
    // ================================
    @Column(name = "age")
    // Maps this field to the "age" column in the employee table.
    // Note: Currently stored as String → could be changed to int for numeric operations.
    private String age;
}