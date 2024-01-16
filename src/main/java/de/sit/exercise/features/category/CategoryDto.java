package de.sit.exercise.features.category;

import java.util.UUID;

import de.sit.exercise.util.dto.IDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Contains all informations about a category
 */
@Data
public class CategoryDto implements IDTO {
    private UUID id;

    @Size(min = 5, max = 50)
    private String name;

    @Size(max = 512)
    private String description;
}
