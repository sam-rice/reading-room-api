package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.AuthorBookDto;
import com.samrice.readingroomapi.dtos.AuthorResultsPageDto;
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
    public AuthorResultsPageDto searchAuthors(String query, int pageSize, int pageNum) throws RrBadRequestException {
        try {
            AuthorResultsPageDto resultsPage = getAllAuthorResults(query, pageSize, pageNum);
            int lastPageNum = (int) Math.ceil((float) resultsPage.totalResults() / resultsPage.pageSize());
            if (pageNum > lastPageNum && pageNum != 1) {
                throw new RrBadRequestException("Page number out of bounds.");
            } else {
                return resultsPage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Something went wrong. Failed to query authors.");
        }
    }

    @Override
    public AuthorDetailsDto getAuthor(String libraryKey) throws RrBadRequestException {
        try {
            return getAuthorDetails(libraryKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid author libraryKey.");
        }
    }

    private AuthorResultsPageDto getAllAuthorResults(String query, int pageSize, int pageNum) throws JsonProcessingException {
        int offset = pageSize * (pageNum - 1);
        String endpoint = OpenLibraryUtils.SEARCH_BASE_URL + "/authors.json?q=" + query + "&fields=key,name,birth_date,death_date,top_work,work_count,top_subjects&limit=" + pageSize + "&offset=" + offset;
        AuthorSearchPojo result = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorSearchPojo.class);
        List<AuthorResultDto> resultDtos = result.docs()
                .stream()
                .map(a -> mapToAuthorResultDto(a)).toList();

        return new AuthorResultsPageDto(result.numFound(), pageSize, pageNum, resultDtos);
//                .filter(a -> a.work_count() != 0 && a.name().contains(" ") && a.top_work() != null)
    }

    private AuthorResultDto mapToAuthorResultDto(AuthorResultPojo author) {
        return new AuthorResultDto(author.key(),
                author.name(),
                author.birth_date(),
                author.death_date(),
                author.top_work(),
                author.top_subjects());
    }

    private AuthorDetailsDto getAuthorDetails(String libraryKey) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.AUTHORS_BASE_URL + "/" + libraryKey + ".json";
        AuthorDetailsPojo authorDetails = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorDetailsPojo.class);
        String photoUrl = OpenLibraryUtils.getPhotoUrl(authorDetails.photos());
        String bio = Optional.ofNullable(authorDetails.bio())
                .map(value -> value instanceof String ? (String) value : ((Map<?, ?>) value).get("value").toString())
                .orElse(null);

        AuthorBooksResult result = getAuthorBooks(libraryKey, authorDetails.name());
        return new AuthorDetailsDto(libraryKey,
                authorDetails.name(),
                bio,
                photoUrl,
                authorDetails.birth_date(),
                authorDetails.death_date(),
                result.works());
    }

    private AuthorBooksResult getAuthorBooks(String libraryAuthorKey,
                                             String authorName) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.AUTHORS_BASE_URL + "/" + libraryAuthorKey + "/works.json?limit=1000";
        AuthorWorksPojo works = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorWorksPojo.class);
        List<AuthorBookDto> booksList = works.entries()
                .stream()
                .filter(w -> w.first_publish_date() != null)
                .map(w -> mapToAuthorBookDto(w, authorName, libraryAuthorKey)).toList();
        return new AuthorBooksResult(works.size(), booksList);
    }

    private AuthorBookDto mapToAuthorBookDto(AuthorWorkPojo work, String authorName, String authorLibraryKey) {
        String formattedLibraryBookKey = OpenLibraryUtils.formatKey(work.key());
        String coverUrl = OpenLibraryUtils.getPhotoUrl(work.covers());
        BasicAuthor author = new BasicAuthor(authorName, authorLibraryKey);
        Boolean byMultipleAuthors = work.authors().size() > 1;
        return new AuthorBookDto(formattedLibraryBookKey,
                work.title(),
                work.first_publish_date(),
                author,
                byMultipleAuthors,
                coverUrl,
                work.subjects());
    }

    private record AuthorBooksResult(Integer workCount, List<AuthorBookDto> works) {
    }
}
