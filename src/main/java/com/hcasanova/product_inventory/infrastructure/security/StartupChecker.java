package com.hcasanova.product_inventory.infrastructure.security;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@ApplicationScoped
public class StartupChecker {

  void onStart(@Observes StartupEvent ev) {
    String pathPublic = "src/main/resources/security/publicKey.pem";
    File publicKey = new File(pathPublic);
    if (!publicKey.exists()) {
      System.err.println("\n**************************************************");
      System.err.println("ERROR: publicKey.pem not found in src/main/resources/security/");
      System.err.println("Please create it in: " + publicKey.getAbsolutePath());
      System.err.println("App cannot run without this file.");
      System.err.println("**************************************************\n");
      throw new RuntimeException("File publicKey.pem not found");
    }

    try {
      List<String> lines = Files.readAllLines(publicKey.toPath());
      boolean hasBegin = lines.stream().anyMatch(l -> l.contains("-----BEGIN PUBLIC KEY-----"));
      boolean hasEnd = lines.stream().anyMatch(l -> l.contains("-----END PUBLIC KEY-----"));
      if (!hasBegin || !hasEnd) {
        throw new RuntimeException("publicKey.pem invalid: it doesn't contain a public valid key");
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading publicKey.pem", e);
    }

    String pathPrivate = "src/main/resources/security/privateKey.pem";
    File privateKey = new File(pathPrivate);
    if (!privateKey.exists()) {
      System.err.println("\n**************************************************");
      System.err.println("ERROR: privateKey.pem not found in src/main/resources/security/");
      System.err.println("Please create it in: " + privateKey.getAbsolutePath());
      System.err.println("App cannot run without this file.");
      System.err.println("**************************************************\n");
      throw new RuntimeException("File privateKey.pem not found");
    }

    try {
      List<String> lines = Files.readAllLines(privateKey.toPath());
      boolean hasBegin = lines.stream().anyMatch(l -> l.contains("-----BEGIN PRIVATE KEY-----"));
      boolean hasEnd = lines.stream().anyMatch(l -> l.contains("-----END PRIVATE KEY-----"));
      if (!hasBegin || !hasEnd) {
        throw new RuntimeException(
            "privateKey.pem invalid: it doesn't contain a private valid key");
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading privateKey.pem", e);
    }
  }
}
