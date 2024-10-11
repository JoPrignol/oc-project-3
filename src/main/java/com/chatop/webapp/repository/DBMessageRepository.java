package com.chatop.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chatop.webapp.model.DBMessage;

@Repository
public interface DBMessageRepository extends CrudRepository<DBMessage, Long> {

}
