package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domains.Shelf;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.repositories.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    ShelfRepository shelfRepository;

    @Override
    public List<Shelf> fetchAllShelvesByUser(Integer userId) {
        return shelfRepository.findAllShelvesByUserId(userId);
    }

    @Override
    public Shelf fetchShelfById(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        return shelfRepository.findShelfById(userId, shelfId);
    }

    @Override
    public Shelf addShelf(Integer userId, String title, String description) throws RrBadRequestException {
        int shelfId = shelfRepository.createShelf(userId, title, description);
        return shelfRepository.findShelfById(userId, shelfId);
    }

    @Override
    public void removeShelfWithAllBooks(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        shelfRepository.findShelfById(userId, shelfId);
        shelfRepository.deleteShelfWithAllBooks(userId, shelfId);
    }

    @Override
    public void updateShelf(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException {
        shelfRepository.updateShelf(userId, shelfId, shelf);
    }
}
