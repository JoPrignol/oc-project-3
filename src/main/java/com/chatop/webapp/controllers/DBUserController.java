package com.chatop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.model.DBUser;
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
  @GetMapping("/user/{id}")
  public DBUser getUserById(@PathVariable Long id) {
    return DBUserService.findById(id);
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
        String name = authentication.getName();

        System.out.println("NAME GOTTEN ==>" + name);

        // Obtenir les détails de l'utilisateur
        DBUser user = DBUserService.findUserByName(name);
        System.out.println("USER RETURNED ; USER = " + DBUserService.findUserByName(name));
        if (user == null) {
            return ResponseEntity.notFound().build(); // Not Found
        }

        return ResponseEntity.ok(user);

    }
}
