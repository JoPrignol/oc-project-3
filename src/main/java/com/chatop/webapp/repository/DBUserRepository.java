package com.chatop.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.webapp.model.DBUser;


public interface DBUserRepository extends JpaRepository<DBUser, Integer> {

  public DBUser findByUsername(String username);

}
