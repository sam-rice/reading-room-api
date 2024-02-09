package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.AuthorResultDto;
import com.samrice.readingroomapi.services.SearchAuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/authors")
public class SearchAuthorsController {

    @Autowired
    SearchAuthorsService librarySearchService;

    @GetMapping("")
    public ResponseEntity<List<AuthorResultDto>> searchAuthors(@RequestParam(value = "q") String authorName) {
        List<AuthorResultDto> authors = librarySearchService.searchAuthors(authorName);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/{key}")
    public ResponseEntity<AuthorDetailsDto> getAuthor(@PathVariable("key") String authorKey) {
        AuthorDetailsDto author = librarySearchService.getAuthor(authorKey);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }
}
