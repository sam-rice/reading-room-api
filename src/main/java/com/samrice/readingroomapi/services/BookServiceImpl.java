package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.AuthorWorkPojo;
import com.samrice.readingroomapi.repositories.BookRepository;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book fetchBookById(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException {
        return bookRepository.findBookById(userId, shelfId, bookId);
    }

    @Override
    public Book addBook(Integer shelfId, Integer userId, String libraryKey) throws RrBadRequestException {
        try {
            BookResult bookResult = getBookResult(libraryKey);
            int bookId = bookRepository.createBook(shelfId,
                    userId,
                    libraryKey,
                    bookResult.bookTitle(),
                    bookResult.authorsList(),
                    bookResult.coverUrl(),
                    "");
            return bookRepository.findBookById(userId, shelfId, bookId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Something went wrong. Could not find additional book info.");
        }
    }

    @Override
    public void updateBook(Integer userId, Integer shelfId, Integer bookId, Book book) throws RrBadRequestException {
        bookRepository.updateBook(userId, shelfId, bookId, book);
    }

    @Override
    public void removeBook(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException {
        bookRepository.deleteBook(userId, shelfId, bookId);
    }

    private record BookResult(String bookTitle, List<BasicAuthor> authorsList, String coverUrl) {
    }

    private BookResult getBookResult(String libraryKey) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.WORKS_BASE_URL + "/" + libraryKey + ".json";
        AuthorWorkPojo workResult = OpenLibraryUtils.getPojoFromEndpoint(endpoint, AuthorWorkPojo.class);
        String coverUrl = OpenLibraryUtils.getPhotoUrl(workResult.covers());
        List<BasicAuthor> authors = OpenLibraryUtils.getBasicInfoForAllAuthors(workResult.authors());
        return new BookResult(workResult.title(), authors, coverUrl);
    }

}

