package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samrice.readingroomapi.Constants;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.SearchedAuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<SearchedAuthorDto> searchAuthors(String authorName, Integer pageNo, Integer pageSize) throws RrBadRequestException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String endpoint = Constants.OPEN_LIBRARY_SEARCH_BASE_URL + "/authors.json?q=" + authorName;
            ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
            JsonNode searchResponseRoot = mapper.readTree(response.getBody());
            List<LinkedHashMap> authorsResponse = mapper.convertValue(searchResponseRoot.get("docs"), List.class);
            List<SearchedAuthorDto> authorsList = authorsResponse
                .stream()
                .filter(a -> Integer.parseInt(a.get("work_count").toString()) != 0)
                .map(a -> mapToSearchedAuthor(a)).toList();
            return authorsList;
        } catch (Exception e) {
            throw new RrBadRequestException(e.getMessage());
        }
    }

    private SearchedAuthorDto mapToSearchedAuthor(LinkedHashMap author) {
        String key = author.get("key").toString();
        String name = author.get("name").toString();
        String birthDate = author.get("birth_date") != null ? author.get("birth_date").toString() : null;
        String deathDate = author.get("death_date") != null ? author.get("death_date").toString() : null;
        String topWork = author.get("top_work") != null ? author.get("top_work").toString() : null;
        Integer workCount = Integer.parseInt(author.get("work_count").toString());
        List<String> topSubjects = (List<String>) author.get("top_subjects");
        return new SearchedAuthorDto(key, name, birthDate, deathDate, topWork, workCount, topSubjects);
    }
}
