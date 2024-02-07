package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.dtos.SearchedAuthorDto;

import java.util.List;

public interface LibrarySearchService {

    List<SearchedAuthorDto> searchAuthors(String authorName, Integer pageNo, Integer pageSize) throws RrBadRequestException;
}
