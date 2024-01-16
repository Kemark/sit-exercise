package de.sit.exercise.features.token;

import org.springframework.lang.NonNull;

/**
 * checks the password against an existing customer, defined by the email.
 *
 * If hte password is correct, the token useable as bearer token is returned.
 */
public interface ITokenService {

    String login(@NonNull String email, @NonNull String password);
}
