package com.samrice.readingroomapi.dtos;

public record ShelfDto(Integer shelfId, Integer userId, String title, String description, Integer totalSavedBooks, String featuredCoverUrl) {
}
