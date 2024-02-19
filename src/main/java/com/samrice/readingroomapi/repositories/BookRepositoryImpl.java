package com.samrice.readingroomapi.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samrice.readingroomapi.domains.BasicAuthor;
import com.samrice.readingroomapi.domains.Book;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
import com.samrice.readingroomapi.utilities.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private static final String SQL_CREATE_BOOK = "INSERT INTO rr_saved_books (book_id, shelf_id, user_id, library_key, title, authors, cover_url, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BOOK_BY_ID = "SELECT * FROM rr_saved_books WHERE user_id = ? AND shelf_id = ? AND book_id = ?";
    private static final String SQL_FIND_ALL_BOOKS_BY_SHELF_ID = "SELECT * from rr_saved_books WHERE user_id = ? AND shelf_id = ? ORDER BY book_id ASC";
    private static final String SQL_UPDATE_BOOK = "UPDATE rr_saved_books SET user_note = ? WHERE user_id = ? AND shelf_id = ? AND book_id = ?";
    private static final String SQL_DELETE_BOOK = "DELETE FROM rr_saved_books WHERE user_id = ? AND shelf_id = ? AND book_id = ?";
    private static final String SQL_FIND_FIRST_SAVED_BOOK_ON_SHELF = "SELECT sb.* FROM rr_saved_books sb WHERE sb.user_id= ? AND sb.shelf_id = ? AND sb.cover_url IS NOT NULL ORDER BY sb.book_id LIMIT 1";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper mapper;

    @Override
    public Integer createBook(Integer shelfId,
                              Integer userId,
                              String libraryKey,
                              String title,
                              List<BasicAuthor> authorsList,
                              String coverUrl,
                              String userNote) throws RrBadRequestException {
        try {
            String stringifiedAuthorList = Json.toJsonString(authorsList);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            long timestamp = new java.util.Date().getTime();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_BOOK, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, shelfId);
                ps.setInt(2, userId);
                ps.setString(3, libraryKey);
                ps.setString(4, title);
                ps.setObject(5, stringifiedAuthorList, java.sql.Types.OTHER);
                ps.setString(6, coverUrl);
                ps.setString(7, userNote);
                ps.setLong(8, timestamp);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("BOOK_ID");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RrBadRequestException("Invalid details. Book could not be saved.");
        }
    }

    @Override
    public Book findBookById(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BOOK_BY_ID,
                    new Object[]{userId, shelfId, bookId},
                    bookRowMapper);
        } catch (Exception e) {
            throw new RrResourceNotFoundException("Book not found");
        }
    }

    @Override
    public List<Book> findAllBooksByShelfId(Integer userId, Integer shelfId) {
        return jdbcTemplate.query(SQL_FIND_ALL_BOOKS_BY_SHELF_ID, new Object[]{userId, shelfId}, bookRowMapper);
    }

    @Override
    public void updateBook(Integer userId, Integer shelfId, Integer bookId, Book book) throws RrBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BOOK, new Object[]{book.userNote(), userId, shelfId, bookId});
        } catch (Exception e) {
            throw new RrBadRequestException("Invalid details. Book not updated.");
        }
    }

    @Override
    public void deleteBook(Integer userId, Integer shelfId, Integer bookId) throws RrResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE_BOOK, new Object[]{userId, shelfId, bookId});
        if (count == 0) {
            throw new RrResourceNotFoundException("Book not found");
        }
    }

    @Override
    public Book findFirstSavedBookOnShelf(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        List<Book> books = jdbcTemplate.query(SQL_FIND_FIRST_SAVED_BOOK_ON_SHELF,
                new Object[]{userId, shelfId},
                bookRowMapper);
        return books.isEmpty() ? null : books.get(0);
    }

    private RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        try {
            String authorsJson = rs.getString("authors");
            List<BasicAuthor> authorsList = mapper.readValue(authorsJson, new TypeReference<List<BasicAuthor>>() {
            });
            return new Book(rs.getInt("book_id"),
                    rs.getInt("shelf_id"),
                    rs.getInt("user_id"),
                    rs.getString("library_key"),
                    rs.getString("title"),
                    authorsList,
                    rs.getString("cover_url"),
                    rs.getString("user_note"),
                    rs.getLong("saved_date"));
        } catch (JsonProcessingException e) {
            throw new RrBadRequestException(e.getMessage());
        }
    };
}
