package com.chatop.webapp.controllers;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.model.DBUser;
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
      LoginResponse loginResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

      return ResponseEntity.ok(loginResponse);

    }catch (BadCredentialsException ex) {

      throw new BadCredentialsException("Invalid credentials provided.");

    }
}

  @Operation(summary = "Register a new user")
  @PostMapping("/api/auth/register")
  public ResponseEntity<LoginResponse> registerUser(@RequestBody RegisterRequest newUser) {

      // Encoder le mot de passe avant de sauvegarder l'utilisateur pour s'assurer de la sécurité de la BDD
      newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

      // Sauvegarder le nouvel utilisateur :
      // appelle le service et construit un nouvel user sur la base de register request
      dbUserService.createUserFromRegisterRequest(newUser);

      try {
        DBUser dbUser = dbUserService.findUserByEmail(newUser.getEmail());

        // Convertir DBUser en User générer le token à renvoyer
        User userDetails = new User(dbUser.getEmail(), dbUser.getPassword(), Collections.emptyList());

        // Générer le token (cela permet de le renvoyer afin de connecter l'utilisateur directement après son inscription)
        String token = jwtService.generateToken(userDetails);

        // Envoi du token JWT
        LoginResponse loginResponse = new LoginResponse(token);

        return ResponseEntity.ok(loginResponse);
    } catch (Exception ex) {
        logger.error("Error :", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
