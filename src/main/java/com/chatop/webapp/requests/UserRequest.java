package com.chatop.webapp.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest {
  private String name;
  private String id;
  private String email;
  private String created_at;
  private String updated_at;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }

  @JsonProperty("id")
  public Long getIdAsLong() {
    return id != null ? Long.parseLong(id) : null;
  }
}
