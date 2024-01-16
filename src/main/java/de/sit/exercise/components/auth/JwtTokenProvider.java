package de.sit.exercise.components.auth;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * Encodes and Decodes JWT Token
 *
 * It's only usabble for testing purposes
 */
@Service
public class JwtTokenProvider {

    private RSAKey rsaJWK;

    /**
     * Generates a bearer token with default values
     *
     * @return bearer token
     */
    public String generateToken(String username) {
        try {
            var rsaPublicJWK = getRSAKey();

            // Create RSA-signer with the private key
            JWSSigner signer = new RSASSASigner(rsaPublicJWK);

            // Prepare JWT with claims set
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("scope", List.of("CUSTOMER"))
                    .issuer("https://sit-example.de")
                    .issueTime(new Date())
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(rsaPublicJWK.getKeyID())
                            .build(),
                    claimsSet);

            // Compute the RSA signature
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AccessDeniedException("Could generate JWT token", e);
        }

    }

    /**
     * Decodes a give bearer token for extracting the authority claims
     *
     * @return Decoder for getting the JWT object
     */
    public JwtDecoder getDecoder() {
        return new JwtDecoder() {

            @Override
            public Jwt decode(String token) throws JwtException {
                try {
                    var signedJWT = SignedJWT.parse(token);
                    var claimsSet = signedJWT.getJWTClaimsSet();

                    return new Jwt(token, claimsSet.getIssueTime().toInstant(),
                            claimsSet.getExpirationTime().toInstant(),
                            signedJWT.getHeader().toJSONObject(), claimsSet.toJSONObject());
                } catch (Exception e) {
                    throw new AccessDeniedException("Could not verify JWT token integrity!", e);
                }
            }

        };
    }

    /**
     * generates the public RSA key, if it not yet generated
     */
    private RSAKey getRSAKey() {
        if (rsaJWK == null) {
            try {
                rsaJWK = new RSAKeyGenerator(2048)
                        .keyID("123")
                        .generate();

            } catch (Exception e) {
                throw new AccessDeniedException("Cannot generate rsa key", e);
            }
        }
        return rsaJWK;
    }
}
