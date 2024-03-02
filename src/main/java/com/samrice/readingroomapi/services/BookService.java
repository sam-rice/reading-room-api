package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface BookService {

    Book fetchBookById(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException;

    Book addBook(Integer shelfId, Integer userId, String libraryKey) throws RrBadRequestException;

    void updateBook(Integer userId, Integer shelfId, Integer bookId, Book book) throws RrBadRequestException;

    void removeBook(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException;
}
