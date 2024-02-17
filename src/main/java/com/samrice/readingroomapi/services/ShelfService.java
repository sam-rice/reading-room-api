package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domains.Shelf;
import com.samrice.readingroomapi.dtos.ShelfDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface ShelfService {
    List<ShelfDto> fetchAllShelvesByUser(Integer userId);

    ShelfDto fetchShelfById(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

    ShelfDto addShelf(Integer userId, String title, String description) throws RrBadRequestException;

    void removeShelfWithAllBooks(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

    void updateShelf(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException;
}
