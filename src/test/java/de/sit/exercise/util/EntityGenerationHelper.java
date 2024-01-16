package de.sit.exercise.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sit.exercise.components.auth.JwtTokenProvider;
import de.sit.exercise.features.book.BookDto;
import de.sit.exercise.features.category.CategoryDto;
import de.sit.exercise.features.customer.CustomerDto;

@Service
public class EntityGenerationHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private String bearerToken;

    /**
     * used for getting the bearerToken for authentication
     */
    public String getBearerToken() {
        if (bearerToken == null) {
            bearerToken = "Bearer " + tokenProvider.generateToken("password");
        }
        return bearerToken;
    }

    /**
     * Add a Book
     *
     * @param book dto of the payload
     * @return response of the request as book dto object
     */
    public BookDto addbook(BookDto book) throws Exception {
        String payload = objectMapper.writeValueAsString(book);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, BookDto.class);
    }

    /**
     * Updates a Book
     *
     * @param id             unique id of the existing Book
     * @param updatedBookDto updated Book
     * @return response of the updated as book dto object
     */
    public BookDto updateBook(UUID id, BookDto updatedBookDto) throws Exception {
        String payload = objectMapper.writeValueAsString(updatedBookDto);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //^
                        .put("/api/book/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, BookDto.class);
    }

    /**
     * Read a Book
     *
     * @param id unique id of the book
     * @return response of the requested as book dto object
     */
    public BookDto readBook(UUID id) throws Exception {
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/book/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken()))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, BookDto.class);
    }

    /**
     * Add a Category
     *
     * @param category dto of the payload
     * @return response of the request as category dto object
     */
    public CategoryDto addCategory(CategoryDto category) throws Exception {
        String payload = objectMapper.writeValueAsString(category);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, CategoryDto.class);
    }

    /**
     * creates a new category for using as associated book category
     *
     * @return Id of the created category
     */
    public UUID getCatetoryId() throws Exception {
        var category = Instancio.create(CategoryDto.class);
        addCategory(category);
        return category.getId();
    }

    /**
     * Updates a Category
     *
     * @param id                 unique id of the existing Category
     * @param updatedCategoryDto updated Category
     * @return response of the updated as category dto object
     */
    public CategoryDto updateCategory(UUID id, CategoryDto updatedCategoryDto) throws Exception {
        String payload = objectMapper.writeValueAsString(updatedCategoryDto);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .put("/api/category/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, CategoryDto.class);
    }

    /**
     * Read a Category
     *
     * @param id unique id of the category
     * @return response of the requested as category dto object
     */
    public CategoryDto readCategory(UUID id) throws Exception {
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/category/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken()))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, CategoryDto.class);
    }

    /**
     * Add a Book
     *
     * @param book dto of the payload
     * @return response of the request as book dto object
     */
    public BookDto addBook(BookDto book) throws Exception {
        String payload = objectMapper.writeValueAsString(book);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, BookDto.class);
    }

    /**
     * Add a Customer
     *
     * @param customer dto of the payload
     * @return response of the request as customer dto object
     */
    public CustomerDto addCustomer(CustomerDto customer) throws Exception {
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

    /**
     * Updates a Customer
     *
     * @param id                 unique id of the existing Customer
     * @param updatedCustomerDto updated Customer
     * @return response of the updated as customer dto object
     */
    public CustomerDto updateCustomer(UUID id, CustomerDto updatedCustomerDto) throws Exception {
        String payload = objectMapper.writeValueAsString(updatedCustomerDto);
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .put("/api/customer/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, CustomerDto.class);
    }

    /**
     * Read a Customer
     *
     * @param id unique id of the customer
     * @return response of the requested as customer dto object
     */
    public CustomerDto readCustomer(UUID id) throws Exception {
        var result = mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/customer/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken()))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, CustomerDto.class);
    }

}
