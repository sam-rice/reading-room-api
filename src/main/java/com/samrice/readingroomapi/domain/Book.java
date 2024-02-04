package com.samrice.readingroomapi.domain;

//public record Book {

//    private Integer bookId;
//    private Integer shelfId;
//    private Integer userId;
//    private String isbn;
//    private String olKey;
//    private String title;
//    private String author;
//    private String userNote;
//    private long savedDate;

import org.springframework.data.annotation.Id;

public record Book(@Id Integer bookId, Integer shelfId, Integer userId, String isbn, String title, String author, String coverUrl, String userNote, long savedDate) {
//        this.bookId = bookId;
//        this.shelfId = shelfId;
//        this.userId = userId;
//        this.isbn = isbn;
//        this.olKey = olKey;
//        this.title = title;
//        this.author = author;
//        this.userNote = userNote;
//        this.savedDate = savedDate;
//    }

//    public Integer getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(Integer bookId) {
//        this.bookId = bookId;
//    }
//
//    public Integer getShelfId() {
//        return shelfId;
//    }
//
//    public void setShelfId(Integer shelfId) {
//        this.shelfId = shelfId;
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public String getOlKey() {
//        return olKey;
//    }
//
//    public void setOlKey(String olKey) {
//        this.olKey = olKey;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getUserNote() {
//        return userNote;
//    }
//
//    public void setUserNote(String userNote) {
//        this.userNote = userNote;
//    }
//
//    public long getSavedDate() {
//        return savedDate;
//    }
//
//    public void setSavedDate(long savedDate) {
//        this.savedDate = savedDate;
//    }
}
