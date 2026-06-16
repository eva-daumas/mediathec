package com.mediathec.membersservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(unique = true, nullable = false)
    private String username;


    @Email
    @Column(unique = true, nullable = false)
    private String email;



    private String password;

    private String role = "USER";

    @Column(name = "created_at")
    private LocalDateTime createdAt;



   // @PrePersist
  //  protected void onCreate() {
     //   createdAt = LocalDateTime.now();
  //  }

}
