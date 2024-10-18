package com.chatop.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
public class ChatopApplication {

	public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure().load();
    System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
    System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

    SpringApplication.run(ChatopApplication.class, args);
  }

    @Bean
    public Cloudinary cloudinary() {
      Dotenv dotenv = Dotenv.configure().load();
      return new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }
}
