package com.samrice.readingroomapi.dtos;

import com.samrice.readingroomapi.domains.BasicAuthor;

import java.util.List;

public record BookResultDto(
        String key,
        String title,
        Integer publishYear,
        Integer editionCount,
        List<BasicAuthor> authors,
        String coverUrl,
        List<String> tags
) {
}
