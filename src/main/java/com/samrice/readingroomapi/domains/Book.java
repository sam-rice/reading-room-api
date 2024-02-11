package com.samrice.readingroomapi.domains;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Book(@Id Integer bookId, Integer shelfId, Integer userId, String key, String title, List<BasicAuthor> authors, String coverUrl, String userNote, long savedDate) {}