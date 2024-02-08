package com.samrice.readingroomapi.dtos;

import java.util.HashMap;

public record BookResultDto(String key, String title, HashMap<String, String> primaryAuthor, Boolean byMultipleAuthors, String coverUrl) {
}
