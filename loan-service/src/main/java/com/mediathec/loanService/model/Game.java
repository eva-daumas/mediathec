package com.mediathec.loanService.model;

public class Game {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String coverImage;
    private boolean available;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

