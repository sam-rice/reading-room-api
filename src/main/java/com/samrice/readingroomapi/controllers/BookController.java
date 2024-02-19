package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.services.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
        String libraryKey = (String) bookMap.get("libraryKey");
        String userNote = (String) bookMap.get("userNote");
        Book book = bookService.addBook(shelfId, userId, libraryKey, userNote);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(HttpServletRequest request,
                                            @PathVariable("shelfId") Integer shelfId,
                                            @PathVariable("bookId") Integer bookId) {
        int userId = (Integer) request.getAttribute("userId");
        Book book = bookService.fetchBookById(userId, shelfId, bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Book>> getAllBooksFromShelf(HttpServletRequest request,
                                                           @PathVariable("shelfId") Integer shelfId) {
        int userId = (Integer) request.getAttribute("userId");
        List<Book> books = bookService.fetchAllBooksByShelf(userId, shelfId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Map<String, Boolean>> updateBook(HttpServletRequest request,
                                                           @PathVariable("shelfId") Integer shelfId,
                                                           @PathVariable("bookId") Integer bookId,
                                                           @RequestBody Book book) {
        int userId = (Integer) request.getAttribute("userId");
        bookService.updateBook(userId, shelfId, bookId, book);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Map<String, Boolean>> removeBook(HttpServletRequest request,
                                                           @PathVariable("shelfId") Integer shelfId,
                                                           @PathVariable("bookId") Integer bookId) {
        int userId = (Integer) request.getAttribute("userId");
        bookService.removeBook(userId, shelfId, bookId);
        Map<String, Boolean> map = new HashMap();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
