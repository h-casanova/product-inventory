package com.hcasanova.product_inventory.infrastructure.rest.controller;

import com.hcasanova.product_inventory.application.service.TokenService;
import com.hcasanova.product_inventory.domain.model.AppUser;
import com.hcasanova.product_inventory.infrastructure.mapper.AppUserMapper;
import com.hcasanova.product_inventory.infrastructure.persistence.repository.AppUserRepository;
import com.hcasanova.product_inventory.infrastructure.rest.dto.CredentialsDTO;
import com.hcasanova.product_inventory.infrastructure.rest.dto.LoginResponseDTO;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

  @Inject AppUserRepository userRepository;

  @Inject TokenService tokenService;

  @ConfigProperty(name = "APP_URL")
  String appUrl;

  @ConfigProperty(name = "APP_PORT")
  int appPort;

  @POST
  @Path("/login")
  public Response login(CredentialsDTO credentials) {
    System.out.println("|| REST REQUEST || AuthController: login");
    AppUser user = userRepository.findByUsername(credentials.username);

    if (user == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    Argon2 argon2 = Argon2Factory.create();
    boolean matches = argon2.verify(user.getPasswordHash(), credentials.password.toCharArray());

    if (!matches) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    String issuer = appUrl + ":" + appPort;
    String token = tokenService.generateToken(user.getUsername(), List.of(user.getRole()), issuer);

    return Response.ok(new LoginResponseDTO(token, AppUserMapper.toDTO(user))).build();
  }
}
