// AuthService.java
package com.chatop.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.chatop.webapp.exception.BadCredentialsException;
import com.chatop.webapp.responses.LoginResponse;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JWTService jwtService;

  public LoginResponse login(String email, String password) {
    try {
      // Effectuer l'authentification
      Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password));

      User authenticatedUser = (User) authentication.getPrincipal();

      // Générer le token JWT
      String token = jwtService.generateToken(authenticatedUser);

      return new LoginResponse(token);
    } catch (Exception ex) {
      throw new BadCredentialsException("Invalid credentials provided.");
    }
  }
}
