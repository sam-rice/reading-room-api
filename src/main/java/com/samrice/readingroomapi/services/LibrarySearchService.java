package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.librarysearchresponses.SearchedAuthor;

import java.util.List;

public interface LibrarySearchService {

    List<SearchedAuthor> searchAuthors(String authorName) throws RrBadRequestException;
}
