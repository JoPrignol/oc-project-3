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

import com.chatop.webapp.requests.MessageRequest;
import com.chatop.webapp.responses.MessageResponse;
import com.chatop.webapp.services.DBMessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
public class DBMessageController {

  @Autowired
  private DBMessageService dbMessageService;

  @Operation(summary = "Create a new message", security = @SecurityRequirement(name = "bearerAuth"))
  @PostMapping("/messages")
  public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest messageRequest) {

    // Vérifiez si l'utilisateur est authentifié
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Refuser l'accès si l'utilisateur n'est pas authentifié
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // Vérifiez que tous les paramètres requis sont présents et renvoyez une erreur si ce n'est pas le cas
    if (messageRequest.getMessage() == null || messageRequest.getUser_id() == null || messageRequest.getRental_id() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // Envoi du message
    dbMessageService.createMessage(messageRequest);

    // Réponse renvoyée si le message est créé
    return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Message created successfully."));
}

}
