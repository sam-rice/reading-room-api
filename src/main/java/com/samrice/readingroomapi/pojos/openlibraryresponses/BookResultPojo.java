package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record BookResultPojo(
        String key,
        String title,
        Integer edition_count,
        Integer cover_i,
        Integer first_publish_year,
        List<String> author_key,
        List<String> author_name,
        List<String> subject
) {
}
