package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.pojos.openlibraryresponses.AuthorDetailsPojo;
import com.samrice.readingroomapi.pojos.openlibraryresponses.WorkPojo;
import com.samrice.readingroomapi.repositories.BookRepository;
import com.samrice.readingroomapi.utilities.OpenLibraryUtils;
import com.samrice.readingroomapi.utilities.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
            int bookId = bookRepository.createBook(shelfId, userId, key, bookResult.bookTitle(), bookResult.authorsList(), bookResult.coverUrl(), userNote);
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

    private BookResult getBookResult(String key) throws JsonProcessingException {
        String endpoint = OpenLibraryUtils.WORKS_BASE_URL + "/" + key + ".json";
        WorkPojo workResult = OpenLibraryUtils.getPojoFromEndpoint(endpoint, WorkPojo.class);
        String coverUrl = workResult.covers() != null && !workResult.covers().isEmpty() ?
                "https://covers.openlibrary.org/b/id/" + workResult.covers().get(0) + "-L.jpg"
                : null;
        List<String> authorKeys = workResult.authors().stream().map(a -> a.author().get("key")).toList();
        List<BasicAuthor> authors = getBasicInfoForAllAuthors(authorKeys);
        return new BookResult(workResult.title(), authors, coverUrl);
    }

    private List<BasicAuthor> getBasicInfoForAllAuthors(List<String> authorKeys) {
        List<BasicAuthor> authors = new ArrayList<>();
        if (authorKeys != null && !authorKeys.isEmpty()) {
            for (String key : authorKeys) {
                try {
                    String formattedKey = OpenLibraryUtils.formatKey(key);
                    String endpoint = OpenLibraryUtils.BASE_URL + key + ".json";
                    ResponseEntity<String> authorResponse = restTemplate.getForEntity(endpoint, String.class);
                    if (authorResponse.getStatusCode().is2xxSuccessful()) {
                        JsonNode root = Json.parse(authorResponse.getBody());
                        AuthorDetailsPojo author = Json.fromJson(root, AuthorDetailsPojo.class);
                        authors.add(new BasicAuthor(author.name(), formattedKey));
                    } else {
                        authors.add(new BasicAuthor(null, formattedKey));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RrBadRequestException("Something went wrong. Failed to get author details.");
                }
            }
        }
        return authors;
    }
}

