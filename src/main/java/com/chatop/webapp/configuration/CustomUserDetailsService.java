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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DBUser user = DBUserRepository.findByUsername(username);

        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }


	// @Override
	// public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	// 	DBUser user = DBUserRepository.findByUsername(username);

	// 	return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
	// }

  // private List<GrantedAuthority> getGrantedAuthorities(DBUser user) {
  //   List<GrantedAuthority> authorities = new ArrayList<>();
  //   authorities.add(new SimpleGrantedAuthority(user.getRole()));
  //   return authorities;
  // }
}
