package com.chatop.webapp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.services.JWTService;

@RestController
public class LoginController {

  public JWTService jwtService;

  public LoginController(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @PostMapping("/auth/login")
  public String getToken(Authentication authentication){
    String token = jwtService.generateToken(authentication);
    return token;
  }

  // Pour les tests de connexion
  @GetMapping("/user")
  public String getUser(){
    return "Hello, User";
  }

  // Pour les tests de connexion
  @GetMapping("/")
  public String getResource(){
    return "Login Test Successful";
  }

}
