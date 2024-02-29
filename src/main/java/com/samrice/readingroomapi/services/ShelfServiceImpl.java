package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.domains.Shelf;
import com.samrice.readingroomapi.dtos.ShelfDetailsDto;
import com.samrice.readingroomapi.dtos.ShelfDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.repositories.BookRepository;
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

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<ShelfDto> fetchAllShelvesByUser(Integer userId) {
        List<Shelf> shelves = shelfRepository.findAllShelvesByUserId(userId);
        return shelves.stream().map(s -> {
            Book firstSavedBook = bookRepository.findFirstSavedBookWithCoverOnShelf(s.userId(), s.shelfId());
            return new ShelfDto(s.shelfId(),
                    s.userId(),
                    s.title(),
                    s.description(),
                    s.totalSavedBooks(),
                    firstSavedBook != null ? firstSavedBook.coverUrl() : null);
        }).toList();
    }

    @Override
    public ShelfDetailsDto fetchShelfById(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        Shelf shelf = shelfRepository.findShelfById(userId, shelfId);
        List<Book> books = bookRepository.findAllBooksByShelfId(userId, shelfId);
        Book firstSavedBook = bookRepository.findFirstSavedBookWithCoverOnShelf(userId, shelfId);
        return new ShelfDetailsDto(shelf.shelfId(),
                shelf.userId(),
                shelf.title(),
                shelf.description(),
                shelf.totalSavedBooks(),
                firstSavedBook != null ? firstSavedBook.coverUrl() : null,
                books);
    }

    @Override
    public ShelfDto addShelf(Integer userId, String title, String description) throws RrBadRequestException {
        int shelfId = shelfRepository.createShelf(userId, title, description);
        Shelf shelf = shelfRepository.findShelfById(userId, shelfId);
        return new ShelfDto(shelf.shelfId(),
                shelf.userId(),
                shelf.title(),
                shelf.description(),
                shelf.totalSavedBooks(),
                null);
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
