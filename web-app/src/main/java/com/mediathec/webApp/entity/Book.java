package com.mediathec.webApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String coverImage;
    private boolean available;

    public Book() {
    }

    public Book(Long id, String title, String author, String category, String description, String coverImage, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.coverImage = coverImage;  // ← AJOUTER
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // AJOUT GETTER ET SETTER POUR coverImage
    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}