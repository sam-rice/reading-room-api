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
        assertEquals(bookId, newBook.getBookId());
        assertEquals(3, newBook.getShelfId());
        assertEquals(2, newBook.getUserId());
        assertEquals("0000000000000", newBook.getIsbn());
        assertEquals("OL1234567W", newBook.getOlKey());
        assertEquals("Book Title", newBook.getTitle());
        assertEquals("Author Name", newBook.getAuthor());
        assertEquals("Something about this book.", newBook.getUserNote());
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
        assertEquals(10_003, retrievedBook.getBookId());
        assertEquals(3, retrievedBook.getShelfId());
        assertEquals(2, retrievedBook.getUserId());
        assertEquals("9780470868089", retrievedBook.getIsbn());
        assertEquals("OL8208787W", retrievedBook.getOlKey());
        assertEquals("Philip Webb: Pioneer of Arts & Crafts Architecture", retrievedBook.getTitle());
        assertEquals("Sheila Kirk", retrievedBook.getAuthor());
        assertEquals("A great resource for info on the red house.", retrievedBook.getUserNote());
        assertEquals(1704341375550L, retrievedBook.getSavedDate());
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
        assertEquals(books.get(0).getBookId(), 10_006);
        assertEquals(books.get(1).getBookId(), 10_007);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        Book changedBook = new Book(10_005, 3, 2, "9780300101775", null, "Bath", "Michael Forsyth", "Updated user note.", 1704341387500L);
        underTest.updateBook(2, 3, 10_005, changedBook);
        Book retrievedBook = underTest.findBookById(2, 3, 10_005);
        assertEquals(10_005, retrievedBook.getBookId());
        assertEquals(3, retrievedBook.getShelfId());
        assertEquals(2, retrievedBook.getUserId());
        assertEquals("9780300101775", retrievedBook.getIsbn());
        assertNull(retrievedBook.getOlKey());
        assertEquals("Bath", retrievedBook.getTitle());
        assertEquals("Michael Forsyth", retrievedBook.getAuthor());
        assertEquals("Updated user note.", retrievedBook.getUserNote());
        assertEquals(1704341387500L, retrievedBook.getSavedDate());
    }

    @Test
    public void testThatBookCanBeDeleted() {
        underTest.deleteBook(1, 1, 10_000);
        List<Book> returnedBooks = underTest.findAllBooksByShelfId(1, 1);
        assertEquals(returnedBooks.size(), 1);
    }
}