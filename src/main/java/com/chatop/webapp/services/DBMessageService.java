package com.chatop.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.webapp.model.DBMessage;
import com.chatop.webapp.repository.DBMessageRepository;
import com.chatop.webapp.requests.MessageRequest;

import lombok.Data;

@Data
@Service
public class DBMessageService {

  @Autowired
  private DBMessageRepository dbMessageRepository;

    public DBMessage createMessage(MessageRequest messageRequest) {
      DBMessage message = new DBMessage();
      message.setMessage(messageRequest.getMessage());
      message.setUser_id(messageRequest.getUser_id());
      message.setRental_id(messageRequest.getRental_id());

      return dbMessageRepository.save(message);
    }
}
