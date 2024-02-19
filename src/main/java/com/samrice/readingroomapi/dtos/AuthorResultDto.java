package com.samrice.readingroomapi.dtos;

import java.util.List;

public record AuthorResultDto(
        String libraryKey,
        String name,
        String birthDate,
        String deathDate,
        String topBook,
        List<String> topSubjects
) {
}
