package com.samrice.readingroomapi.dtos;

import com.samrice.readingroomapi.domains.BasicAuthor;

import java.util.List;

public record BookDetailsDto(
        String key,
        String title,
        String description,
        String publishDate,
        List<BasicAuthor> authors,
        String coverUrl,
        List<String> tags
) {
}
