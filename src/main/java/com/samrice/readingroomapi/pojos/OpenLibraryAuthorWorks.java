package com.samrice.readingroomapi.pojos;

import java.util.List;

public record OpenLibraryAuthorWorks(Integer size, List<OpenLibraryWork> entries) {
}
