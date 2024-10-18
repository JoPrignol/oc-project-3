package com.chatop.webapp.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chatop.webapp.model.DBUser;
import com.chatop.webapp.repository.DBUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private DBUserRepository DBUserRepository;

    @Override
    // public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    //     DBUser user = DBUserRepository.findByName(name);

    //     if (user == null) {
    //       throw new UsernameNotFoundException("User not found");
    //   }

    //     return new User(user.getName(), user.getPassword(), new ArrayList<>());
    // }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      DBUser user = DBUserRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
  }

    // public Long getId(String name) {
    //   DBUser user = DBUserRepository.findByName(name);
    //   return user.getId();
    // }
  }
