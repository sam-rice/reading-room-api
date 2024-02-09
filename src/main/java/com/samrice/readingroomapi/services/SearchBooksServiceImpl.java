package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.BasicAuthorPojo;
import com.samrice.readingroomapi.pojos.openlibraryresponses.BookDetailsPojo;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;

import java.util.List;

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

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid book key.");
        }
    }

    private BookDetailsDto getBookDetails(String key) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.WORKS_BASE_URL + "/" + key + ".json";
        BookDetailsPojo bookDetails = OpenLibraryUtils.getPojoFromEndpoint(endpoint, BookDetailsPojo.class);
        String coverUrl = OpenLibraryUtils.getPhotoUrl(bookDetails.covers());

        List<BasicAuthor> authorsList = getAuthorsList(bookDetails.authors());

    }

    private List<BasicAuthor> getAuthorsList(List<BasicAuthorPojo> authorsPojo)
}
