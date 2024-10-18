package com.chatop.webapp.controllers;

import java.io.IOException;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.webapp.model.DBRental;
import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.services.DBRentalService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api")
public class DBRentalController {

  @Autowired
  private DBRentalService DBRentalService;
  @Autowired
  private DBUserRepository dbUserRepository;
  @Autowired
  private Cloudinary cloudinary;

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
  @PostMapping(value = "/rentals", consumes = { "multipart/form-data" })
  public ResponseEntity<DBRental> createRental(
    @RequestPart("name") String name,
    @RequestPart("surface") int surface,
    @RequestPart("price") int price,
    @RequestPart("description") String description,
    @RequestPart("picture") MultipartFile picture
  ) {

      // Obtenir l'ID de l'utilisateur connecté
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      DBUser user = dbUserRepository.findByName(username);
      if (user != null) {
        Long owner_id = user.getId();
        DBRental rental = new DBRental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner_id(owner_id);

        try {
            if (!picture.isEmpty()) {
                // Utilisation de Cloudinary pour l'upload du fichier
                byte[] imageBytes = picture.getBytes();
                Map<?, ?> uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());
                String url = (String) uploadResult.get("url");
                rental.setPicture(url);

            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Renvoyer le message et pas l'objet créé
        DBRental createdRental = DBRentalService.save(rental);
        return ResponseEntity.ok(createdRental);

      } else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
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
