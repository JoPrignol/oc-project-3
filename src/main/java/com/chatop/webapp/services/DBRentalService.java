package com.chatop.webapp.services;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.webapp.model.DBRental;
import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBRentalRepository;
import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.responses.RentalResponse;
import com.chatop.webapp.responses.SingleRentalResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.Data;

@Data
@Service
public class DBRentalService {

  @Autowired
  private DBRentalRepository DBRentalRepository;

  @Autowired
  private DBUserRepository DBUserRepository;

  @Autowired
  private Cloudinary cloudinary;

  public Iterable<DBRental> findAll() {
    return DBRentalRepository.findAll();
  }

  public DBRental findById(Long id) {
    return DBRentalRepository.findById(id).orElse(null);
  }

  public DBRental save(DBRental DBRental) {
    return DBRentalRepository.save(DBRental);
  }

  public SingleRentalResponse findRentalResponseById(Long id) {
    DBRental rental = DBRentalRepository.findById(id).orElse(null);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    if (rental == null) {
        return null;
    }

    SingleRentalResponse response = new SingleRentalResponse();
    response.setId(rental.getId());
    response.setName(rental.getName());
    response.setSurface(rental.getSurface());
    response.setPrice(rental.getPrice());
    response.setDescription(rental.getDescription());
    response.setOwner_id(rental.getOwner_id());
    response.setPicture(Collections.singletonList(rental.getPicture()));
    response.setCreated_at(rental.getCreated_at().toLocalDateTime().format(formatter));
    response.setUpdated_at(rental.getUpdated_at().toLocalDateTime().format(formatter));

    return response;
}


  public Iterable<RentalResponse> findAllRentalResponses() {
      Iterable<DBRental> rentals = DBRentalRepository.findAll();

      return StreamSupport.stream(rentals.spliterator(), false)
          .map(rental -> {
            RentalResponse response = new RentalResponse();
            response.setId(rental.getId());
            response.setName(rental.getName());
            response.setSurface(rental.getSurface());
            response.setPrice(rental.getPrice());
            response.setDescription(rental.getDescription());
            response.setOwner_id(rental.getOwner_id());
            response.setPicture(rental.getPicture());
            response.setCreated_at(rental.getCreated_at().toString());
            response.setUpdated_at(rental.getUpdated_at().toString());
            return response;
          })
          .collect(Collectors.toList());
  }

  public RentalResponse createRental(String name,
                                    int surface,
                                    int price,
                                    String description,
                                    MultipartFile picture,
                                    String email)
                                    throws IOException {

    DBUser user = DBUserRepository.findByEmail(email);
      if (user == null) {
        throw new IllegalArgumentException("User not found");
      }

      DBRental rental = new DBRental();
      rental.setName(name);
      rental.setSurface(surface);
      rental.setPrice(price);
      rental.setDescription(description);
      rental.setOwner_id(user.getId());

      if (picture != null && !picture.isEmpty()) {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(picture.getBytes(), ObjectUtils.emptyMap());
        String url = (String) uploadResult.get("url");
        rental.setPicture(url);
      }

      DBRental savedRental = DBRentalRepository.save(rental);

      RentalResponse response = new RentalResponse();
      response.setId(savedRental.getId());
      response.setName(savedRental.getName());
      response.setSurface(savedRental.getSurface());
      response.setPrice(savedRental.getPrice());
      response.setDescription(savedRental.getDescription());
      response.setOwner_id(savedRental.getOwner_id());
      response.setPicture(savedRental.getPicture());
      response.setCreated_at(savedRental.getCreated_at().toString());
      response.setUpdated_at(savedRental.getUpdated_at().toString());

      return response;
  }

  public SingleRentalResponse updateRental(Long id, String name, int surface, int price, String description) {
    DBRental rental = DBRentalRepository.findById(id).orElse(null);
    if (rental == null) {
        throw new IllegalArgumentException("Rental not found");
    }

    rental.setName(name);
    rental.setSurface(surface);
    rental.setPrice(price);
    rental.setDescription(description);

    DBRental updatedRental = DBRentalRepository.save(rental);

    SingleRentalResponse response = new SingleRentalResponse();
    response.setId(updatedRental.getId());
    response.setName(updatedRental.getName());
    response.setSurface(updatedRental.getSurface());
    response.setPrice(updatedRental.getPrice());
    response.setDescription(updatedRental.getDescription());
    response.setOwner_id(updatedRental.getOwner_id());
    response.setPicture(Collections.singletonList(updatedRental.getPicture())); // Convertir en tableau
    response.setCreated_at(updatedRental.getCreated_at().toString());
    response.setUpdated_at(updatedRental.getUpdated_at().toString());

    return response;
}

}
