package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domain.Shelf;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface ShelfService {
    List<Shelf> fetchAllShelvesByUser(Integer userId);

    Shelf fetchShelfById(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

    Shelf addShelf(Integer userId, String title, String description) throws RrBadRequestException;

    void removeShelf(Integer userId, Integer shelfId) throws RrResourceNotFoundException;
    void updateShelf(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException;

}
