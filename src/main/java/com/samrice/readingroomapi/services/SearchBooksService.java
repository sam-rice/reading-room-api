package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;

import java.util.List;

public interface SearchBooksService {

    List<BookResultDto> searchBooks(String query);

    BookDetailsDto getBook(int userId, String libraryKey);
}
