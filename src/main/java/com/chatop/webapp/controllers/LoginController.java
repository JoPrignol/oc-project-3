package com.chatop.webapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @GetMapping("/user")
  public String getUser(){
    return "Hello, User";
  }

  @GetMapping("/")
	public String getGitHub() {
		return "Welcome, GitHub user";
	}
}
