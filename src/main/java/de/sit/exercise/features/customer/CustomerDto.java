package de.sit.exercise.features.customer;

import java.util.UUID;

import de.sit.exercise.util.dto.IDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Contains all informations about a category
 */
@Data
public class CustomerDto implements IDTO {
    private UUID id;

    @Size(min = 5, max = 100)
    private String name;

    @Size(min = 5, max = 20)
    private String password;

    @Email
    @NotEmpty
    private String email;
}
