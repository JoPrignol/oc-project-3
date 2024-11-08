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
import com.chatop.webapp.responses.RentalsResponse;
import com.chatop.webapp.responses.SingleRentalResponse;
import com.chatop.webapp.services.DBRentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api")
public class DBRentalController {

  @Autowired
  private DBRentalService DBRentalService;

  @Operation(summary = "List all the rentals",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of rentals",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RentalsResponse.class),
                    examples = @ExampleObject(
                        value = """
                        {
                            "rentals": [
                                {
                                    "id": 0,
                                    "name": "string",
                                    "surface": 0,
                                    "price": 0,
                                    "picture": [
                                      "string"
                                    ],
                                    "description": "string",
                                    "owner_id": 0,
                                    "created_at": "string",
                                    "updated_at": "string"
                                }
                            ]
                        }
                        """
                    )
                )
            )
        }
  )
  @GetMapping(value = "/rentals", produces = "application/json")
  public ResponseEntity<RentalsResponse> findAll() {
    // Création d'une liste de toutes les locations de type RentalsResponse
    Iterable<RentalResponse> rentalResponses = DBRentalService.findAllRentalResponses();
    RentalsResponse response = new RentalsResponse(rentalResponses);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get the rental's informations", security = @SecurityRequirement(name = "bearerAuth"))
  @GetMapping(value = "/rentals/{id}", produces = "application/json")
  public ResponseEntity<SingleRentalResponse> getRentalById(@PathVariable Long id) {
    SingleRentalResponse response = DBRentalService.findRentalResponseById(id);

    if (response == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return ResponseEntity.ok(response);
}


  @Operation(summary = "Create a new rental", security = @SecurityRequirement(name = "bearerAuth"))
  @PostMapping(value = "/rentals", consumes = { "multipart/form-data" })
  public ResponseEntity<MessageResponse> createRental(
    // Passage des paramètres requis pour créer une location
    @RequestPart("name") String name,
    @RequestPart("surface") String surfaceStr,
    @RequestPart("price") String priceStr,
    @RequestPart("description") String description,
    @RequestPart("picture") MultipartFile picture
  ) {
      // Conversion des paramètres en entiers
      int surface = Integer.parseInt(surfaceStr);
      int price = Integer.parseInt(priceStr);

      // Vérification de l'authentification de l'utilisateur
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !authentication.isAuthenticated()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      // Récupération de l'email de l'utilisateur pour l'associer à la location
      String email = authentication.getName();
      try {
        // Création de la location
          DBRentalService.createRental(name, surface, price, description, picture, email);
          return ResponseEntity.ok(new MessageResponse("Rental created !"));
      } catch (IllegalArgumentException e) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (IOException e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
    }

  @Operation(summary = "Modify a rental's informations", security = @SecurityRequirement(name = "bearerAuth"))
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
        SingleRentalResponse updatedRental = DBRentalService.updateRental(id, name, surface, price, description);
        return ResponseEntity.ok(new MessageResponse("Rental updated!"));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

}
