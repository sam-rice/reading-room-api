package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record AuthorWorkPojo(
        String title,
        List<String> subjects,
        String key,
        List<BasicAuthorPojo> authors,
        List<Integer> covers
) {
}

