package de.sit.exercise.features.token.impl;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sit.exercise.features.token.ITokenService;
import de.sit.exercise.features.token.Credential;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TokenController {

    private final ITokenService service;

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public final ResponseEntity<String> create(
            final @RequestBody @Validated @NonNull Credential credential) {
        var token = service.login(credential.getEmail(), credential.getPassword());
        return ResponseEntity.ok(token);
    }
}
