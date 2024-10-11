package com.chatop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.model.DBRental;
import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.services.DBRentalService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api")
public class DBRentalController {

  @Autowired
  private DBRentalService DBRentalService;
  @Autowired
  private DBUserRepository dbUserRepository;

  @Operation(summary = "List all the rentals")
  @GetMapping("/rentals")
  public Iterable<DBRental> findAll() {
      return DBRentalService.findAll();
  }

  @Operation(summary = "Get the rental's informations")
  @GetMapping("/rentals/{id}")
  public DBRental getRentalById(@PathVariable Long id) {
    return DBRentalService.findById(id);
  }

  @Operation(summary = "Create a new rental")
  @PostMapping("/rentals")
  public ResponseEntity<DBRental> createRental(@RequestBody DBRental rental) {

      // Obtenir l'ID de l'utilisateur connecté
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      DBUser user = dbUserRepository.findByName(username);
      if (user != null) {
          Long owner_id = user.getId();
          rental.setOwner_id(owner_id);
      } else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    DBRental createdRental = DBRentalService.save(rental);
    return ResponseEntity.ok(createdRental);
  }

  @Operation(summary = "Modify a rental's informations")
  @PutMapping("/rentals/{id}")
  public ResponseEntity<DBRental> updateRental(@PathVariable Long id, @RequestBody DBRental rental) {

    DBRental selectedRental = DBRentalService.findById(id);
    if (selectedRental == null) {
        return ResponseEntity.notFound().build();
    }

    // Mise à jour des champs
    selectedRental.setName(rental.getName());
    selectedRental.setSurface(rental.getSurface());
    selectedRental.setPrice(rental.getPrice());
    selectedRental.setDescription(rental.getDescription());
    selectedRental.setPicture(rental.getPicture()); // Assurez-vous que le nom du champ correspond à votre modèle

    // Sauvegarde
    DBRental updatedRental = DBRentalService.save(selectedRental);
    return ResponseEntity.ok(updatedRental);
  }
}
