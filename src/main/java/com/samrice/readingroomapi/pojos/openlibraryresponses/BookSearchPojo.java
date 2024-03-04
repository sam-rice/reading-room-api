package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record BookSearchPojo(int numFound, List<BookResultPojo> docs) {
}
