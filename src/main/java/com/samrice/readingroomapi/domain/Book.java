package com.samrice.readingroomapi.domain;

import org.springframework.data.annotation.Id;

public record Book(@Id Integer bookId, Integer shelfId, Integer userId, String isbn, String title, String author, String coverUrl, String userNote, long savedDate) {}