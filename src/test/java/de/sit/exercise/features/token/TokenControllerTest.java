package de.sit.exercise.features.token;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sit.exercise.components.auth.JwtTokenProvider;
import de.sit.exercise.features.customer.CustomerDto;
import de.sit.exercise.util.AbstractContainerTest;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class TokenControllerTest extends AbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider tokenProvider;

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
    @Order(1)
    @DisplayName("password is not checked, if no customer exists")
    void testCreateCategoryWithoutCredential() throws Exception {
        var credetial = Instancio.create(Credential.class);

        String payload = objectMapper.writeValueAsString(credetial);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        // response token must contain the CUSTOMER claim
        var response = result.getResponse().getContentAsString();
        var jwt = tokenProvider.getDecoder().decode(response);
        List<String> scopeClaim = jwt.getClaim("scope");
        Assertions.assertArrayEquals(Arrays.array("CUSTOMER"), scopeClaim.toArray());
    }

    /**
     *
     */
    @Test
    @Order(2)
    @DisplayName("password is checked, if at least one customer exists")
    void testPasswordIsChecked() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        // add a customer
        addCustomer(customer);

        // create another password
        var credential = Instancio.create(Credential.class);
        // email must be the same
        credential.setEmail(customer.getEmail());

        String payload = objectMapper.writeValueAsString(credential);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());

    }

    /**
     *
     */
    @Test
    @Order(3)
    @DisplayName("token is generated it email and password fits")
    void testGenerateTokenIfPasswordFits() throws Exception {
        var customer = Instancio.create(CustomerDto.class);

        // add a customer
        addCustomer(customer);

        // use the same credentials
        var credential = new Credential(customer.getPassword(), customer.getEmail());

        String payload = objectMapper.writeValueAsString(credential);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

    }

    /**
     * Add a Customer
     *
     * @param customer dto of the payload
     * @return response of the request as customer dto object
     */
    private CustomerDto addCustomer(CustomerDto customer) throws Exception {
        String payload = objectMapper.writeValueAsString(customer);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, CustomerDto.class);
    }

}
