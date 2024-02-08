package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.AuthorResultDto;

import java.util.List;

public interface LibrarySearchService {

    List<AuthorResultDto> searchAuthors(String authorName) throws RrBadRequestException;

    AuthorDetailsDto getAuthor(String authorKey) throws RrBadRequestException;

//    List<BookResultsDto> searchBooks(String bookTitle) throws RrBadRequestException;
}
