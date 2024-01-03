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

    private static final String SQL_CREATE = "INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), ?, ?, ?)";
    private static final String SQL_FIND_BY_ID = "WITH ShelfBookCount AS (" +
            "    SELECT shelf_id, COUNT(*) AS total_saved_books " +
            "    FROM rr_saved_books " +
            "    WHERE shelf_id = ? " +
            "    GROUP BY shelf_id" +
            ") " +
            "SELECT s.*, COALESCE(c.total_saved_books, 0) AS total_saved_books " +
            "FROM rr_shelves s " +
            "LEFT JOIN ShelfBookCount c ON s.shelf_id = c.shelf_id " +
            "WHERE s.user_id = ? AND s.shelf_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Shelf> findAll(Integer userId) throws RrResourceNotFoundException {
        return null;
    }

    @Override
    public Shelf findById(Integer userId, Integer shelfId) throws RrResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{shelfId, userId, shelfId}, shelfRowMapper);
        } catch (Exception e) {
            throw new RrResourceNotFoundException("Shelf not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws RrBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
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
    public void update(Integer userId, Integer shelfId, Shelf shelf) throws RrBadRequestException {

    }

    @Override
    public void delete(Integer userId, Integer shelfId) throws RrResourceNotFoundException {

    }

    private RowMapper<Shelf> shelfRowMapper = (rs, rowNum) -> {
        return new Shelf(rs.getInt("SHELF_ID"),
                rs.getInt("USER_ID"),
                rs.getString("TITLE"),
                rs.getString("DESCRIPTION"),
                rs.getInt("TOTAL_SAVED_BOOKS"));
    };
}
