package com.hcasanova.product_inventory.application.service;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class TokenService {

  public String generateToken(String username, List<String> roles, String issuer) {
    Set<String> rolesSet = new HashSet<>(roles);

    JwtClaimsBuilder claims =
        Jwt.claims()
            .subject(username) // "sub"
            .upn(username) // "upn"
            .groups(rolesSet) // "groups"
            .issuer(issuer) // "iss"
            .issuedAt(Instant.now()) // "iat"
            .expiresAt(Instant.now().plus(2, ChronoUnit.HOURS)); // "exp"

    return claims.sign();
  }
}
