package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.AuthorBookDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.AuthorResultDto;
import com.samrice.readingroomapi.pojos.openlibraryresponses.*;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class SearchAuthorsServiceImpl implements SearchAuthorsService {

    @Override
    public List<AuthorResultDto> searchAuthors(String query) throws RrBadRequestException {
        try {
            return getAllAuthorResults(query);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Something went wrong. Failed to query authors.");
        }
    }

    @Override
    public AuthorDetailsDto getAuthor(String key) throws RrBadRequestException {
        try {
            return getAuthorDetails(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid author key.");
        }
    }

    private List<AuthorResultDto> getAllAuthorResults(String query) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.SEARCH_BASE_URL + "/authors.json?q=" + query;
        AuthorSearchPojo results = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorSearchPojo.class);
        return results.docs()
                .stream()
                .filter(a -> a.work_count() != 0)
                .map(a -> mapToAuthorResultDto(a)).toList();
    }

    private AuthorResultDto mapToAuthorResultDto(AuthorResultPojo author) {
        return new AuthorResultDto(author.key(),
                author.name(),
                author.birth_date(),
                author.death_date(),
                author.top_work(),
                author.top_subjects());
    }

    private AuthorDetailsDto getAuthorDetails(String key) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.AUTHORS_BASE_URL + "/" + key + ".json";
        AuthorDetailsPojo authorDetails = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorDetailsPojo.class);
        String photoUrl = OpenLibraryUtils.getPhotoUrl(authorDetails.photos());
        String bio = Optional.ofNullable(authorDetails.bio())
                .map(value -> value instanceof String ? (String) value : ((Map<?, ?>) value).get("value").toString())
                .orElse(null);

        AuthorBooksResult result = getAuthorBooks(key, authorDetails.name());
        return new AuthorDetailsDto(key,
                authorDetails.name(),
                bio,
                photoUrl,
                authorDetails.birth_date(),
                authorDetails.death_date(),
                result.works());
    }

    private AuthorBooksResult getAuthorBooks(String authorKey, String authorName) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.AUTHORS_BASE_URL + "/" + authorKey + "/works.json?limit=1000";
        AuthorWorksPojo works = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorWorksPojo.class);
        List<AuthorBookDto> booksList = works.entries()
                .stream()
                .filter(w -> w.first_publish_date() != null)
                .map(w -> mapToAuthorBookDto(w, authorName, authorKey)).toList();
        return new AuthorBooksResult(works.size(), booksList);
    }

    private AuthorBookDto mapToAuthorBookDto(AuthorWorkPojo work, String authorName, String authorKey) {
        String formattedKey = OpenLibraryUtils.formatKey(work.key());
        String coverUrl = OpenLibraryUtils.getPhotoUrl(work.covers());
        BasicAuthor author = new BasicAuthor(authorName, authorKey);
        Boolean byMultipleAuthors = work.authors().size() > 1;
        return new AuthorBookDto(formattedKey, work.title(), work.first_publish_date(), author, byMultipleAuthors, coverUrl, work.subjects());
    }

    private record AuthorBooksResult(Integer workCount, List<AuthorBookDto> works){}
}
