// Package declaration → groups related classes together.
package com.springboot.address_service.repository;

import com.springboot.address_service.entity.Address;       // Import the Address entity.
import org.springframework.data.jpa.repository.JpaRepository; // Provides CRUD operations and query methods for JPA entities.
import org.springframework.data.jpa.repository.Query;         // Allows defining custom SQL queries.
import org.springframework.data.repository.query.Param;       // Used to bind method parameters to query parameters.
import org.springframework.stereotype.Repository;             // Marks this interface as a Spring-managed repository bean.

import java.util.Optional; // Represents a container that may or may not hold a non-null value.

// @Repository → Marks this interface as a Spring Data repository.
// Spring will automatically detect it and create a proxy implementation at runtime.
// This allows you to perform database operations without writing SQL manually.
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    // ================================
    // Custom Query: Find Address by Employee ID
    // ================================
    // @Query → Defines a custom SQL query.
    // nativeQuery = true → Means this is a raw SQL query (not JPQL).
    // value = "SELECT * FROM address WHERE employee_id = :employeeId"
    // → Fetches the address record where employee_id matches the given parameter.
    //
    // @Param("employeeId") → Binds the method parameter to the SQL query parameter.
    //
    // Optional<Address> → Return type ensures safe handling:
    // - If address exists → returns Address object.
    // - If not found → returns Optional.empty().
    @Query(nativeQuery = true,
            value = "SELECT * FROM address WHERE employee_id = :employeeId")
    Optional<Address> findAddressByEmployeeId(@Param("employeeId") int employeeId);

    // ================================
    // Example Usage in Service Layer
    // ================================
    // addressRepository.findAddressByEmployeeId(1)
    // → Executes SQL: SELECT * FROM address WHERE employee_id = 1
    // → Returns Optional<Address>
    // → You can handle result like:
    // Address address = addressRepository.findAddressByEmployeeId(1)
    //        .orElseThrow(() -> new RuntimeException("Address not found"));
}