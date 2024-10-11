package com.chatop.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;

import lombok.Data;

@Data
@Service
public class DBUserService {

  @Autowired
  private DBUserRepository DBUserRepository;

    public Iterable<DBUser> findAll() {
        return DBUserRepository.findAll();
    }

    public DBUser findById(Long id) {
      return DBUserRepository.findById(id).orElse(null);
    }

    public DBUser findByEmail(String email) {
      return DBUserRepository.findByEmail(email).orElse(null);
    }

    // public DBUser findByName(String name) {
    //   return DBUserRepository.findByName(name).orElse(null);
    // }

    public DBUser findUserByName(String name) {
      return DBUserRepository.findUserByName(name).orElse(null);
    }
}
