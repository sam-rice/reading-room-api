package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samrice.readingroomapi.Constants;
import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.AuthorResultDto;
import com.samrice.readingroomapi.pojos.OpenLibraryAuthorDetails;
import com.samrice.readingroomapi.pojos.OpenLibraryAuthorWorks;
import com.samrice.readingroomapi.pojos.OpenLibraryWork;
import com.samrice.readingroomapi.utilities.Json;
import com.samrice.readingroomapi.utilities.OpenLibraryCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LibrarySearchServiceImpl implements LibrarySearchService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<AuthorResultDto> searchAuthors(String authorName) throws RrBadRequestException {
        try {
            return getAllAuthorResults(authorName);
        } catch (Exception e) {
            throw new RrBadRequestException("Something went wrong. Failed to query authors.");
        }
    }

    @Override
    public AuthorDetailsDto getAuthor(String authorKey) throws RrBadRequestException {
        try {
            return getAuthorDetails(authorKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Something went wrong. Failed to find author details.");
        }
    }

    private AuthorDetailsDto getAuthorDetails(String authorKey) throws JsonProcessingException {
        String endpoint = Constants.OPEN_LIBRARY_AUTHORS_BASE_URL + "/" + authorKey + ".json";
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        JsonNode root = Json.parse(response.getBody());
        OpenLibraryAuthorDetails authorDetails = Json.fromJson(root, OpenLibraryAuthorDetails.class);
        String photoUrl = "https://covers.openlibrary.org/a/olid/" + authorDetails.photos().get(0) + "-L.jpg";
        String formattedAuthorKey = OpenLibraryCleaner.formatKey(authorDetails.key());
        AuthorWorksResult result = getAuthorWorks(formattedAuthorKey, authorDetails.name());
        return new AuthorDetailsDto(formattedAuthorKey, authorDetails.name(), authorDetails.bio(), photoUrl, authorDetails.birth_date(), authorDetails.death_date(), result.workCount(), result.works());
    }

    private AuthorWorksResult getAuthorWorks(String authorKey, String authorName) throws JsonProcessingException {
        String endpoint = Constants.OPEN_LIBRARY_AUTHORS_BASE_URL + "/" + authorKey + "/works.json?limit=1000";
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        JsonNode root = Json.parse(response.getBody());
        OpenLibraryAuthorWorks works = Json.fromJson(root, OpenLibraryAuthorWorks.class);
        List<BookResultDto> worksList = works.entries()
                .stream()
                .map(w -> mapToBookResultDto(w, authorName, authorKey)).toList();
        return new AuthorWorksResult(works.size(), worksList);
    }

    private BookResultDto mapToBookResultDto(OpenLibraryWork work, String authorName, String authorKey) {
        String key = work.key().replaceFirst("/works/", "");
        String coverUrl = work.covers() != null ? "https://covers.openlibrary.org/a/olid/" + work.covers().get(0).toString() + "-L.jpg" : null;
        HashMap<String, String> author = new HashMap<>();
        author.put("name", authorName);
        author.put("key", authorKey);
        Boolean byMultipleAuthors = work.authors().size() > 1;
        return new BookResultDto(key, work.title(), author, byMultipleAuthors, coverUrl);
    }

    private record AuthorWorksResult(Integer workCount, List<BookResultDto> works){}

    private List<AuthorResultDto> getAllAuthorResults(String authorName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String endpoint = Constants.OPEN_LIBRARY_SEARCH_BASE_URL + "/authors.json?q=" + authorName;
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        JsonNode root = mapper.readTree(response.getBody());
        List<LinkedHashMap> parsedAuthorsMap = mapper.convertValue(root.get("docs"), List.class);
        return parsedAuthorsMap
                .stream()
                .filter(a -> Integer.parseInt(a.get("work_count").toString()) != 0)
                .map(a -> mapToAuthorResultDto(a)).toList();
    }

    private AuthorResultDto mapToAuthorResultDto(LinkedHashMap author) {
        String key = author.get("key").toString();
        String name = author.get("name").toString();
        String birthDate = author.get("birth_date") != null ? author.get("birth_date").toString() : null;
        String deathDate = author.get("death_date") != null ? author.get("death_date").toString() : null;
        String topWork = author.get("top_work") != null ? author.get("top_work").toString() : null;
        Integer workCount = Integer.parseInt(author.get("work_count").toString());
        List<String> topSubjects = (List<String>) author.get("top_subjects");
        return new AuthorResultDto(key, name, birthDate, deathDate, topWork, workCount, topSubjects);
    }
}
