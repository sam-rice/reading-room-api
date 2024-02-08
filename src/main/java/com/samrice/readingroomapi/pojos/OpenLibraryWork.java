package com.samrice.readingroomapi.pojos;

import java.util.List;
import java.util.Map;

public record OpenLibraryWork(String title, List<String> subjects, String key, List<IndividualAuthorPojo> authors, List<Integer> covers) {
}

record IndividualAuthorPojo(Map<String, String> author) {
}