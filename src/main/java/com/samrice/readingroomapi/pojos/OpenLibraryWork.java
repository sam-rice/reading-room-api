package com.samrice.readingroomapi.pojos;

import java.util.List;

public record OpenLibraryWork(String title, List<String> subjects, String key, List<String> isbn_13, List<IndividualAuthorPojo> authors, List<Integer> covers) {
}

