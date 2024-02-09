package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.AuthorResultDto;
import com.samrice.readingroomapi.pojos.openlibraryresponses.*;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class SearchAuthorsServiceImpl implements SearchAuthorsService {

    @Override
    public List<AuthorResultDto> searchAuthors(String authorName) throws RrBadRequestException {
        try {
            return getAllAuthorResults(authorName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Something went wrong. Failed to query authors.");
        }
    }

    @Override
    public AuthorDetailsDto getAuthor(String authorKey) throws RrBadRequestException {
        try {
            return getAuthorDetails(authorKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid author key.");
        }
    }

    private AuthorDetailsDto getAuthorDetails(String authorKey) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.AUTHORS_BASE_URL + "/" + authorKey + ".json";
        AuthorDetailsPojo authorDetails = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorDetailsPojo.class);
        String photoUrl = OpenLibraryUtils.getPhotoUrl(authorDetails.photos());
        String formattedAuthorKey = OpenLibraryUtils.formatKey(authorDetails.key());
        String bio = Optional.ofNullable(authorDetails.bio())
                .map(value -> value instanceof String ? (String) value : ((Map<?, ?>) value).get("value").toString())
                .orElse(null);

        AuthorWorksResult result = getAuthorWorks(formattedAuthorKey, authorDetails.name());
        return new AuthorDetailsDto(formattedAuthorKey, authorDetails.name(), bio, photoUrl, authorDetails.birth_date(), authorDetails.death_date(), result.workCount(), result.works());
    }

    private AuthorWorksResult getAuthorWorks(String authorKey, String authorName) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.AUTHORS_BASE_URL + "/" + authorKey + "/works.json?limit=1000";
        AuthorWorksPojo works = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorWorksPojo.class);
        List<BookResultDto> worksList = works.entries()
                .stream()
                .map(w -> mapToBookResultDto(w, authorName, authorKey)).toList();

        return new AuthorWorksResult(works.size(), worksList);
    }

    private BookResultDto mapToBookResultDto(WorkPojo work, String authorName, String authorKey) {
        String key = OpenLibraryUtils.formatKey(authorKey);
        String coverUrl = OpenLibraryUtils.getPhotoUrl(work.covers());
        HashMap<String, String> author = new HashMap<>();
        author.put("name", authorName);
        author.put("key", authorKey);
        Boolean byMultipleAuthors = work.authors().size() > 1;
        return new BookResultDto(key, work.title(), author, byMultipleAuthors, coverUrl);
    }

    private record AuthorWorksResult(Integer workCount, List<BookResultDto> works){}

    private List<AuthorResultDto> getAllAuthorResults(String authorName) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.SEARCH_BASE_URL + "/authors.json?q=" + authorName;
        AuthorSearchPojo authorResults = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorSearchPojo.class);
        return authorResults.docs()
                .stream()
                .filter(a -> a.work_count() != 0)
                .map(a -> mapToAuthorResultDto(a)).toList();
    }

    private AuthorResultDto mapToAuthorResultDto(AuthorResultPojo author) {
        return new AuthorResultDto(author.key(), author.name(), author.birth_date(), author.death_date(), author.top_work(), author.work_count(), author.top_subjects());
    }
}
