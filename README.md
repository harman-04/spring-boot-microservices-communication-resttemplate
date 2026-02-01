
#  Microservices: Synchronous Communication (Employee & Address)

## Project Overview

This project demonstrates the **Microservices Pattern**. Instead of a single database table containing all employee and address data, we have two isolated services, each with its own database responsibility.

---

##  Technical Concepts

### 1. Distributed Logic

* **Employee Service**: Manages core employee details (Name, Email, Age).
* **Address Service**: Manages location details (City, State) linked by an `employeeId`.
  When a user requests employee details, the `Employee Service` acts as an **aggregator**—it fetches its own data and then "calls" the `Address Service` to complete the picture.

### 2. Inter-Service Communication (`RestTemplate`)

The `Employee Service` needs data it doesn't own. It uses `RestTemplate` to make an HTTP GET request to the other service:

```java
restTemplate.getForObject("http://localhost:8081/address-service/address/{id}", AddressResponse.class, id);

```

This is **Synchronous Communication**: the Employee service waits for the Address service to respond before sending the final result to the user.

### 3. Model Mapping (`ModelMapper`)

To keep our code clean, we don't return Entity objects directly. We use `ModelMapper` to automatically convert our Database Entities (`Employee`, `Address`) into Response DTOs (`EmployeeResponse`, `AddressResponse`). This decouples our database schema from our API contract.

---

##  Component Reference

### `EmployeeService.java`

The "Orchestrator." It performs three steps:

1. Fetch `Employee` from its own MySQL repo.
2. Map it to `EmployeeResponse`.
3. Call `Address Service` via URL to fetch `AddressResponse` and "set" it inside the employee object.

### `AddressRepository.java`

Uses a **Native Query** to find an address based on an `employee_id`. This demonstrates how microservices use foreign keys to maintain relationships across different service contexts.

---
## Key Concepts Explained Simply:
### Component Analysis

| Component              | Responsibility & Implementation Details                                                                                                                                                                                                                                                                                                                                                                          |
|:-----------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Employee Entity**    | **@Entity**: Marks this class as a JPA entity for Hibernate mapping.<br>**@Table(name = "employee")**: Explicitly defines the database table name.<br>**@Id / @GeneratedValue**: Defines primary key with identity auto-increment strategy.<br>**@Column**: Maps Java fields to specific database columns.<br>**Lombok**: Uses @Data, @AllArgsConstructor, and @NoArgsConstructor to eliminate boilerplate code. |
| **EmployeeRepository** | **JpaRepository<Employee, Integer>**: Inherits full CRUD functionality and pagination.<br>**@Repository**: Marks as a Spring bean and enables automatic exception translation.<br>**Query Methods**: Supports derivation (e.g., findByEmail) and @Query annotations for custom SQL/JPQL.                                                                                                                         |
| **EmployeeConfig**     | **@Configuration**: Registers the class as a source of bean definitions for the Spring IoC container.<br>**ModelMapper Bean**: Provides an instance for automated Entity-to-DTO conversion.<br>**RestTemplate Bean**: Enables synchronous HTTP communication with external microservices (e.g., Address Service).                                                                                                |
| **EmployeeService**    | **Data Orchestration**: Combines local data from EmployeeRepository with external data from RestTemplate.<br>**Mapping**: Utilizes ModelMapper to transform internal Entities into external-facing EmployeeResponse DTOs.<br>**Microservice Integration**: Acts as the client for the Address Service to aggregate a complete user profile.                                                                      |
---
## Overall Flow
This is a classic microservices communication example. You have two independent Spring Boot services:

1. **Employee Service** (Running on default port `8080`): Handles employee basic details.
2. **Address Service** (Running on port `8081`, based on the hardcoded URL in your code): Handles address details linked to an employee ID.

The Employee Service acts as the orchestrator, fetching its own data first, then making a synchronous REST call (using `RestTemplate`) to the Address Service to gather the complete picture before returning it to the client.

Here is the complete breakdown of the flow.

---

### 1. High-Level Architecture View

Before diving into the classes, visualize the two services interacting:

`Client (Browser/Postman)` -> `[Employee Service (8080)]` -> `(RestTemplate GET call)` -> `[Address Service (8081)]`

---

### 2. Sequence Diagram: The Flow of `getEmployeeDetails`

