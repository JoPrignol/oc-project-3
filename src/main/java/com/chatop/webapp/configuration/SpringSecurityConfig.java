package com.chatop.webapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	// Mise en place de filtres en fonction des rôles des utilisateurs.
	// @Bean
	// public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
	// 	return http.authorizeHttpRequests(auth -> {
  //     auth.requestMatchers("/user");
	// 		// Faire en sorte que toutes les requêtes soient sécurisées
	// 		auth.anyRequest().authenticated();
	// 	}).formLogin(Customizer.withDefaults())
  //   .oauth2Login(Customizer.withDefaults())
  //   .build();
	// }

  @Bean
	public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> {
      auth.requestMatchers("/auth/login", "/auth/register").permitAll();
			auth.anyRequest().authenticated();
		}).formLogin(Customizer.withDefaults())
    .build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

  // @Autowired
  // private CustomUserDetailsService customUserDetailsService;

  // @Bean
  // public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
	//   AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
  //   authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
	//   return authenticationManagerBuilder.build();
  // }

  // @Bean
	// public UserDetailsService users() {
	// 	UserDetails user = User.builder()
	// 			.username("user")
	// 			.password(passwordEncoder().encode("user"))
	// 			.roles("USER").build();
	// 	return new InMemoryUserDetailsManager(user);
	// }
}
