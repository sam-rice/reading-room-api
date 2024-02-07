package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.domains.Shelf;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ShelfRepositoryIntegrationTests {

    private ShelfRepository underTest;
    private BookRepository bookRepository;

    @Autowired
    public ShelfRepositoryIntegrationTests(ShelfRepository underTest, BookRepository bookRepository) {
        this.underTest = underTest;
        this.bookRepository = bookRepository;
    }

    @Test
    public void testThatShelfCanBeCreatedAndQueried() {
        Integer newShelfId = underTest.createShelf(2, "Jimmy's New Shelf", "A description.");
        assertNotNull(newShelfId);
        assertNotEquals(0, newShelfId);
        Shelf retrievedShelf = underTest.findShelfById(2, newShelfId);
        assertEquals(newShelfId, retrievedShelf.shelfId());
        assertEquals(2, retrievedShelf.userId());
        assertEquals("Jimmy's New Shelf", retrievedShelf.title());
        assertEquals("A description.", retrievedShelf.description());
        assertEquals(0, retrievedShelf.totalSavedBooks());
    }

    @Test
    public void testThatInvalidShelfCreationDetailsThrowsBadRequestException() {
        assertThrows(RrBadRequestException.class, () -> {
            underTest.createShelf(2, null, "A description.");
        });
    }

    @Test
    public void testThatExistingShelfCanBeQueried() {
        Shelf retrievedShelf = underTest.findShelfById(2, 3);
        List<Book> relatedBooks = bookRepository.findAllBooksByShelfId(2, 3);
        assertEquals(3, retrievedShelf.shelfId());
        assertEquals(2, retrievedShelf.userId());
        assertEquals("English & Misc. Architecture", retrievedShelf.title());
        assertEquals("Books on historical architecture of the British Isles.", retrievedShelf.description());
        assertEquals(3, retrievedShelf.totalSavedBooks());
        assertEquals(relatedBooks.size(), retrievedShelf.totalSavedBooks());
    }

    @Test
    public void testThatMissingShelfThrowsResourceNotFoundException() {
        assertThrows(RrResourceNotFoundException.class, () -> {
            underTest.findShelfById(2, 100);
        });
    }

    @Test
    public void testThatAllShelvesByUserCanBeQueried() {
        List<Shelf> shelves = underTest.findAllShelvesByUserId(3);
        assertEquals(shelves.size(), 1);
        Shelf shelf = shelves.get(0);
        assertEquals(6, shelf.shelfId());
        assertEquals(3, shelf.userId());
        assertEquals("Drinking, Farming, and Farm Equipment", shelf.title());
        assertEquals("All the books I own.", shelf.description());
        assertEquals(1, shelf.totalSavedBooks());
    }

    @Test
    public void testThatShelfCanBeUpdated() {
        Shelf newShelf = new Shelf(4, 2, "Updated Title", "Updated description.", 3);
        underTest.updateShelf(2, 4, newShelf);
        Shelf returned = underTest.findShelfById(2, 4);
        assertEquals(4, returned.shelfId());
        assertEquals(2, returned.userId());
        assertEquals("Updated Title", returned.title());
        assertEquals("Updated description.", returned.description());
        assertEquals(2, returned.totalSavedBooks());
    }

    @Test
    public void testInvalidShelfUpdateDetailsThrowsBadRequestException() {
        assertThrows(RrBadRequestException.class, () -> {
            Shelf invalidShelf = new Shelf(4, 2, null, "Updated description.", 3);
            underTest.updateShelf(2, 4, invalidShelf);
        });
    }

    @Test
    public void testThatShelfCanBeDeletedWithAllItsBooks() {
        underTest.deleteShelfWithAllBooks(2, 2);
        List<Book> remainingBooks = bookRepository.findAllBooksByShelfId(2, 2);
        assertEquals(remainingBooks.size(), 0);
    }
}