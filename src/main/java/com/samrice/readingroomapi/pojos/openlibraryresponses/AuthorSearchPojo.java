package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record AuthorSearchPojo(int numFound, List<AuthorResultPojo> docs) {
}

