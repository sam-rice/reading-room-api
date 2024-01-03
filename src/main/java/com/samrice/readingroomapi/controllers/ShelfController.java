package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.domain.Shelf;
import com.samrice.readingroomapi.services.ShelfService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shelves")
public class ShelfController {

    @Autowired
    ShelfService shelfService;

//    @GetMapping("")
//    public ResponseEntity<List<Shelf>> getAllShelves(HttpServletRequest request) {
//        int userId = (Integer) request.getAttribute("userId");
//        return shelfService.fetchAllShelvesByUser(userId);
//    }

//    @PostMapping("")
//    public ResponseEntity<Shelf> getShelfById(HttpServletRequest request,
//                                              @RequestBody Map<String, Object> shelfMap) {
//        int userId = (Integer) request.getAttribute("userId");
//        int shelfId = (Integer) shelfMap.get("shelfId");
//        return shelfService.fetchShelfById(userId, shelfId);
//    }

    @PostMapping("")
    public ResponseEntity<Shelf> addShelf(HttpServletRequest request,
                                          @RequestBody Map<String, Object> shelfMap) {
        int userId = (Integer) request.getAttribute("userId");
        String title = (String) shelfMap.get("title");
        String description = (String) shelfMap.get("description");
        Shelf shelf = shelfService.addShelf(userId, title, description);
        return new ResponseEntity<>(shelf, HttpStatus.CREATED);
    }
}
