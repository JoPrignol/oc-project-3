package com.chatop.webapp.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.webapp.responses.MessageResponse;
import com.chatop.webapp.responses.RentalResponse;
import com.chatop.webapp.services.DBRentalService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api")
public class DBRentalController {

  @Autowired
  private DBRentalService DBRentalService;

  @Operation(summary = "List all the rentals")
  @GetMapping(value = "/rentals", produces = "application/json")
  public ResponseEntity<Iterable<RentalResponse>> findAll() {
    Iterable<RentalResponse> rentalResponses = DBRentalService.findAllRentalResponses();
    return ResponseEntity.ok(rentalResponses);
  }

  @Operation(summary = "Get the rental's informations")
  @GetMapping(value = "/rentals/{id}", produces = "application/json")
  public ResponseEntity<RentalResponse> getRentalById(@PathVariable Long id) {
    RentalResponse response =  DBRentalService.findRentalResponseById(id);

    if (response == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return ResponseEntity.ok(response);
  }


  @Operation(summary = "Create a new rental")
  @PostMapping(value = "/rentals", consumes = { "multipart/form-data" })
  public ResponseEntity<MessageResponse> createRental(
    @RequestPart("name") String name,
    @RequestPart("surface") String surfaceStr,
    @RequestPart("price") String priceStr,
    @RequestPart("description") String description,
    @RequestPart("picture") MultipartFile picture
  ) {

    int surface = Integer.parseInt(surfaceStr);
    int price = Integer.parseInt(priceStr);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String email = authentication.getName();
    try {
        DBRentalService.createRental(name, surface, price, description, picture, email);
        return ResponseEntity.ok(new MessageResponse("Rental created !"));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


  @Operation(summary = "Modify a rental's informations")
  @PutMapping("/rentals/{id}")
  public ResponseEntity<MessageResponse> updateRental(
    @PathVariable Long id,
    @RequestPart("name") String name,
    @RequestPart("surface") String surfaceStr,
    @RequestPart("price") String priceStr,
    @RequestPart("description") String description) {

    int surface = Integer.parseInt(surfaceStr);
    int price = Integer.parseInt(priceStr);

    try {
        DBRentalService.updateRental(id, name, surface, price, description);
        return ResponseEntity.ok(new MessageResponse("Rental updated !"));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }
}
