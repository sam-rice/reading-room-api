package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultsPageDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;

import java.util.List;

public interface SearchBooksService {

    BookResultsPageDto searchBooks(String query, int pageSize, int pageNum) throws RrBadRequestException;

    BookDetailsDto getBook(int userId, String libraryKey) throws RrBadRequestException;
}
