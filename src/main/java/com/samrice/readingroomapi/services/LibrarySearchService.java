package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.AuthorResultDto;

import java.util.List;

public interface LibrarySearchService {

    List<AuthorResultDto> searchAuthors(String authorName) throws RrBadRequestException;
}
