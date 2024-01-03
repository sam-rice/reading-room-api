package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.Shelf;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface ShelfRepository {

    List<Shelf> findAllShelvesByUserId(Integer userId) throws RrResourceNotFoundException;

    Shelf findShelfById(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

    Integer createShelf(Integer userId, String title, String description) throws RrBadRequestException;

    void updateShelf(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException;
    void deleteShelf(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

}
