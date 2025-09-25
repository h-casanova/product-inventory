package com.hcasanova.product_inventory.infrastructure.security;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class HybridSecurity implements SecurityIdentityAugmentor {

  private static final String CLIENT_ID = "product-app"; // tu clientId en Keycloak
  private static final String LOCAL_ISSUER = "http://localhost:8085"; // issuer de tu JWT local

  @Override
  public Uni<SecurityIdentity> augment(
      SecurityIdentity identity, AuthenticationRequestContext context) {

    Set<String> roles = new HashSet<>();

    if (identity.getPrincipal() instanceof JWTCallerPrincipal principal) {
      // Diferenciar si es tu JWT local por el issuer
      if (LOCAL_ISSUER.equals(principal.getIssuer())) {
        // Token local: roles vienen de claim "groups"
        Object groups = principal.getClaim("groups");
        if (groups instanceof Collection<?> c) {
          roles.addAll(c.stream().map(Object::toString).collect(Collectors.toSet()));
          System.out.println("LOCAL JWT detected, roles: " + roles);
        }
      } else {
        // Token Keycloak: OIDC ya lo validó, roles vienen de realm_access y
        // resource_access
        Object realmAccess = identity.getAttribute("realm_access");
        if (realmAccess instanceof Map<?, ?> map) {
          Object realmRoles = map.get("roles");
          if (realmRoles instanceof Collection<?> c) {
            roles.addAll(c.stream().map(Object::toString).collect(Collectors.toSet()));
          }
        }

        Object resourceAccess = identity.getAttribute("resource_access");
        if (resourceAccess instanceof Map<?, ?> map) {
          Object client = map.get(CLIENT_ID);
          if (client instanceof Map<?, ?> clientMap) {
            Object clientRoles = clientMap.get("roles");
            if (clientRoles instanceof Collection<?> c) {
              roles.addAll(c.stream().map(Object::toString).collect(Collectors.toSet()));
            }
          }
        }

        System.out.println("Keycloak token detected, roles: " + roles);
      }
    }

    return Uni.createFrom().item(QuarkusSecurityIdentity.builder(identity).addRoles(roles).build());
  }
}
