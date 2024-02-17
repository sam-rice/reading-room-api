package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.domains.Shelf;
import com.samrice.readingroomapi.dtos.ShelfDto;
import com.samrice.readingroomapi.services.ShelfService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shelves")
public class ShelfController {

    @Autowired
    ShelfService shelfService;

    @GetMapping("")
    public ResponseEntity<List<ShelfDto>> getAllShelves(HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");
        List<ShelfDto> allShelves = shelfService.fetchAllShelvesByUser(userId);
        return new ResponseEntity<>(allShelves, HttpStatus.OK);
    }

    @GetMapping("/{shelfId}")
    public ResponseEntity<ShelfDto> getShelfById(HttpServletRequest request,
                                              @PathVariable("shelfId") Integer shelfId) {
        int userId = (Integer) request.getAttribute("userId");
        ShelfDto shelf = shelfService.fetchShelfById(userId, shelfId);
        return new ResponseEntity<>(shelf, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ShelfDto> addShelf(HttpServletRequest request,
                                          @RequestBody Map<String, Object> shelfMap) {
        int userId = (Integer) request.getAttribute("userId");
        String title = (String) shelfMap.get("title");
        String description = (String) shelfMap.get("description");
        ShelfDto shelf = shelfService.addShelf(userId, title, description);
        return new ResponseEntity<>(shelf, HttpStatus.CREATED);
    }

    @PutMapping("/{shelfId}")
    public ResponseEntity<Map<String, Boolean>> updateShelf(HttpServletRequest request,
                                                           @PathVariable("shelfId") Integer shelfId,
                                                           @RequestBody Shelf shelf) {
        int userId = (Integer) request.getAttribute("userId");
        shelfService.updateShelf(userId, shelfId, shelf);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{shelfId}")
    public ResponseEntity<Map<String, Boolean>> removeShelf(HttpServletRequest request,
                                                            @PathVariable("shelfId") Integer shelfId) {
        int userId = (Integer) request.getAttribute("userId");
        shelfService.removeShelfWithAllBooks(userId, shelfId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
