package com.chatop.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.webapp.model.DBMessage;
import com.chatop.webapp.repository.DBMessageRepository;

import lombok.Data;

@Data
@Service
public class DBMessageService {

  @Autowired
  private DBMessageRepository dbMessageRepository;

  public DBMessage createMessage(DBMessage message) {
      // Si vous avez besoin d'ajouter des champs supplémentaires, faites-le ici
      // message.setUserId(getCurrentUserId());
      return dbMessageRepository.save(message);
  }

}
