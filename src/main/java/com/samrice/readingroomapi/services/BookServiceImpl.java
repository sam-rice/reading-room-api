package com.samrice.readingroomapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.pojos.IndividualAuthorPojo;
import com.samrice.readingroomapi.pojos.OpenLibraryWork;
import com.samrice.readingroomapi.repositories.BookRepository;
import com.samrice.readingroomapi.utilities.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.samrice.readingroomapi.Constants;

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
            int bookId = bookRepository.createBook(shelfId, userId, key, bookResult.isbn(), bookResult.bookTitle(), bookResult.authorName(), userNote);
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

    private record BookResult(String bookTitle, String authorName, String isbn) {
    }

    private BookResult getBookResult(String key) throws JsonProcessingException {
        String bookEndpoint = Constants.OPEN_LIBRARY_WORKS_BASE_URL + "/" + key + ".json";
        ResponseEntity<String> bookResponse = restTemplate.getForEntity(bookEndpoint, String.class);
        JsonNode root = Json.parse(bookResponse.getBody());
        OpenLibraryWork workResult = Json.fromJson(root, OpenLibraryWork.class);
        String formattedIsbn = !workResult.isbn_13().isEmpty() ? workResult.isbn_13().get(0).replace("-", "").replace("—", "") : null;

//        List<Object> authorsList = mapper.convertValue(bookRoot.get("authors"), List.class);
        //remove authorsList param
        String authorName = getAuthorName(workResult.key(), workResult.authors());
        return new BookResult(workResult.title(), authorName, formattedIsbn);
    }

    private String getAuthorName(String key, List<IndividualAuthorPojo> authorsList) throws JsonProcessingException {
        String authorName = null;
        if (authorsList != null && !authorsList.isEmpty()) {
            String authorEndpoint = Constants.OPEN_LIBRARY_BASE_URL + key + ".json";
            ResponseEntity<String> authorResponse = restTemplate.getForEntity(authorEndpoint, String.class);
            JsonNode authorRoot = Json.parse(authorResponse.getBody());
            JsonNode personalNameField = authorRoot.get("personal_name");
            JsonNode nameField = authorRoot.get("name");
            authorName = personalNameField != null ? personalNameField.asText() : nameField != null ? nameField.asText() : null;
        }
        return authorName;
    }
}
