package com.samrice.readingroomapi.librarysearchresponses;

import java.util.List;

public record SearchedAuthor(String key, String name, String birthDate, String deathDate, String topWork, Integer workCount, List<String> topSubjects) {
}
