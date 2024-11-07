package com.chatop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.requests.UserRequest;
import com.chatop.webapp.services.DBUserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class DBUserController {

  @Autowired
  private DBUserService DBUserService;

  @Operation(summary = "List all the users")
  @GetMapping("/users")
  public ResponseEntity<Iterable<UserRequest>> getAllUsers() {
    Iterable<UserRequest> users = DBUserService.findAll();
    return ResponseEntity.ok(users);
  }

  @Operation(summary = "Get the user's informations")
  @GetMapping(value = "/user/{id}", produces = "application/json")
  public ResponseEntity<UserRequest> getUserById(@PathVariable Long id) {

    // Récupération des informations de l'utilisateur
    UserRequest userRequest = DBUserService.getUserById(id);

    if (userRequest == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return ResponseEntity.ok(userRequest);
  }

  @Operation(summary = "Get the current user's informations")
  @GetMapping("/auth/me")
  public ResponseEntity<UserRequest> getCurrentUser() {

    // Récupération de l'utilisateur actuellement connecté
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(401).build();
    }

    String email = authentication.getName();

    UserRequest user = DBUserService.getCurrentUserByEmail(email);

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(user);
  }
}
