package com.chatop.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.requests.LoginRequest;
import com.chatop.webapp.requests.RegisterRequest;
import com.chatop.webapp.responses.LoginResponse;
import com.chatop.webapp.services.AuthService;
import com.chatop.webapp.services.DBUserService;
import com.chatop.webapp.services.JWTService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
public class LoginController {

  @Autowired
  private DBUserRepository dbUserRepository;
  @Autowired
  private DBUserService dbUserService;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  public JWTService jwtService;
  @Autowired
  public AuthService authService;

  private final AuthenticationManager authenticationManager;
  public Logger logger = LoggerFactory.getLogger(LoginController.class);

  public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Operation(summary = "Login to the application")
  @PostMapping("/api/auth/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
    try {
        // Appeler le service d'authentification pour obtenir le token
        LoginResponse loginResponse = authService.login(loginRequest.getLogin(), loginRequest.getPassword());

        logger.info("Tentative de connexion avec l'email : {}", loginRequest.getLogin());
        logger.info("Token généré : {}", loginResponse.getToken());

        return ResponseEntity.ok(loginResponse);
    } catch (BadCredentialsException ex) {
        logger.error("Login échoué en raison de : ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception ex) {
        logger.error("Erreur interne lors de l'authentification : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

  @Operation(summary = "Register a new user")
  @PostMapping("/api/auth/register")
  public ResponseEntity<String> registerUser(@RequestBody RegisterRequest newUser) {
      if (dbUserRepository.findUserByName(newUser.getName()).isPresent()) {
          return ResponseEntity.status(400).body("This name already exists");
      } else if (dbUserRepository.findByEmail(newUser.getEmail()).isPresent()) {
          return ResponseEntity.status(400).body("This email is already taken");
      }

      // Encoder le mot de passe avant de sauvegarder l'utilisateur
      newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

      // Sauvegarder le nouvel utilisateur
      // appelle le service et je construis un nouvel user sur la base de register request
      dbUserService.createUserFromRegisterRequest(newUser);

      return ResponseEntity.ok().build();
  }
}
