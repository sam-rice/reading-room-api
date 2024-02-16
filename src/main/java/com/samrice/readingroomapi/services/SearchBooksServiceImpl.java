package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.BookDetailsPojo;
import com.samrice.readingroomapi.pojos.openlibraryresponses.BookResultPojo;
import com.samrice.readingroomapi.pojos.openlibraryresponses.BookSearchPojo;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
public class SearchBooksServiceImpl implements SearchBooksService{
    @Override
    public List<BookResultDto> searchBooks(String query) {
        try {
            return getAllBookResults(query);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Something went wrong. Failed to query books.");
        }
    }

    @Override
    public BookDetailsDto getBook(String key) {
        try {
            return getBookDetails(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid book key.");
        }
    }

    private List<BookResultDto> getAllBookResults(String query) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.SEARCH_BASE_URL + ".json?q=" + query;
        BookSearchPojo results = OpenLibraryUtils.getPojoFromEndpoint(endpoint, BookSearchPojo.class);
        return results.docs()
                .stream()
                .filter(b -> b.first_publish_year() != null)
                .map(b -> mapToBookResultDto(b)).toList();
    }

    private BookResultDto mapToBookResultDto(BookResultPojo pojo) {
        List<BasicAuthor> authorsList = pojo.author_name() != null && pojo.author_key() != null ?
                IntStream.range(0, pojo.author_name().size())
                    .mapToObj(i -> new BasicAuthor(pojo.author_name().get(i), pojo.author_key().get(i)))
                    .toList()
                : null;
        String coverUrl = pojo.cover_i() != null ? "https://covers.openlibrary.org/b/id/" + pojo.cover_i() + "-L.jpg" : null;
        String formattedKey = OpenLibraryUtils.formatKey(pojo.key());
        return new BookResultDto(formattedKey,
                pojo.title(),
                pojo.first_publish_year(),
                pojo.edition_count(),
                authorsList,
                coverUrl,
                pojo.subject());
    }

    private BookDetailsDto getBookDetails(String key) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.WORKS_BASE_URL + "/" + key + ".json";
        BookDetailsPojo bookDetails = OpenLibraryUtils.getPojoFromEndpoint(endpoint, BookDetailsPojo.class);
        String coverUrl = OpenLibraryUtils.getPhotoUrl(bookDetails.covers());
        List<BasicAuthor> authors = OpenLibraryUtils.getBasicInfoForAllAuthors(bookDetails.authors());
        return new BookDetailsDto(key,
                bookDetails.title(),
                bookDetails.description(),
                bookDetails.first_publish_date(),
                authors,
                coverUrl,
                bookDetails.subjects());
    }
}
