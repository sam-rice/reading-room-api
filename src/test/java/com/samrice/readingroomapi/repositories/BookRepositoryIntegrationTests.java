package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.dtos.AssociatedShelfDto;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<BasicAuthor> exampleAuthorList = List.of(new BasicAuthor("Author Name", "OL654321"));
        Integer bookId = underTest.createBook(3,
                2,
                "OL123456",
                "Book Title",
                exampleAuthorList,
                "https://www.example.com/cover-url.jpg",
                "Something about this book.");
        Book newBook = underTest.findBookById(2, 3, bookId);

        assertEquals(bookId, newBook.bookId());
        assertEquals(3, newBook.shelfId());
        assertEquals(2, newBook.userId());
        assertEquals("OL123456", newBook.libraryKey());
        assertEquals("Book Title", newBook.title());
        assertEquals(exampleAuthorList, newBook.authors());
        assertEquals("https://www.example.com/cover-url.jpg", newBook.coverUrl());
        assertEquals("Something about this book.", newBook.userNote());
    }

    @Test
    public void testThatInvalidBookCreationDetailsThrowsBadRequestException() {
        assertThrows(RrBadRequestException.class, () -> {
            underTest.createBook(3,
                    2,
                    "OL123456",
                    null,
                    null,
                    "cover url",
                    "Something about this book.");
        });
    }

    @Test
    public void testThatExistingBookCanBeQueried() {
        List<BasicAuthor> authorList = List.of(new BasicAuthor("Sheila Kirk", "OL2732304A"));
        Book retrievedBook = underTest.findBookById(2, 3, 10_003);

        assertEquals(10_003, retrievedBook.bookId());
        assertEquals(3, retrievedBook.shelfId());
        assertEquals(2, retrievedBook.userId());
        assertEquals("OL8208787W", retrievedBook.libraryKey());
        assertEquals("Philip Webb: Pioneer of Arts & Crafts Architecture", retrievedBook.title());
        assertEquals(authorList, retrievedBook.authors());
        assertEquals("https://covers.openlibrary.org/b/id/300234-L.jpg", retrievedBook.coverUrl());
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
        List<BasicAuthor> authorList = List.of(new BasicAuthor("Thom Gorst", "OL69317A"));
        Book updatedBook = new Book(10_005,
                3,
                2,
                "OL820369W",
                "Bath",
                authorList,
                "https://covers.openlibrary.org/b/id/946298-L.jpg",
                "Updated user note.",
                1704341381405L);
        underTest.updateBook(2, 3, 10_005, updatedBook);
        Book retrievedBook = underTest.findBookById(2, 3, 10_005);

        assertEquals(10_005, retrievedBook.bookId());
        assertEquals(3, retrievedBook.shelfId());
        assertEquals(2, retrievedBook.userId());
        assertEquals("OL820369W", retrievedBook.libraryKey());
        assertEquals("Bath", retrievedBook.title());
        assertEquals(authorList, retrievedBook.authors());
        assertEquals("https://covers.openlibrary.org/b/id/946298-L.jpg", retrievedBook.coverUrl());
        assertEquals("Updated user note.", retrievedBook.userNote());
        assertEquals(1704341381405L, retrievedBook.savedDate());
    }

    @Test
    public void testThatBookCanBeDeleted() {
        underTest.deleteBook(1, 1, 10_000);
        List<Book> returnedBooks = underTest.findAllBooksByShelfId(1, 1);

        assertEquals(returnedBooks.size(), 1);
    }

    @Test
    public void testThatFirstBookWithCoverUrlCanBeQueried() {
        Book bookWithCover = underTest.findFirstSavedBookWithCoverOnShelf(2, 4);
        assertEquals(10_007, bookWithCover.bookId());
        assertEquals("https://covers.openlibrary.org/b/id/13344966-L.jpg", bookWithCover.coverUrl());

        Book nonexistentBook = underTest.findFirstSavedBookWithCoverOnShelf(3, 6);
        assertNull(nonexistentBook);
    }

    @Test
    public void testThatAssociatedShelvesCanBeFoundForAnOpenLibraryWork() {
        List<AssociatedShelfDto> shelves = underTest.findAssociatedShelves(2, "OL8208787W");
        List<AssociatedShelfDto> expectedResult = Arrays.asList(new AssociatedShelfDto(3,
                "English & Misc. Architecture"), new AssociatedShelfDto(5, "Books About Led Zeppelin"));
        assertIterableEquals(shelves, expectedResult);

        List<AssociatedShelfDto> emptyListOfShelves = underTest.findAssociatedShelves(2, "OL00000000");
        assertIterableEquals(emptyListOfShelves, new ArrayList<>());
    }
}