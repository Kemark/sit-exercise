package de.sit.exercise.features.book;

import java.util.UUID;

import de.sit.exercise.util.dto.IDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Contains all informations about a book
 */
@Data
public class BookDto implements IDTO {
    private UUID id;

    @Size(min = 5, max = 256)
    private String title;

    @Size(min = 5, max = 100)
    private String author;

    @Size(min = 5, max = 100)
    private String publisher;

    private Integer publishingYear;

    /**
     * A category must always be associated to the book
     */
    @NotNull
    private UUID categoryId;
}
