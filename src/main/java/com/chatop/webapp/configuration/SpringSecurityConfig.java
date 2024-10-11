package com.chatop.webapp.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Bean
	public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth -> {
        auth.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/logout").permitAll();
        auth.anyRequest().authenticated();
      })
      .httpBasic(Customizer.withDefaults())
      .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
      // .logout(logout -> logout
      //   .logoutUrl("/api/auth/logout")
      //   .logoutSuccessUrl("/")
      //   .invalidateHttpSession(true)
      //   .deleteCookies("JSESSIONID")
      // )
      .build();
	}

  Dotenv dotenv = Dotenv.configure().load();
  private String jwtKey = dotenv.get("256_JWT_KEY");

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length,"RSA");
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
  }


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
