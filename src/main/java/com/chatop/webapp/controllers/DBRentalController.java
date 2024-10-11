package com.chatop.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.webapp.model.DBRental;
import com.chatop.webapp.services.DBRentalService;


@RestController
@RequestMapping("/api")
public class DBRentalController {

  @Autowired
  private DBRentalService DBRentalService;

  @GetMapping("/rentals")
  public Iterable<DBRental> findAll() {
      return DBRentalService.findAll();
  }

  @GetMapping("/rentals/{id}")
  public DBRental getRentalById(@PathVariable Long id) {
    return DBRentalService.findById(id);
  }
}
