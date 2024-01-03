package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.Shelf;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;

import java.util.List;

public interface ShelfRepository {

    List<Shelf> findAll(Integer userId) throws RrResourceNotFoundException;

    Shelf findById(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

    Integer create(Integer userId, String title, String description) throws RrBadRequestException;

    void update(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException;
    void delete(Integer userId, Integer shelfId) throws RrResourceNotFoundException;

}
