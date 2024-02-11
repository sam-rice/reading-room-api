package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record BookDetailsPojo(
        String title,
        String description,
        List<Integer> covers,
        String first_publish_date,
        List<BasicAuthorPojo> authors,
        List<String> subjects
) {
}
