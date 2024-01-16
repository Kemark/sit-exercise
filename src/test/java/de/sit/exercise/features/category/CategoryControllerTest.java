package de.sit.exercise.features.category;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
import de.sit.exercise.util.EntityGenerationHelper;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

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
        entityGenerator.addCategory(category);

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
        entityGenerator.addCategory(category);

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
        entityGenerator.addCategory(category);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Create")
    void testReadCategoryAfterCreate() throws Exception {
        var category = Instancio.create(CategoryDto.class);

        entityGenerator.addCategory(category);
        var addedCategory = entityGenerator.readCategory(category.getId());
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
            entityGenerator.addCategory(category);

            // read the added category
            var addedCategory = entityGenerator.readCategory(category.getId());

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
        entityGenerator.addCategory(category);

        // then update the category
        var categoryToUpdate = Instancio.create(CategoryDto.class);
        categoryToUpdate.setId(category.getId());
        entityGenerator.updateCategory(category.getId(), categoryToUpdate);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Update")
    void testReadCategoryAfterUpdate() throws Exception {
        var category = Instancio.create(CategoryDto.class);

        // add a new category
        entityGenerator.addCategory(category);

        // then update the category
        var categoryToUpdate = Instancio.create(CategoryDto.class);
        categoryToUpdate.setId(category.getId());
        entityGenerator.updateCategory(category.getId(), categoryToUpdate);

        // then update the category
        var updateCategory = entityGenerator.readCategory(category.getId());

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
        entityGenerator.addCategory(category);

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
        entityGenerator.addCategory(category);

        var secondCategory = Instancio.create(CategoryDto.class);
        secondCategory.setName(category.getName());
        var secondPayload = objectMapper.writeValueAsString(secondCategory);

        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", entityGenerator.getBearerToken())
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
        entityGenerator.addCategory(category);

        // create 20 books
        var books = Instancio.ofList(BookDto.class).size(bookCount).create();
        for (var book : books) {
            // add a book
            book.setCategoryId(category.getId());
            entityGenerator.addBook(book);
        }

        // read the book count
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/category/" + category.getId() + "/bookCount")
                        .header("Authorization", entityGenerator.getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(bookCount)));
    }
}
