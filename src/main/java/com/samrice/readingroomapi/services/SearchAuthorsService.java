package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.AuthorResultsPageDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;

import java.util.List;

public interface SearchAuthorsService {

    AuthorResultsPageDto searchAuthors(String authorName, int pageSize, int pageNum) throws RrBadRequestException;

    AuthorDetailsDto getAuthor(String libraryKey) throws RrBadRequestException;
}
