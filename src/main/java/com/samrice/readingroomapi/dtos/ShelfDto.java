package com.samrice.readingroomapi.dtos;

import com.samrice.readingroomapi.domains.Book;

import java.util.List;

public record ShelfDto(Integer shelfId, Integer userId, String title, String description, Integer totalSavedBooks, String featuredCoverUrl) {
}
