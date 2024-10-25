package com.chatop.webapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.webapp.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {

  // TODO: nettoyer -> grouper les méthodes

  public DBUser findByName(String name);
  public Optional<DBUser> findUserByName(String name);
  Optional<DBUser> findByEmail(String email);
  public DBUser getUserByEmail (String email);
}
