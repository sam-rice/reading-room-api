package com.samrice.readingroomapi.domains;

import org.springframework.data.annotation.Id;

public record Shelf(@Id Integer shelfId, Integer userId, String title, String description, Integer totalSavedBooks) {}