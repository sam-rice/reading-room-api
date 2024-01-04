package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.Shelf;
import com.samrice.readingroomapi.exceptions.RrBadRequestException;
import com.samrice.readingroomapi.exceptions.RrResourceNotFoundException;
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
public class ShelfRepositoryImpl implements ShelfRepository {

    private static final String SQL_CREATE_SHELF = "INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), ?, ?, ?)";
    private static final String SQL_FIND_ALL_SHELVES_BY_USER_ID = "SELECT s.*, COALESCE(sb.total_saved_books, 0) AS total_saved_books " +
            "FROM rr_shelves s " +
            "LEFT JOIN (" +
            "    SELECT shelf_id, COUNT(*) AS total_saved_books " +
            "    FROM rr_saved_books " +
            "    GROUP BY shelf_id" +
            ") sb ON s.shelf_id = sb.shelf_id " +
            "WHERE s.user_id = ?";
    private static final String SQL_FIND_SHELF_BY_ID = "WITH ShelfBookCount AS (" +
            "    SELECT shelf_id, COUNT(*) AS total_saved_books " +
            "    FROM rr_saved_books " +
            "    WHERE shelf_id = ? " +
            "    GROUP BY shelf_id" +
            ") " +
            "SELECT s.*, COALESCE(c.total_saved_books, 0) AS total_saved_books " +
            "FROM rr_shelves s " +
            "LEFT JOIN ShelfBookCount c ON s.shelf_id = c.shelf_id " +
            "WHERE s.user_id = ? AND s.shelf_id = ?";
    private static final String SQL_UPDATE_SHELF = "UPDATE rr_shelves SET title = ?, description = ? " +
            "WHERE user_id = ? AND shelf_id = ?";
    private static final String SQL_DELETE_SHELF = "DELETE FROM rr_shelves WHERE user_id = ? AND shelf_id = ?";
    private static final String SQL_DELETE_ALL_BOOKS_BY_SHELF = "DELETE FROM rr_saved_books WHERE user_id = ? AND shelf_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Shelf> findAllShelvesByUserId(Integer userId) throws RrResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL_SHELVES_BY_USER_ID, new Object[]{userId}, shelfRowMapper);
    }

    @Override
    public Shelf findShelfById(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_SHELF_BY_ID, new Object[]{shelfId, userId, shelfId}, shelfRowMapper);
        } catch (Exception e) {
            throw new RrResourceNotFoundException("Shelf not found");
        }
    }

    @Override
    public Integer createShelf(Integer userId, String title, String description) throws RrBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_SHELF, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("SHELF_ID");
        } catch (Exception e) {
            throw new RrBadRequestException("Invalid details. Failed to create shelf.");
        }
    }

    @Override
    public void updateShelf(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE_SHELF, new Object[]{shelf.getTitle(), shelf.getDescription(), userId, shelfId});
        } catch (Exception e) {
            throw new RrBadRequestException("Invalid request. Failed to update shelf.");
        }
    }

    @Override
    public void deleteShelfWithAllBooks(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        this.deleteAllBooksByShelf(userId, shelfId);
        jdbcTemplate.update(SQL_DELETE_SHELF, new Object[]{userId, shelfId});
    }

    private void deleteAllBooksByShelf(Integer userId, Integer shelfId) {
        jdbcTemplate.update(SQL_DELETE_ALL_BOOKS_BY_SHELF, new Object[]{userId, shelfId});
    }

    private RowMapper<Shelf> shelfRowMapper = (rs, rowNum) -> {
        return new Shelf(rs.getInt("SHELF_ID"),
                rs.getInt("USER_ID"),
                rs.getString("TITLE"),
                rs.getString("DESCRIPTION"),
                rs.getInt("TOTAL_SAVED_BOOKS"));
    };
}
