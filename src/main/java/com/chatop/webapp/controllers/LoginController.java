package com.chatop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.services.JWTService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class LoginController {

  public JWTService jwtService;

  public LoginController(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Operation(summary = "Login to the application")
  @PostMapping("/api/auth/login")
  public String getToken(Authentication authentication){
    String token = jwtService.generateToken(authentication);
    return token;
  }

  @Autowired
  private DBUserRepository dbUserRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Operation(summary = "Register a new user")
  @PostMapping("/api/auth/register")
  public ResponseEntity<String> registerUser(@RequestBody DBUser newUser) {
      if (dbUserRepository.findUserByName(newUser.getName()).isPresent()) {
          return ResponseEntity.status(400).body("This name already exists");
      } else if (dbUserRepository.findByEmail(newUser.getEmail()).isPresent()) {
          return ResponseEntity.status(400).body("This email is already taken");
      }

      // Encoder le mot de passe avant de sauvegarder l'utilisateur
      newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

      // Sauvegarder le nouvel utilisateur
      dbUserRepository.save(newUser);

      return ResponseEntity.ok("User registered successfully");
  }

  // @GetMapping("/api/auth/logout")
  // public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
  //   if (authentication != null) {
  //     new SecurityContextLogoutHandler().logout(request, response, authentication);
  //   }
  //   return "User logged out successfully";
  // }

}
