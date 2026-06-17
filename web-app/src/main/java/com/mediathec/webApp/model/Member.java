package com.mediathec.webApp.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Member {

    private Long id;

    @NotBlank(message = "please enter your username")
    private String username;

    @NotBlank(message = "please enter your password")
    private String password;

    @NotBlank(message = "please enter your email")
    @Email(message = "Email address should be a valid value")
    private String email;

    private String role = "USER";

    private String createdAt;

    // Constructeur par défaut
    public Member() {
    }

    // Constructeur avec paramètres
    public Member(Long id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}