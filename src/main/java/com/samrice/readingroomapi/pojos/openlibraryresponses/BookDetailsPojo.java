package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;
import java.util.Map;

public record BookDetailsPojo(
        String title,
        Map<String, String> description,
        List<Integer> covers,
        String first_publish_date,
        List<BasicAuthorPojo> authors,
        List<String> subjects
) {
}
