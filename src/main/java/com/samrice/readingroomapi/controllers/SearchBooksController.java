package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultsPageDto;
import com.samrice.readingroomapi.services.SearchBooksService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/books")
public class SearchBooksController {

    @Autowired
    SearchBooksService searchBooksService;

    @GetMapping("")
    public ResponseEntity<BookResultsPageDto> searchBooks(@RequestParam(value = "q") String query,
                                                          @RequestParam(value = "size") int pageSize,
                                                          @RequestParam(value = "page") int pageNum) {
        BookResultsPageDto bookResults = searchBooksService.searchBooks(query, pageSize, pageNum);
        return new ResponseEntity<>(bookResults, HttpStatus.OK);
    }

    @GetMapping("/{libraryKey}")
    public ResponseEntity<BookDetailsDto> getBookDetails(HttpServletRequest request,
                                                         @PathVariable("libraryKey") String libraryKey) {
        int userId = (int) request.getAttribute("userId");
        BookDetailsDto book = searchBooksService.getBook(userId, libraryKey);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
