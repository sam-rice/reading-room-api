package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samrice.readingroomapi.domain.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.repositories.BookRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.samrice.readingroomapi.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Book addBook(Integer shelfId, Integer userId, String isbn, String userNote) throws RrBadRequestException {
        try {
            String formattedIsbn = isbn.replace("-", "").replace("—", "");
            BookResult bookResult = getBookResult(formattedIsbn);
            int bookId = bookRepository.createBook(shelfId, userId, formattedIsbn, bookResult.bookTitle(), bookResult.authorName(), userNote);
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

    private record BookResult(String bookTitle, String authorName) {
    }

    private BookResult getBookResult(String isbn) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String bookEndpoint = Constants.OPEN_LIBRARY_API_BOOKS_BASE_URL + "/" + isbn + ".json";
        ResponseEntity<String> bookResponse = restTemplate.getForEntity(bookEndpoint, String.class);
        JsonNode bookRoot = mapper.readTree(bookResponse.getBody());
        String bookTitle = mapper.readTree(bookResponse.getBody()).get("title").asText();
        List<Object> authorsList = mapper.convertValue(bookRoot.get("authors"), List.class);
        String authorName = getAuthorName(bookRoot, authorsList);
        return new BookResult(bookTitle, authorName);
    }

    private String getAuthorName(JsonNode bookRoot, List<Object> authorsList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String authorName = null;
        if (authorsList != null && !authorsList.isEmpty()) {
            String authorKey = bookRoot.get("authors").get(0).get("key").asText();
            String authorEndpoint = Constants.OPEN_LIBRARY_API_BASE_URL + authorKey + ".json";
            ResponseEntity<String> authorResponse = restTemplate.getForEntity(authorEndpoint, String.class);
            JsonNode personalNameField = mapper.readTree(authorResponse.getBody()).get("personal_name");
            JsonNode nameField = mapper.readTree(authorResponse.getBody()).get("name");
            authorName = personalNameField != null ? personalNameField.asText() : nameField != null ? nameField.asText() : null;
        }
        return authorName;
    }
}
