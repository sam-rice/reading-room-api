package com.samrice.readingroomapi.dtos;

import com.samrice.readingroomapi.domains.BasicAuthor;

import java.util.List;

public record AuthorBookDto(
        String key,
        String title,
        String publishDate,
        BasicAuthor primaryAuthor,
        Boolean byMultipleAuthors,
        String coverUrl,
        List<String> subjects
) {
}
