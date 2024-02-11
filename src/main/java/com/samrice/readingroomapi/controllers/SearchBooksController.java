package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.dtos.BookDetailsDto;
import com.samrice.readingroomapi.dtos.BookResultDto;
import com.samrice.readingroomapi.services.SearchBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/books")
public class SearchBooksController {

    @Autowired
    SearchBooksService searchBooksService;

    @GetMapping("")
    public ResponseEntity<List<BookResultDto>> searchBooks(@RequestParam(value = "q") String query) {
        List<BookResultDto> bookResults = searchBooksService.searchBooks(query);
        return new ResponseEntity<>(bookResults, HttpStatus.OK);
    }

    @GetMapping("/{key}")
    public ResponseEntity<BookDetailsDto> getBook(@PathVariable("key") String bookKey) {
        BookDetailsDto book = searchBooksService.getBook(bookKey);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
