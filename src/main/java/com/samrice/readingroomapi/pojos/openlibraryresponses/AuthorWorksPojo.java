package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record AuthorWorksPojo(
        Integer size,
        List<AuthorWorkPojo> entries
) {
}
