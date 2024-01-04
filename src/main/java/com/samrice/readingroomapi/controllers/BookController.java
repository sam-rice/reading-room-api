package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.domain.Book;
import com.samrice.readingroomapi.services.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shelves/{shelfId}/books")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("")
    public ResponseEntity<Book> addBook(HttpServletRequest request,
                                        @PathVariable("shelfId") Integer shelfId,
                                        @RequestBody Map<String, Object> bookMap) {
        int userId = (Integer) request.getAttribute("userId");
        String isbn = (String) bookMap.get("isbn");
        String olKey = (String) bookMap.get("olKey");
        String title = (String) bookMap.get("title");
        String author = (String) bookMap.get("author");
        String userNote = (String) bookMap.get("userNote");
        Book book = bookService.addBook(shelfId, userId, isbn, olKey, title, author, userNote);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }
}
