package com.chatop.webapp.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.webapp.exception.EmailAlreadyRegisteredException;
import com.chatop.webapp.exception.UserNotFoundException;
import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;
import com.chatop.webapp.requests.RegisterRequest;
import com.chatop.webapp.requests.UserRequest;

import lombok.Data;

@Data
@Service
public class DBUserService {

  @Autowired
  private DBUserRepository DBUserRepository;

    public Iterable<UserRequest> findAll() {
      // Récupère tous les utilisateurs et les transforme en UserRequest
      return DBUserRepository.findAll().stream()
            .map(user -> {
              UserRequest userRequest = new UserRequest();
              userRequest.setName(user.getName());
              userRequest.setId(user.getId().toString());
              userRequest.setEmail(user.getEmail());
              userRequest.setCreated_at(user.getCreated_at().toString());
              userRequest.setUpdated_at(user.getUpdated_at().toString());
              return userRequest;
            })
            .collect(Collectors.toList());
    }

    public DBUser findById(Long id) {
      return DBUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public DBUser findUserByEmail(String email) {
      return DBUserRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public DBUser findUserByName(String name) {
      return DBUserRepository.findUserByName(name).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public DBUser createUserFromRegisterRequest(RegisterRequest request) {

      if(DBUserRepository.findUserByEmail(request.getEmail()).isPresent()) {
        throw new EmailAlreadyRegisteredException("Email already registered");
      }

      // Créer un nouvel objet DBUser
      DBUser newUser = new DBUser();

      // Mapper les champs de RegisterRequest à DBUser
      newUser.setName(request.getName());
      newUser.setEmail(request.getEmail());
      newUser.setPassword(request.getPassword());

      // Sauvegarder l'utilisateur dans la base de données
      return DBUserRepository.save(newUser);
    }

    public UserRequest getUserById(Long id) {
      DBUser user = DBUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

      // Mapper DBUser vers UserRequest
      UserRequest userRequest = new UserRequest();
      userRequest.setName(user.getName());
      userRequest.setId(user.getId().toString());
      userRequest.setEmail(user.getEmail());
      userRequest.setCreated_at(user.getCreated_at().toString());
      userRequest.setUpdated_at(user.getUpdated_at().toString());

      return userRequest;
    }

    public UserRequest getCurrentUserByEmail(String email) {
      DBUser user = DBUserRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

      UserRequest userRequest = new UserRequest();
        userRequest.setName(user.getName());
        userRequest.setId(user.getId().toString());
        userRequest.setEmail(user.getEmail());
        userRequest.setCreated_at(user.getCreated_at().toString());
        userRequest.setUpdated_at(user.getUpdated_at().toString());

        return userRequest;
  }
}
