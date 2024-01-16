package de.sit.exercise.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import de.sit.exercise.components.auth.JwtTokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@Getter
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    /**
     * Used by the Spring Framework to configure the authentication mechanism
     *
     * @return configured filter chain for using OAuth2
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/category", "/api/category/**",
                                    "/api/book/**")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                            .requestMatchers("/api/**").hasAuthority("SCOPE_CUSTOMER");
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));

        return http.build();
    }

    /**
     * It's needed for OAuth2 claims authorrity resolving
     *
     * @return Decoder Implementation
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return getTokenProvider().getDecoder();
    }

    /**
     * Provides encoder for password encoding, when password is stored in database
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
