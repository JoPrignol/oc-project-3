package com.chatop.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.webapp.model.DBRental;
import com.chatop.webapp.repository.DBRentalRepository;

import lombok.Data;

@Data
@Service
public class DBRentalService {

    @Autowired
    private DBRentalRepository DBRentalRepository;

    public Iterable<DBRental> findAll() {
        return DBRentalRepository.findAll();
    }

    public DBRental findById(Long id) {
        return DBRentalRepository.findById(id).orElse(null);
    }

    public DBRental save(DBRental DBRental) {
        return DBRentalRepository.save(DBRental);
    }

    // public void deleteById(Long id) {
    //     DBRentalRepository.deleteById(id);
    // }


}
