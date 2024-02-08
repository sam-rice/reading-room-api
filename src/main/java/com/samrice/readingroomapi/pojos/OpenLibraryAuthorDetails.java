package com.samrice.readingroomapi.pojos;

import java.util.List;

public record OpenLibraryAuthorDetails(String key, String name, String bio, List<Integer> photos, String birth_date, String death_date) {
}
