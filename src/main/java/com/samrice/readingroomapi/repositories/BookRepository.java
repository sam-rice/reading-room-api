package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface BookRepository {

    List<Book> findAllBooksByShelfId(Integer userId, Integer shelfId);

    Book findBookById(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException;

    Integer createBook(Integer shelfId, Integer userId, String libraryKey, String title, List<BasicAuthor> authorsList, String coverUrl, String userNote) throws RrBadRequestException;

    void updateBook(Integer userId, Integer shelfId, Integer bookId, Book book) throws RrBadRequestException;

    void deleteBook(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException;

    Book findFirstSavedBookOnShelf(Integer userId, Integer shelfId) throws RrResourceNotFoundException;
}
