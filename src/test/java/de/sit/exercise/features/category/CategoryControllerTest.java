package de.sit.exercise.features.category;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.UUID;

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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sit.exercise.features.book.BookDto;
import de.sit.exercise.util.AbstractContainerTest;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class CategoryControllerTest extends AbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("Only an authenticated user can create categories")
    void testCreateCategoryWithoutCredential() throws Exception {
        var category = Instancio.create(CategoryDto.class);
        String payload = objectMapper.writeValueAsString(category);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can update categories")
    void testUpdateCategoryWithoutCredential() throws Exception {

        var category = Instancio.create(CategoryDto.class);

        // first a new category
        addCategory(category);

        // then update the category
        var categoryToUpdate = Instancio.create(CategoryDto.class);
        categoryToUpdate.setId(category.getId());
        String payload = objectMapper.writeValueAsString(categoryToUpdate);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .put("/api/category/" + categoryToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can delete categories")
    void testDeleteCategoryWithoutCredential() throws Exception {

        var category = Instancio.create(CategoryDto.class);

        // first a new category
        addCategory(category);

        // then update the category
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .delete("/api/category/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Create a Category")
    void testAddCategory() throws Exception {
        var category = Instancio.create(CategoryDto.class);
        addCategory(category);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Create")
    void testReadCategoryAfterCreate() throws Exception {
        var category = Instancio.create(CategoryDto.class);

        addCategory(category);
        var addedCategory = readCategory(category.getId());
        Assertions.assertEquals(category, addedCategory);
    }

    /**
     *
     */
    @Test
    @DisplayName("Create multiple Categories")
    void testAddCategories() throws Exception {
        var categories = Instancio.ofList(CategoryDto.class).size(20).create();

        for (var category : categories) {
            // add a category
            addCategory(category);

            // read the added category
            var addedCategory = readCategory(category.getId());

            // check each created category
            Assertions.assertEquals(category, addedCategory);
        }
        Assertions.assertTrue(repository.findAll().size() >= categories.size());
    }

    /**
     *
     */
    @Test
    @DisplayName("Update a Category")
    void testUpdateCategories() throws Exception {
        var category = Instancio.create(CategoryDto.class);

        // first a new category
        addCategory(category);

        // then update the category
        var categoryToUpdate = Instancio.create(CategoryDto.class);
        categoryToUpdate.setId(category.getId());
        updateCategory(category.getId(), categoryToUpdate);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Update")
    void testReadCategoryAfterUpdate() throws Exception {
        var category = Instancio.create(CategoryDto.class);

        // add a new category
        addCategory(category);

        // then update the category
        var categoryToUpdate = Instancio.create(CategoryDto.class);
        categoryToUpdate.setId(category.getId());
        updateCategory(category.getId(), categoryToUpdate);

        // then update the category
        var updateCategory = readCategory(category.getId());

        Assertions.assertEquals(categoryToUpdate, updateCategory);
    }

    /**
     *
     */
    @Test
    @DisplayName("Even unauthenticated user can read categories")
    void testReadCategoryWithoutCredential() throws Exception {

        var category = Instancio.create(CategoryDto.class);

        // first a new category
        addCategory(category);

        // then update the category
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/category/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     *
     */
    @Test
    @DisplayName("Name must be unique")
    void testNameMustBeUnique() throws Exception {
        var category = Instancio.create(CategoryDto.class);

        // add a new category
        addCategory(category);

        var secondCategory = Instancio.create(CategoryDto.class);
        secondCategory.setName(category.getName());
        var secondPayload = objectMapper.writeValueAsString(secondCategory);

        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", getBearerToken())
                        .content(secondPayload))
                .andExpect(status().isConflict());
    }

    /**
     *
     */
    @Test
    @DisplayName("Test count of assigned books")
    void testCountOfAssignedBooks() throws Exception {
        var category = Instancio.create(CategoryDto.class);
        var bookCount = 20;

        // add a new category
        addCategory(category);

        // create 20 books
        var books = Instancio.ofList(BookDto.class).size(bookCount).create();
        for (var book : books) {
            // add a book
            book.setCategoryId(category.getId());
            addBook(book);
        }

        // read the book count
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/category/" + category.getId() + "/bookCount")
                        .header("Authorization", getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(bookCount)));
    }

    /**
     * Add a Category
     *
     * @param category dto of the payload
     * @return response of the request as category dto object
     */
    private CategoryDto addCategory(CategoryDto category) throws Exception {
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
     * Updates a Category
     *
     * @param id                 unique id of the existing Category
     * @param updatedCategoryDto updated Category
     * @return response of the updated as category dto object
     */
    private CategoryDto updateCategory(UUID id, CategoryDto updatedCategoryDto) throws Exception {
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
    private CategoryDto readCategory(UUID id) throws Exception {
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
    private BookDto addBook(BookDto book) throws Exception {
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

}
