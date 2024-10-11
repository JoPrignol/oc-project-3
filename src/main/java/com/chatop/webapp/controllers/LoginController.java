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

@RestController
public class LoginController {

  public JWTService jwtService;

  public LoginController(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @PostMapping("/api/auth/login")
  public String getToken(Authentication authentication){
    String token = jwtService.generateToken(authentication);
    return token;
  }

  @Autowired
  private DBUserRepository dbUserRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @PostMapping("/api/auth/register")
  public ResponseEntity<String> registerUser(@RequestBody DBUser newUser) {
      // Vérifier si l'utilisateur existe déjà par nom
      if (dbUserRepository.findUserByName(newUser.getName()).isPresent()) {
          return ResponseEntity.status(400).body("Error: Name already exists");
      }

      // Vérifier si l'utilisateur existe déjà par email
      if (dbUserRepository.findByEmail(newUser.getEmail()).isPresent()) {
          return ResponseEntity.status(400).body("Error: Email already exists");
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


  // // Pour les tests de connexion
  // @GetMapping("/api/user")
  // public String getUser(){
  //   return "Hello, User";
  // }

  // // Pour les tests de connexion
  // @GetMapping("/")
  // public String getResource(){
  //   return "Login Test Successful";
  // }

}
