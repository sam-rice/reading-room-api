package com.samrice.readingroomapi.dtos;

import java.util.List;

public record AuthorResultDto(String key, String name, String birthDate, String deathDate, String topWork, Integer workCount, List<String> topSubjects) {
}
