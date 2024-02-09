package com.samrice.readingroomapi.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

public class OpenLibraryUtils {
    public static final String BASE_URL = "https://openlibrary.org";
    public static final String WORKS_BASE_URL = "https://openlibrary.org/works";
    public static final String SEARCH_BASE_URL = "https://openlibrary.org/search";
    public static final String AUTHORS_BASE_URL = "https://openlibrary.org/authors";

    private static final RestTemplate restTemplate = new RestTemplate();

    public static <T> T getPojoFromEndpoint(String endpoint, Class<T> pojo) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        JsonNode root = Json.parse(response.getBody());
        return Json.fromJson(root, pojo);
    }

    public static String formatKey(String key) {
        return key.substring(key.indexOf("/", key.indexOf("/") + 1) + 1);
    }

    public static String getPhotoUrl(List<Integer> covers) {
        return Optional.ofNullable(covers)
                .map(c -> !c.isEmpty() ? "https://covers.openlibrary.org/b/id/" + c.get(0) + "-L.jpg" : null)
                .orElse(null);
    }
}
