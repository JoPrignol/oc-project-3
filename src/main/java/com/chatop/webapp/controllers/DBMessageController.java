package com.chatop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.model.DBMessage;
import com.chatop.webapp.services.DBMessageService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class DBMessageController {

  @Autowired
  private DBMessageService dbMessageService;

  @Operation(summary = "Create a new message")
  @PostMapping("/messages")
  public ResponseEntity<String> createMessage(@RequestBody DBMessage message) {
    // Vérifiez si l'utilisateur est authentifié
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
    }

    // Vérifiez que les paramètres requis ne sont pas nuls
    if (message.getMessage() == null || message.getUser_id() == null || message.getRental_id() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields are required.");
    }

    // Si tout est valide, sauvegardez le message
    dbMessageService.createMessage(message);
    return ResponseEntity.status(HttpStatus.CREATED).body("Message created successfully.");
}

}
