package com.samrice.readingroomapi.pojos;

import java.util.List;

public record OpenLibraryAuthorResult(String key, String name, String birth_date, String death_date, String top_work,
                                      Integer work_count, List<String> top_subjects) {
}
