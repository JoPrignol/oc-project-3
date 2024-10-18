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

import com.chatop.webapp.model.DBUser;
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
  public Iterable<DBUser> getAllUsers() {
    return DBUserService.findAll();
  }

  @Operation(summary = "Get the user's informations")
  @GetMapping(value = "/user/{id}", produces = "application/json")
  public ResponseEntity<UserRequest> getUserById(@PathVariable Long id) {
    DBUser user = DBUserService.findById(id);

    if (user == null) {
      // Retourner une réponse 404 si l'utilisateur n'est pas trouvé
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Mapper les données de DBUser à UserRequest
    UserRequest userRequest = new UserRequest();
    userRequest.setName(user.getName());
    userRequest.setId(user.getId().toString());
    userRequest.setEmail(user.getEmail());
    userRequest.setCreated_at(user.getCreated_at().toString()); // Adapter le format si nécessaire
    userRequest.setUpdated_at(user.getUpdated_at().toString()); // Adapter le format si nécessaire

    // Retourner le DTO UserRequest
    return ResponseEntity.ok(userRequest);
  }

  @Operation(summary = "Get the current user's informations")
  @GetMapping("/auth/me")
    public ResponseEntity<DBUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Authentication: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        System.out.println("USER IS AUTHENTICATED");

        // Récupérer le nom de l'utilisateur
        String email = authentication.getName();

        System.out.println("NAME GOTTEN ==>" + email);

        // Obtenir les détails de l'utilisateur
        DBUser user = DBUserService.findByEmail(email);
        System.out.println("USER RETURNED ; USER = " + DBUserService.findByEmail(email));
        if (user == null) {
            return ResponseEntity.notFound().build(); // Not Found
        }

        return ResponseEntity.ok(user);

    }
}
