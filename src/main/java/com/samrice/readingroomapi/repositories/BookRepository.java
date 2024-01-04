package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface BookRepository {

    List<Book> findAllBooksByShelfId(Integer userId, Integer shelfId);

    Book findBookById(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException;

    Integer addBook(Integer shelfId, Integer userId, String isbn, String olKey, String title, String author, String userNote) throws RrBadRequestException;

    void updateBook(Integer userId, Integer shelfId, Integer bookId, Book book) throws RrBadRequestException;

    void deleteBook(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException;
}
