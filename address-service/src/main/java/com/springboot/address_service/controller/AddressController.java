// Package declaration → groups related classes together.
package com.springboot.address_service.controller;

import com.springboot.address_service.response.AddressResponse; // DTO returned to client (address details).
import com.springboot.address_service.service.AddressService;  // Service layer containing business logic.
import lombok.RequiredArgsConstructor;                        // Lombok → generates constructor for final fields.
import org.springframework.http.HttpStatus;                   // Represents HTTP status codes.
import org.springframework.http.ResponseEntity;               // Represents HTTP response with body + status.
import org.springframework.web.bind.annotation.GetMapping;    // Maps HTTP GET requests to controller methods.
import org.springframework.web.bind.annotation.PathVariable;  // Extracts values from URL path.
import org.springframework.web.bind.annotation.RestController;// Marks this class as a REST controller (returns JSON).

// @RestController → Marks this class as a REST API controller.
// Combines @Controller + @ResponseBody → methods return JSON responses instead of HTML views.
// @RequiredArgsConstructor → Lombok generates constructor for final fields (dependency injection).
@RestController
@RequiredArgsConstructor
public class AddressController {

    // ================================
    // Dependency Injection
    // ================================
    // AddressService is injected automatically via constructor (thanks to @RequiredArgsConstructor).
    // This allows the controller to delegate business logic to the service layer.
    private final AddressService addressService;

    // ================================
    // GET Endpoint: Fetch Address by Employee ID
    // ================================
    // URL: GET /address/{employeeId}
    // @PathVariable("employeeId") → extracts employeeId from the URL.
    // Calls AddressService.findAddressByEmployeeId(employeeId).
    // Returns ResponseEntity<AddressResponse> → structured JSON response with HTTP status.
    @GetMapping("/address/{employeeId}")
    public ResponseEntity<AddressResponse> getAddressByEmployeeId(@PathVariable("employeeId") int employeeId) {
        // Step 1: Call service layer to fetch address details by employeeId.
        AddressResponse addressResponse = addressService.findAddressByEmployeeId(employeeId);

        // Step 2: Wrap response in ResponseEntity with HTTP 200 OK status.
        return ResponseEntity.status(HttpStatus.OK).body(addressResponse);
    }
}