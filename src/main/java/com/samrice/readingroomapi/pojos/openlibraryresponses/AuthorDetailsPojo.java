package com.samrice.readingroomapi.pojos.openlibraryresponses;

import java.util.List;

public record AuthorDetailsPojo(String key, String name, String bio, List<Integer> photos, String birth_date, String death_date) {
}
