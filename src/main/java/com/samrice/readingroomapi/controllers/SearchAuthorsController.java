package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.dtos.AuthorDetailsDto;
import com.samrice.readingroomapi.dtos.AuthorResultsPageDto;
import com.samrice.readingroomapi.services.SearchAuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/authors")
public class SearchAuthorsController {

    @Autowired
    SearchAuthorsService searchAuthorsService;

    @GetMapping("")
    public ResponseEntity<AuthorResultsPageDto> searchAuthors(@RequestParam(value = "q") String query,
                                                               @RequestParam(value = "size") int pageSize,
                                                               @RequestParam(value = "page") int pageNum) {
        AuthorResultsPageDto authors = searchAuthorsService.searchAuthors(query, pageSize, pageNum);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/{libraryKey}")
    public ResponseEntity<AuthorDetailsDto> getAuthor(@PathVariable("libraryKey") String libraryKey) {
        AuthorDetailsDto author = searchAuthorsService.getAuthor(libraryKey);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }
}
