package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.samrice.readingroomapi.domains.Author;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.AuthorDetailsPojo;
import com.samrice.readingroomapi.pojos.openlibraryresponses.WorkPojo;
import com.samrice.readingroomapi.repositories.BookRepository;
import com.samrice.readingroomapi.utilities.Json;
import com.samrice.readingroomapi.utilities.OpenLibraryCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.samrice.readingroomapi.Constants;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Book> fetchAllBooksByShelf(Integer userId, Integer shelfId) {
        return bookRepository.findAllBooksByShelfId(userId, shelfId);
    }

    @Override
    public Book fetchBookById(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException {
        return bookRepository.findBookById(userId, shelfId, bookId);
    }

    @Override
    public Book addBook(Integer shelfId, Integer userId, String key, String userNote) throws RrBadRequestException {
        try {
            BookResult bookResult = getBookResult(key);
            int bookId = bookRepository.createBook(shelfId, userId, key, bookResult.isbn(), bookResult.bookTitle(), bookResult.authorsList(), userNote);
            return bookRepository.findBookById(userId, shelfId, bookId);
        } catch (Exception e) {
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

    private record BookResult(String bookTitle, List<Author> authorsList, String isbn) {
    }

    private BookResult getBookResult(String key) throws JsonProcessingException {
        String bookEndpoint = Constants.OPEN_LIBRARY_WORKS_BASE_URL + "/" + key + ".json";
        ResponseEntity<String> bookResponse = restTemplate.getForEntity(bookEndpoint, String.class);
        JsonNode root = Json.parse(bookResponse.getBody());
        WorkPojo workResult = Json.fromJson(root, WorkPojo.class);
        String formattedIsbn = OpenLibraryCleaner.formatIsbn(workResult.isbn_13());
        List<String> authorKeys = workResult.authors().stream().map(a -> a.author().get("key")).toList();
        List<Author> authors = getBasicInfoForAllAuthors(authorKeys);
        return new BookResult(workResult.title(), authors, formattedIsbn);
    }

    private List<Author> getBasicInfoForAllAuthors(List<String> authorKeys) {
        List<Author> authors = new ArrayList<>();
        if (authorKeys != null && !authorKeys.isEmpty()) {
            for (String key : authorKeys) {
                try {
                    String formattedKey = OpenLibraryCleaner.formatKey(key);
                    String endpoint = Constants.OPEN_LIBRARY_BASE_URL + key + ".json";
                    ResponseEntity<String> authorResponse = restTemplate.getForEntity(endpoint, String.class);
                    if (authorResponse.getStatusCode().is2xxSuccessful()) {
                        JsonNode root = Json.parse(authorResponse.getBody());
                        AuthorDetailsPojo author = Json.fromJson(root, AuthorDetailsPojo.class);
                        authors.add(new Author(author.name(), formattedKey));
                    } else {
                        authors.add(new Author(null, formattedKey));
                    }
                } catch (Exception e) {
                    throw new RrBadRequestException("Something went wrong. Failed to get author details.");
                }
            }
        }
        return authors;
    }
}
