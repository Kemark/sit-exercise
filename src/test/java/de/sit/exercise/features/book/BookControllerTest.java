package de.sit.exercise.features.book;

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
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

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
    @DisplayName("Only an authenticated user can create books")
    void testCreatebookWithoutCredential() throws Exception {
        var book = Instancio.create(BookDto.class);
        String payload = objectMapper.writeValueAsString(book);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can update books")
    void testUpdateBookWithoutCredential() throws Exception {

        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // first a new book
        entityGenerator.addbook(book);

        // then update the book
        var bookToUpdate = Instancio.create(BookDto.class);
        bookToUpdate.setId(book.getId());
        String payload = objectMapper.writeValueAsString(bookToUpdate);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .put("/api/book/" + bookToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Only an authenticated user can delete books")
    void testDeleteBookWithoutCredential() throws Exception {

        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // first a new book
        entityGenerator.addbook(book);

        // then update the book
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .delete("/api/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     *
     */
    @Test
    @DisplayName("Create a book")
    void testAddBook() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        entityGenerator.addbook(book);
    }

    /**
     *
     */
    @Test
    @DisplayName("Category must always be defined")
    void testCategoryMustBeDefined() throws Exception {
        var book = Instancio.create(BookDto.class);

        String payload = objectMapper.writeValueAsString(book);
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", entityGenerator.getBearerToken())
                        .content(payload))
                .andExpect(status().isConflict());
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Create")
    void testReadBookAfterCreate() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        entityGenerator.addbook(book);
        var addedbook = entityGenerator.readBook(book.getId());
        Assertions.assertEquals(book, addedbook);
    }

    /**
     *
     */
    @Test
    @DisplayName("Create multiple books")
    void testAddbooks() throws Exception {
        var books = Instancio.ofList(BookDto.class).size(20).create();

        for (var book : books) {
            // add a book
            book.setCategoryId(entityGenerator.getCatetoryId());
            entityGenerator.addbook(book);

            // read the added book
            var addedbook = entityGenerator.readBook(book.getId());

            // check each created book
            Assertions.assertEquals(book, addedbook);
        }
        Assertions.assertTrue(repository.findAll().size() >= books.size());
    }

    /**
     *
     */
    @Test
    @DisplayName("Update a book")
    void testUpdateBooks() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // first a new book
        entityGenerator.addbook(book);

        // then update the book
        var bookToUpdate = Instancio.create(BookDto.class);
        bookToUpdate.setId(book.getId());
        bookToUpdate.setCategoryId(book.getCategoryId());
        entityGenerator.updateBook(book.getId(), bookToUpdate);
    }

    /**
     *
     */
    @Test
    @DisplayName("Read after Update")
    void testReadbookAfterUpdate() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // add a new book
        entityGenerator.addbook(book);

        // then update the book
        var bookToUpdate = Instancio.create(BookDto.class);
        bookToUpdate.setId(book.getId());
        bookToUpdate.setCategoryId(book.getCategoryId());
        entityGenerator.updateBook(book.getId(), bookToUpdate);

        // then update the book
        var updatebook = entityGenerator.readBook(book.getId());

        Assertions.assertEquals(bookToUpdate, updatebook);
    }

    /**
     *
     */
    @Test
    @DisplayName("Update Category")
    void testUpdateBookCategory() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // first a new book
        entityGenerator.addbook(book);

        // then update the book
        var bookToUpdate = Instancio.create(BookDto.class);
        bookToUpdate.setId(book.getId());
        bookToUpdate.setCategoryId(entityGenerator.getCatetoryId());

        entityGenerator.updateBook(book.getId(), bookToUpdate);

        var updateCategory = entityGenerator.readBook(book.getId());
        Assertions.assertEquals(bookToUpdate.getCategoryId(), updateCategory.getCategoryId());
    }

    /**
     *
     */
    @Test
    @DisplayName("Even unauthenticated user can read books")
    void testReadbookWithoutCredential() throws Exception {

        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // first a new book
        entityGenerator.addbook(book);

        // then update the book
        mockMvc.perform(
                MockMvcRequestBuilders //
                        .get("/api/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     *
     */
    @Test
    @DisplayName("Title together with Author must be unique")
    void testTitleAndAuthorMustBeUnique() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // add a new book
        entityGenerator.addbook(book);

        // use the same title and author for the seocnd book
        var secondbook = Instancio.create(BookDto.class);
        secondbook.setAuthor(book.getAuthor());
        secondbook.setTitle(book.getTitle());
        var secondPayload = objectMapper.writeValueAsString(secondbook);

        mockMvc.perform(
                MockMvcRequestBuilders //
                        .post("/api/book")
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
    @DisplayName("Title must not be unique")
    void testTitleMustNotBeUniqe() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // add a new book
        entityGenerator.addbook(book);

        // use the same title for the second book
        var secondbook = Instancio.create(BookDto.class);
        secondbook.setTitle(book.getTitle());
        secondbook.setCategoryId(book.getCategoryId());

        entityGenerator.addbook(secondbook);
    }

    /**
     *
     */
    @Test
    @DisplayName("Author must not be unique")
    void testAuthroMustNotBeUniqe() throws Exception {
        var book = Instancio.create(BookDto.class);
        book.setCategoryId(entityGenerator.getCatetoryId());

        // add a new book
        entityGenerator.addbook(book);

        // use the same title for the second book
        var secondbook = Instancio.create(BookDto.class);
        secondbook.setAuthor(book.getAuthor());
        secondbook.setCategoryId(book.getCategoryId());

        entityGenerator.addbook(secondbook);
    }

}
