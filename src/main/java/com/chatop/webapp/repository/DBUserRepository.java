package com.chatop.webapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.webapp.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {

  public DBUser findByName(String name);
  public DBUser findByEmail (String email);
  public Optional<DBUser> findUserByName(String name);
  public Optional<DBUser> findUserByEmail(String email);
}
