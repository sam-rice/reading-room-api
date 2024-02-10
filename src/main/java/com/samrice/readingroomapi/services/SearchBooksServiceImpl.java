package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.BookDetailsPojo;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SearchBooksServiceImpl implements SearchBooksService{
    @Override
    public List<BookResultDto> searchBooks(String query) {
        try {

        return null;
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

    private BookDetailsDto getBookDetails(String key) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.WORKS_BASE_URL + "/" + key + ".json";
        BookDetailsPojo bookDetails = OpenLibraryUtils.getPojoFromEndpoint(endpoint, BookDetailsPojo.class);
        String coverUrl = OpenLibraryUtils.getPhotoUrl(bookDetails.covers());
        List<BasicAuthor> authors = OpenLibraryUtils.getBasicInfoForAllAuthors(bookDetails.authors());
        return new BookDetailsDto(bookDetails.title(), bookDetails.description(), bookDetails.first_publish_date(), authors, coverUrl, bookDetails.subjects());
    }

}
