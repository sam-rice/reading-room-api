package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record WorkPojo(String title, List<String> subjects, String key, List<String> isbn_13, List<SimpleAuthorPojo> authors, List<Integer> covers) {
}

