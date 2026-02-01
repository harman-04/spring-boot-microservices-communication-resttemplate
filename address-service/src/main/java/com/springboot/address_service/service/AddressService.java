// Package declaration → groups related classes together.
package com.springboot.address_service.service;

import com.springboot.address_service.entity.Address;              // Address entity mapped to DB table.
import com.springboot.address_service.repository.AddressRepository; // Repository for Address CRUD operations.
import com.springboot.address_service.response.AddressResponse;   // DTO for sending address data to client/microservice.
import org.modelmapper.ModelMapper;                              // Library for mapping between Entity ↔ DTO.
import org.springframework.beans.factory.annotation.Autowired;   // Enables dependency injection.
import org.springframework.stereotype.Service;                   // Marks this class as a Spring-managed service bean.

// @Service → Marks this class as a service component.
// Service classes contain business logic and are managed by Spring’s IoC container.
@Service
public class AddressService {

    // ================================
    // Dependencies (Injected by Spring)
    // ================================
    @Autowired
    private AddressRepository addressRepository; // Handles DB operations for Address entity.

    @Autowired
    private ModelMapper mapper;                  // Maps Address entity → AddressResponse DTO.

    // ================================
    // Method: findAddressByEmployeeId
    // ================================
    // Purpose:
    // - Fetch address details from DB using employeeId.
    // - Convert Address entity → AddressResponse DTO using ModelMapper.
    // - Return structured response to controller.
    public AddressResponse findAddressByEmployeeId(int employeeId) {
        // Step 1: Fetch address by employeeId from DB.
        // Repository returns Optional<Address> → safe handling if no record exists.
        Address addressByEmployeeId = addressRepository.findAddressByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Step 2: Map Address entity → AddressResponse DTO.
        // ModelMapper automatically copies matching fields (id, city, state).
        AddressResponse addressResponse = mapper.map(addressByEmployeeId, AddressResponse.class);

        // Step 3: Return DTO response.
        return addressResponse;
    }
}