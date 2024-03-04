package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.dtos.AssociatedShelfDto;
import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.dtos.BookResultsPageDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.*;
import com.samrice.readingroomapi.repositories.BookRepository;
import com.samrice.readingroomapi.utilities.Json;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
public class SearchBooksServiceImpl implements SearchBooksService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public BookResultsPageDto searchBooks(String query, int pageSize, int pageNum) throws RrBadRequestException {
        try {
            BookResultsPageDto resultsPage = getPageOfBookResults(query, pageSize, pageNum);
            int lastPageNum = (int) Math.ceil((float) resultsPage.totalResults() / resultsPage.pageSize());
            if (pageNum > lastPageNum && pageNum != 1) {
                throw new RrBadRequestException("Page number out of bounds.");
            } else {
                return resultsPage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException(e.getMessage());
        }
    }

    @Override
    public BookDetailsDto getBook(int userId, String libraryKey) throws RrBadRequestException {
        try {
            return getBookDetails(userId, libraryKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid book libraryKey.");
        }
    }

    private BookResultsPageDto getPageOfBookResults(String query,
                                                    int pageSize,
                                                    int pageNum) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.SEARCH_BASE_URL + ".json?q=" + query + "&fields=key,title,edition_count,cover_i,first_publish_year,author_key,author_name,subject&limit=" + pageSize + "&page=" + pageNum;
        BookSearchPojo result = OpenLibraryUtils.getPojoFromEndpoint(endpoint, BookSearchPojo.class);
        List<BookResultDto> resultDtos = result.docs()
                .stream()
                .map(b -> mapToBookResultDto(b)).toList();

        return new BookResultsPageDto(result.numFound(), pageSize, pageNum, resultDtos);
    }

    private BookResultDto mapToBookResultDto(BookResultPojo pojo) {
        List<BasicAuthor> authorsList = pojo.author_name() != null && pojo.author_key() != null ?
                IntStream.range(0, pojo.author_name().size())
                        .mapToObj(i -> new BasicAuthor(pojo.author_name().get(i), pojo.author_key().get(i)))
                        .toList()
                : null;
        String coverUrl = pojo.cover_i() != null ? "https://covers.openlibrary.org/b/id/" + pojo.cover_i() + "-L.jpg" : null;
        String formattedLibraryBookKey = OpenLibraryUtils.formatKey(pojo.key());
        return new BookResultDto(formattedLibraryBookKey,
                pojo.title(),
                pojo.first_publish_year(),
                pojo.edition_count(),
                authorsList,
                coverUrl,
                pojo.subject());
    }

    private BookDetailsDto getBookDetails(int userId, String libraryKey) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.WORKS_BASE_URL + "/" + libraryKey + ".json";
        JsonNode root = OpenLibraryUtils.getRootFromEndpoint(endpoint);
        boolean hasNestedDescriptionField = root.toString().contains("\"description\":{\"type\"");
        BookDetailsPojo bookDetails;
        String description;

        if (hasNestedDescriptionField) {
            BookDetailsNestedDescriptionPojo pojo = Json.<BookDetailsNestedDescriptionPojo>fromJson(root,
                    BookDetailsNestedDescriptionPojo.class);
            description = pojo.getDescription() != null ? pojo.getDescription().get("value") : null;
            bookDetails = pojo;
        } else {
            BookDetailsUnNestedDetailsPojo pojo = Json.<BookDetailsUnNestedDetailsPojo>fromJson(root,
                    BookDetailsUnNestedDetailsPojo.class);
            description = pojo.getDescription();
            bookDetails = pojo;
        }

        String coverUrl = OpenLibraryUtils.getPhotoUrl(bookDetails.getCovers());
        List<BasicAuthor> authors = OpenLibraryUtils.getBasicInfoForAllAuthors(bookDetails.getAuthors());
        List<AssociatedShelfDto> associatedShelves = bookRepository.findAssociatedShelves(userId, libraryKey);
        return new BookDetailsDto(libraryKey,
                bookDetails.getTitle(),
                description,
                bookDetails.getFirst_publish_date(),
                authors,
                coverUrl,
                bookDetails.getSubjects(),
                associatedShelves);
    }
}
