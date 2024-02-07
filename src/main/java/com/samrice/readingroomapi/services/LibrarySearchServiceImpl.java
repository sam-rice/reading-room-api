package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samrice.readingroomapi.Constants;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.AuthorResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

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
            throw new RrBadRequestException(e.getMessage());
        }
    }

    private List<AuthorResultDto> getAllAuthorResults(String authorName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String endpoint = Constants.OPEN_LIBRARY_SEARCH_BASE_URL + "/authors.json?q=" + authorName;
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        JsonNode searchResponseRoot = mapper.readTree(response.getBody());
        List<LinkedHashMap> authorsResponse = mapper.convertValue(searchResponseRoot.get("docs"), List.class);
        List<AuthorResultDto> authorsList = authorsResponse
            .stream()
            .filter(a -> Integer.parseInt(a.get("work_count").toString()) != 0)
            .map(a -> mapToAuthorResultDto(a)).toList();
        return authorsList;
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
