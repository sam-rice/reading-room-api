package com.samrice.readingroomapi.dtos;

import com.samrice.readingroomapi.domains.BasicAuthor;

public record AuthorWorkDto(
        String key,
        String title,
        BasicAuthor primaryAuthor,
        Boolean byMultipleAuthors,
        String coverUrl
) {
}
