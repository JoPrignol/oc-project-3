package com.chatop.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.requests.RegisterRequest;

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


    public DBUser createUserFromRegisterRequest(RegisterRequest request) {
        // Créer un nouvel objet DBUser
        DBUser newUser = new DBUser();

        // Mapper les champs de RegisterRequest à DBUser
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());  // Encoder le mot de passe

        // Sauvegarder l'utilisateur dans la base de données
        return DBUserRepository.save(newUser);
    }
}
