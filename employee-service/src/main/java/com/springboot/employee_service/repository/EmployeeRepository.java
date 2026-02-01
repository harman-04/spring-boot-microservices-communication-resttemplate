// Package declaration → groups related classes together.
package com.springboot.employee_service.repository;

import com.springboot.employee_service.entity.Employee; // Import the Employee entity.
import org.springframework.data.jpa.repository.JpaRepository; // Provides CRUD operations and query methods for JPA entities.
import org.springframework.stereotype.Repository;            // Marks this interface as a Spring-managed repository bean.

// @Repository → Marks this interface as a Spring Data repository.
// Spring will automatically detect it and create a proxy implementation at runtime.
// This allows you to perform database operations without writing SQL manually.
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // ================================
    // JpaRepository<Employee, Integer>
    // ================================
    // Employee → Entity type managed by this repository.
    // Integer → Type of the primary key (id field in Employee entity).
    //
    // JpaRepository provides built-in methods like:
    // - save(Employee entity) → Insert or update employee.
    // - findById(Integer id) → Find employee by primary key.
    // - findAll() → Get all employees.
    // - deleteById(Integer id) → Delete employee by primary key.
    // - count() → Count total employees.
    //
    // You can also define custom query methods here, for example:
    // Optional<Employee> findByEmail(String email);
    // List<Employee> findByName(String name);
}