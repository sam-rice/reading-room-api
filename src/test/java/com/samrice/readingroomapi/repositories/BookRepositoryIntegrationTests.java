package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.Book;
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
public class BookRepositoryIntegrationTests {

    private BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository bookRepository) {
        this.underTest = bookRepository;
    }

    @Test
    public void testThatBookCanBeCreatedAndQueried() {
        Integer bookId = underTest.createBook(3, 2, "0000000000000", "OL1234567W", "Book Title", "Author Name", "Something about this book.");
        Book newBook = underTest.findBookById(2, 3, bookId);
        assertEquals(bookId, newBook.bookId());
        assertEquals(3, newBook.shelfId());
        assertEquals(2, newBook.userId());
        assertEquals("0000000000000", newBook.isbn());
        assertEquals("OL1234567W", newBook.olKey());
        assertEquals("Book Title", newBook.title());
        assertEquals("Author Name", newBook.author());
        assertEquals("Something about this book.", newBook.userNote());
    }

    @Test
    public void testThatInvalidBookCreationDetailsThrowsBadRequestException() {
        assertThrows(RrBadRequestException.class, () -> {
            underTest.createBook(3, 2, "0000000000000", "OL1234567W", null, "Author Name", "Something about this book.");
        });
    }

    @Test
    public void testThatExistingBookCanBeQueried() {
        Book retrievedBook = underTest.findBookById(2, 3, 10_003);
        assertEquals(10_003, retrievedBook.bookId());
        assertEquals(3, retrievedBook.shelfId());
        assertEquals(2, retrievedBook.userId());
        assertEquals("9780470868089", retrievedBook.isbn());
        assertEquals("OL8208787W", retrievedBook.olKey());
        assertEquals("Philip Webb: Pioneer of Arts & Crafts Architecture", retrievedBook.title());
        assertEquals("Sheila Kirk", retrievedBook.author());
        assertEquals("A great resource for info on the red house.", retrievedBook.userNote());
        assertEquals(1704341375550L, retrievedBook.savedDate());
    }

    @Test
    public void testThatMissingBookThrowsResourceNotFoundException() {
        assertThrows(RrResourceNotFoundException.class, () -> {
            underTest.findBookById(2, 3, 100_000);
        });
    }

    @Test
    public void testThatAllBooksCanBeQueriedByShelfId() {
        List<Book> books = underTest.findAllBooksByShelfId(2, 4);
        assertEquals(books.size(), 2);
        assertEquals(books.get(0).bookId(), 10_006);
        assertEquals(books.get(1).bookId(), 10_007);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        Book changedBook = new Book(10_005, 3, 2, "9781899858286", "OL820369W", "Bath", "Thom Gorst", "Updated user note.", 1704341381405L);
        underTest.updateBook(2, 3, 10_005, changedBook);
        Book retrievedBook = underTest.findBookById(2, 3, 10_005);
        assertEquals(10_005, retrievedBook.bookId());
        assertEquals(3, retrievedBook.shelfId());
        assertEquals(2, retrievedBook.userId());
        assertEquals("9781899858286", retrievedBook.isbn());
        assertEquals("OL820369W", retrievedBook.olKey());
        assertEquals("Bath", retrievedBook.title());
        assertEquals("Thom Gorst", retrievedBook.author());
        assertEquals("Updated user note.", retrievedBook.userNote());
        assertEquals(1704341381405L, retrievedBook.savedDate());
    }

    @Test
    public void testThatBookCanBeDeleted() {
        underTest.deleteBook(1, 1, 10_000);
        List<Book> returnedBooks = underTest.findAllBooksByShelfId(1, 1);
        assertEquals(returnedBooks.size(), 1);
    }
}