This diagram shows exactly which class calls which method across the two services and the databases.
![Gemini_Generated_Image_tojppotojppotojp.png](images/Gemini_Generated_Image_tojppotojppotojp.png)
---

### 3. Detailed Step-by-Step Walkthrough

#### Phase 1: Incoming Request & Local Data Fetch (Employee Service)

1. **The Request:** A client sends a `GET` request to `http://localhost:8080/employees/101`.
2. **Controller:** The `EmployeeController.getEmployeeDetails(101)` method is hit. It immediately delegates the business logic to the service layer.
   * *Method call:* `employeeService.getEmployeeById(101)`
3. **Service (DB Call):** The `EmployeeService` needs to find the basic employee data first.
   * *Method call:* `employeeRepository.findById(101)`
4. **Repository & DB:** The `EmployeeRepository` (extending JpaRepository) executes a `SELECT` query against the employee database table.
   * *Result:* An `Employee` entity (ID: 101, Name: John, Email:..., Age:...) is returned to the service. (Or an exception is thrown if not found).
5. **Mapping (Entity to DTO):** The service needs to convert the database entity into a response DTO. It uses the injected `ModelMapper` bean.
   * *Method call:* `modelMapper.map(employeeEntity, EmployeeResponse.class)`
   * *Result:* An `EmployeeResponse` object is created with basic details populated, but the `addressResponse` field is currently null.
#### Phase 2: Inter-Service Communication (The Bridge)
6. **The RestTemplate Call:** This is the crucial step. The `EmployeeService` knows it needs address data but doesn't have access to the address database. It uses the injected `RestTemplate` bean to make an HTTP call to the other microservice.
   * *Code:* `restTemplate.getForObject("http://localhost:8081/address-service/address/101", AddressResponse.class, 101);`
   * *Action:* `RestTemplate` creates an HTTP GET request and sends it to port 8081. It pauses execution in the Employee Service, waiting for a response.



#### Phase 3: Address Service Processing (Remote Service)

7. **Receiving the Request:** The `AddressController` on port 8081 receives the GET request at `/address/101`.
   * *Method call:* `addressService.findAddressByEmployeeId(101)`


8. **Service & Repo (Address DB):** The `AddressService` calls its repository to find address records linked to employee ID 101.
   * *Method call:* `addressRepository.findAddressByEmployeeId(101)`
   * *DB Action:* The native query `SELECT * FROM address WHERE employee_id = :employeeId` is executed against the address database.
   * *Result:* An `Address` entity (City: NY, State: NY, employeeId: 101) is returned.


9. **Mapping (Address Entity to DTO):** The `AddressService` uses its own `ModelMapper` to convert the entity.
   * *Method call:* `mapper.map(addressByEmployeeId, AddressResponse.class)`


10. **Sending Response back:** The `AddressController` wraps the resulting `AddressResponse` object in a `ResponseEntity` and returns it as JSON with a 200 OK status back to the caller (the RestTemplate).

#### Phase 4: Aggregation & Final Response (Employee Service)

11. **Receiving Remote Data:** Back in the `EmployeeService`, the `RestTemplate` receives the JSON response from the Address Service and automatically deserializes (converts) it into an `AddressResponse` Java object.
12. **Aggregation:** The `EmployeeService` now has the basic `EmployeeResponse` (from step 5) and the `AddressResponse` (from step 11). It combines them.
    * *Code:* `employeeResponse.setAddressResponse(addressResponse);`


13. **Final Return:** The fully populated `EmployeeResponse` is returned up the chain to the `EmployeeController`, which sends it as the final JSON response to the client.
---

##  How to Run & Test

### Step 1: Start Address Service (Port 8081)

Ensure the `application.properties` for the address service is set to `server.port=8081`.
This service must be running first.

### Step 2: Start Employee Service (Port 8080)

This service will run on the default port.

### Step 3: Verify the Flow

Send a request to the Employee Service:
`GET http://localhost:8080/employees/1`

**Expected JSON Response:**

```json
{
  "id": 1,
  "name": "Vishu",
  "email": "vishu@example.com",
  "age": "25",
  "addressResponse": {
    "id": 101,
    "city": "Ludhiana",
    "state": "Punjab"
  }
}

```

