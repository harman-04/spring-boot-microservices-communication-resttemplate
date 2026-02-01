// Package declaration → groups related classes together.
package com.springboot.address_service.configuration;

import org.modelmapper.ModelMapper;          // Library for mapping between DTOs and Entities.
import org.springframework.context.annotation.Bean;        // Marks a method as a Spring bean provider.
import org.springframework.context.annotation.Configuration; // Marks this class as a Spring configuration class.

// @Configuration → Marks this class as a Spring configuration class.
// Spring Boot will automatically detect and load it at startup.
// This is where you define beans that can be injected into other parts of the application.
@Configuration
public class AddressConfig {

    // ================================
    // Bean: ModelMapper
    // ================================
    // ModelMapper → A library that simplifies object mapping.
    // Example: Convert Address entity → AddressResponse DTO (Data Transfer Object).
    // Avoids manual copying of fields between objects.
    //
    // @Bean → Registers ModelMapper as a Spring-managed bean.
    // This allows it to be injected into services/controllers using @Autowired or constructor injection.
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}