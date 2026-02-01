
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
## Microservices Architecture Breakdown

### Employee Service (Port: 8080)

| Component | Responsibility & Implementation Details |
| :--- | :--- |
| **Employee Entity** | **@Entity**: Marks this class as a JPA entity where Hibernate maps it to a database table.<br>**@Table(name = "employee")**: Explicitly sets the table name to "employee" in MySQL.<br>**@Id + @GeneratedValue**: Defines the Primary key with an auto-increment strategy.<br>**@Column**: Maps Java fields directly to table columns.<br>**Lombok**: Includes **@Data** (getters/setters, etc.), **@AllArgsConstructor**, and **@NoArgsConstructor**. |
| **EmployeeRepository** | **JpaRepository<Employee, Integer>**: Provides CRUD operations out-of-the-box and saves boilerplate DAO code.<br>**@Repository**: Marks as a Spring bean and enables exception translation.<br>**Custom Queries**: Supports generated SQL based on method names (e.g., `findByEmail`). |
| **EmployeeConfig** | **@Configuration**: Marks this class as a configuration provider.<br>**ModelMapper Bean**: Used for mapping between Entity ↔ DTO to avoid manual field copying.<br>**RestTemplate Bean**: Used for making synchronous HTTP requests to other services (e.g., calling Address Service). |
| **EmployeeService** | **Orchestration**: Fetches primary employee data from MySQL and uses **RestTemplate** to call the external Address Service.<br>**Mapping**: Converts the internal Employee entity into an **EmployeeResponse** DTO.<br>**Aggregation**: Combines employee data + address data into a single response. |
| **AddressResponse (DTO)** | **DTO Pattern**: Used to transfer data between microservices while keeping responses lightweight.<br>**Lombok**: Uses **@Data**, **@AllArgsConstructor**, and **@NoArgsConstructor**.<br>**Integration**: EmployeeService maps the response from Address Service into this DTO to attach it to the EmployeeResponse. |
| **EmployeeResponse (DTO)** | **Structured Data**: Holds employee details (id, name, email, age) and a nested **AddressResponse** object.<br>**Clean API**: Keeps responses clean and avoids exposing internal entity details directly. |
| **EmployeeController** | **@RestController**: Marks this as a REST API controller returning JSON directly.<br>**@GetMapping("/employees/{id}")**: Maps GET requests with a path variable.<br>**ResponseEntity**: Wraps the response with HTTP status codes for proper RESTful formatting.<br>**Delegation**: Delegates logic to EmployeeService to keep the controller lightweight. |
| **Employee Service Application** | **@SpringBootApplication**: Marks the main entry point and triggers auto-configuration.<br>**Integration**: Boots the entire stack including Controller, Service, and Repository layers. |

---

### Address Service (Port: 8081)

| Component | Responsibility & Implementation Details |
| :--- | :--- |
| **Address Entity** | **@Entity**: Marks the class as a JPA entity.<br>**@Table(name = "address")**: Sets the table name to "address".<br>**@Id + @GeneratedValue**: Primary key with auto-increment.<br>**employeeId**: Links the address to a specific employee (Foreign Key logic). |
| **AddressRepository** | **JpaRepository**: Provides built-in CRUD methods.<br>**Custom Query (@Query)**: Allows writing raw SQL queries to fetch address by `employee_id`.<br>**Optional<Address>**: Prevents NullPointerExceptions and forces safe handling of missing data. |
| **AddressResponse (DTO)** | **DTO Pattern**: Transfers structured data while protecting internal entity structures.<br>**Integration**: The Address entity is mapped into this DTO via ModelMapper before being returned to the client or the Employee Service. |
| **AddressConfig** | **@Configuration**: Manages IoC beans.<br>**ModelMapper Bean**: Injected into AddressService to automate the Entity ↔ DTO transformation. |
| **AddressService** | **Service Layer**: Fetches data via AddressRepository using `employeeId`.<br>**Optional Handling**: Uses `.orElseThrow()` to ensure clear exceptions if no address exists.<br>**Encapsulation**: Keeps business logic separated from the web layer. |
| **AddressController** | **@RestController**: Handles HTTP requests and returns JSON responses.<br>**@GetMapping("/address/{employeeId}")**: Maps GET requests for specific employees.<br>**ResponseEntity**: Ensures the AddressResponse is sent with the correct HTTP status. |
| **Address Service Application** | **Main Class**: Uses `SpringApplication.run()` to load the specific Address Service context and beans. |

---

### application.properties (Infrastructure)

| Feature | Logic & Microservice Behavior |
| :--- | :--- |
| **Port Separation** | **Employee Service**: 8080<br>**Address Service**: 8081<br>Ensures both microservices can run simultaneously without port conflicts. |
| **Context Path** | Adds a prefix to all endpoints (e.g., `/address-service/`).<br>Helps organize APIs and prevents overlap when multiple services run on the same host. |
| **Service URLs** | **Employee**: `http://localhost:8080/employee-service/...`<br>**Address**: `http://localhost:8081/address-service/...` |
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

