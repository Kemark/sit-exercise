package de.sit.exercise.features.token.impl;

import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.sit.exercise.components.auth.JwtTokenProvider;
import de.sit.exercise.features.customer.ICustomerService;
import de.sit.exercise.features.token.ITokenService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final ICustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    /**
     * checks the password against an existing customer, defined by the email.
     *
     * If hte password is correct, the token useable as bearer token is returned.
     */
    @Override
    public String login(@NonNull String email, @NonNull String password) {

        // for the first access (no customer exisst), the token is generated withoud emaail and password checking
        if (getCustomerService().isAnyCustomerExist()) {
            var customer = getCustomerService().findByEmail(email);

            if (!getPasswordEncoder().matches(password, customer.getPassword())) {
                throw new AccessDeniedException("password is not correct");
            }
        }

        return getTokenProvider().generateToken(password);
    }

}
