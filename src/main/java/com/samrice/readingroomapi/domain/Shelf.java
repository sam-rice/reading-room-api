package com.samrice.readingroomapi.domain;

public class Shelf {
    private Integer shelfId;
    private Integer userId;
    private String title;
    private String description;
    private Integer totalSavedBooks;

    public Shelf(Integer shelfId, Integer userId, String title, String description, Integer totalSavedBooks) {
        this.shelfId = shelfId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.totalSavedBooks = totalSavedBooks;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalSavedBooks() {
        return totalSavedBooks;
    }

    public void setTotalSavedBooks(Integer totalSavedBooks) {
        this.totalSavedBooks = totalSavedBooks;
    }
}
