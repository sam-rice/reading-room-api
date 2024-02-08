package com.samrice.readingroomapi.dtos;

import java.util.List;

public record BookDetailsDto(String key, String title, List<String> tags, String authorName, String description, String isbn, String coverUrl, String publishDate) {
}
