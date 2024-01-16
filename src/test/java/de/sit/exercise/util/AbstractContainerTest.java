package de.sit.exercise.util;

import org.springframework.beans.factory.annotation.Autowired;

import de.sit.exercise.components.auth.JwtTokenProvider;

/**
 * Supports unique TestContainer Connection to the msql server
 */
public abstract class AbstractContainerTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * used for getting the bearerToken for authentication
     */
    private String bearerToken;

    protected String getBearerToken() {
        if (bearerToken == null) {
            bearerToken = "Bearer " + tokenProvider.generateToken("password");
        }
        return bearerToken;
    }
}
