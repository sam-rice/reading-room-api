package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.dtos.AuthorResultDto;
import com.samrice.readingroomapi.services.LibrarySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class LibrarySearchController {

    @Autowired
    LibrarySearchService librarySearchService;

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResultDto>> searchAuthors(@RequestParam(value = "q") String authorName) {
        List<AuthorResultDto> authors = librarySearchService.searchAuthors(authorName);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

}
