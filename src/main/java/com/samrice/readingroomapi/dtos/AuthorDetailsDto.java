package com.samrice.readingroomapi.dtos;

import java.util.List;

public record AuthorDetailsDto(
        String libraryKey,
        String name,
        String bio,
        String photoUrl,
        String birthDate,
        String deathDate,
        List<AuthorBookDto> books
) {
}
