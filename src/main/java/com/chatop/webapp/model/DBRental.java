package com.chatop.webapp.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rentals")
public class DBRental {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="name")
  private String name;

  @Column(name="surface")
  private int surface;

  @Column(name="price")
  private int price;

  @Column(name="picture")
  private String picture;

  @Column(name="description")
  private String description;

  @Column(name="owner_id")
  private Long ownerId;

  @CreationTimestamp
  @Column(name="created_at", updatable = false, nullable = false)
  private Timestamp createdAt;

  @UpdateTimestamp
  @Column(name="updated_at", nullable = false)
  private Timestamp updatedAt;

}
