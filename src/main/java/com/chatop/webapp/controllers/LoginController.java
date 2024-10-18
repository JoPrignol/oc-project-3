package com.chatop.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.requests.LoginRequest;
import com.chatop.webapp.requests.RegisterRequest;
import com.chatop.webapp.services.DBUserService;
import com.chatop.webapp.services.JWTService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class LoginController {

  @Autowired
  private DBUserRepository dbUserRepository;
  @Autowired
  private DBUserService dbUserService;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public JWTService jwtService;
  private final AuthenticationManager authenticationManager;
  public Logger logger = LoggerFactory.getLogger(LoginController.class);

  public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Operation(summary = "Login to the application")
  @PostMapping("/api/auth/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
    try {
      Authentication authenticate = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

      User authenticatedUser = (User) authenticate.getPrincipal();

      String token = jwtService.generateToken(authenticatedUser);
      logger.info("Token is : " + token);

      logger.info("Tentative de connexion avec l'email : {}", loginRequest.getLogin());

      return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token)
      .body("User logged in successfully");
    } catch(BadCredentialsException ex) {
        logger.error("Login failed due to: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (InternalAuthenticationServiceException ex) {
        logger.error("Internal authentication service exception: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
  // // authentication disparait et on prend username et password
  // public String getToken(Authentication authentication){
  //   // authentifier username et password (authencation manager permet d'appeler authenticate )
  //   // Si authentication generation de token
  //   String token = jwtService.generateToken(authentication);

  //   return token;
  // }

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

      return ResponseEntity.ok("User registered successfully");
  }

  @GetMapping("/api/auth/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return "User logged out successfully";
  }

}
