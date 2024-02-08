package com.samrice.readingroomapi.dtos;

import java.util.List;

public record AuthorDetailsDto(String name, String bio, String photoUrl, String birthDate, String deathDate, Integer workCount, List<BookResultDto> works) {
}
