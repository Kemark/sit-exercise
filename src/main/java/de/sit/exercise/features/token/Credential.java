package de.sit.exercise.features.token;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * It's needed for the login auth flow
 */
@Data
public class Credential {
    @NotEmpty
    @NonNull
    private String password;

    @NotEmpty
    @NonNull
    private String email;
}
