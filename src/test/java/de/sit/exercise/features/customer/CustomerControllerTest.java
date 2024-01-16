package de.sit.exercise.features.customer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sit.exercise.util.EntityGenerationHelper;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityGenerationHelper entityGenerator;

    @Container
    private static final MSSQLServerContainer<?> SQLSERVER_CONTAINER = new MSSQLServerContainer<>(
            "mcr.microsoft.com/mssql/server:2022-latest").acceptLicense();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", SQLSERVER_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", SQLSERVER_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", SQLSERVER_CONTAINER::getPassword);
    }

    /**
     *
     */
    @Test
    void testConnectionToDatabase() {
        Assertions.assertNotNull(repository);
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can create customers")
    void testCreateCustomerWithoutCredential() throws Exception {
        var customer = Instancio.create(CustomerDto.class);
        String payload = objectMapper.writeValueAsString(customer);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can update customers")
    void testUpdateCustomerWithoutCredential() throws Exception {

        var customer = Instancio.create(CustomerDto.class);

        // first a new customer
        entityGenerator.addCustomer(customer);

        // then update the customer
        var customerToUpdate = Instancio.create(CustomerDto.class);
        customerToUpdate.setId(customer.getId());
        String payload = objectMapper.writeValueAsString(customerToUpdate);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .put("/api/customer/" + customerToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can delete customers")
    void testDeleteCustomerWithoutCredential() throws Exception {

        var customer = Instancio.create(CustomerDto.class);

        // first a new customer
        entityGenerator.addCustomer(customer);

        // then update the customer
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .delete("/api/customer/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Even unauthenticated user can read customers")
    void testReadCustomerWithoutCredential() throws Exception {

        var customer = Instancio.create(CustomerDto.class);

        // first a new customer
        entityGenerator.addCustomer(customer);

        // then update the customer
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/customer/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Create a Customer")
    void testAddCustomer() throws Exception {
        var customer = Instancio.create(CustomerDto.class);
        entityGenerator.addCustomer(customer);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Create")
    void testReadCustomerAfterCreate() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        entityGenerator.addCustomer(customer);
        var addedCustomer = entityGenerator.readCustomer(customer.getId());

        // the password must be set to null for the comparison, as it is not read by the database
        customer.setPassword(null);
        Assertions.assertEquals(customer, addedCustomer);
    }

    /**
     *
     */
    @Test
    @DisplayName("Password must not be read from the database")
    void testPasswordMustBeEmpty() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        entityGenerator.addCustomer(customer);
        var addedCustomer = entityGenerator.readCustomer(customer.getId());

        Assertions.assertNull(addedCustomer.getPassword());
    }

    /**
     *
     */
    @Test
    @DisplayName("Password must be hashed in the database")
    void testPasswordMustBeHashed() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        entityGenerator.addCustomer(customer);
        var addedCustomer = repository.findById(customer.getId()).get();

        Assertions.assertTrue(passwordEncoder.matches(customer.getPassword(), addedCustomer.getPassword()));
    }

    /**
     *
     */
    @Test
    @DisplayName("Create multiple Customers")
    void testAddCustomers() throws Exception {
        var customers = Instancio.ofList(CustomerDto.class).size(20).create();

        for (var customer : customers) {
            // add a customer
            entityGenerator.addCustomer(customer);

            // read the added customer
            var addedCustomer = entityGenerator.readCustomer(customer.getId());

            // check each created customer
            // the password must be set to null for the comparison, as it is not read by the database
            customer.setPassword(null);

            Assertions.assertEquals(customer, addedCustomer);
        }
        Assertions.assertTrue(repository.findAll().size() >= customers.size());
    }

    /**
     *
     */
    @Test
    @DisplayName("Update a Customer")
    void testUpdateCustomers() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        // first a new customer
        entityGenerator.addCustomer(customer);

        // then update the customer
        var customerToUpdate = Instancio.create(CustomerDto.class);
        customerToUpdate.setId(customer.getId());
        entityGenerator.updateCustomer(customer.getId(), customerToUpdate);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Update")
    void testReadCustomerAfterUpdate() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        // add a new customer
        entityGenerator.addCustomer(customer);

        // then update the customer
        var customerToUpdate = Instancio.create(CustomerDto.class);
        customerToUpdate.setId(customer.getId());
        entityGenerator.updateCustomer(customer.getId(), customerToUpdate);

        // then update the customer
        var updateCustomer = entityGenerator.readCustomer(customer.getId());

        // the password must be set to null for the comparison, as it is not read by the database
        customerToUpdate.setPassword(null);
        Assertions.assertEquals(customerToUpdate, updateCustomer);
    }

    /**
     *
     */
    @Test
    @DisplayName("Email must be unique")
    void testEmailMustBeUnique() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        // add a new customer
        entityGenerator.addCustomer(customer);

        var secondCustomer = Instancio.create(CustomerDto.class);
        secondCustomer.setEmail(customer.getEmail());
        var secondPayload = objectMapper.writeValueAsString(secondCustomer);

        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", entityGenerator.getBearerToken())
                        .content(secondPayload))
                .andExpect(status().isConflict());
    }

}
