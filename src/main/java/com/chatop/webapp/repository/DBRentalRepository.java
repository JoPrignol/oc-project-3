package com.chatop.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chatop.webapp.model.DBRental;

@Repository
public interface DBRentalRepository extends CrudRepository<DBRental, Long> {

}
