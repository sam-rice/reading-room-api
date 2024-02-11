package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domains.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(NEXTVAL('rr_users_seq'), ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM rr_users WHERE email = ?";
    private static final String SQL_FIND_BY_ID = "SELECT user_id, first_name, last_name, email, password " + "FROM rr_users WHERE user_id = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT user_id, first_name, last_name, email, password " + "FROM rr_users WHERE email = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer createUser(String firstName, String lastName, String email, String password) throws RrAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        } catch (Exception e) {
            throw new RrAuthException("Invalid details. Failed to create account.");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws RrAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new RrAuthException("Invalid password");
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new RrAuthException("Invalid email");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper);
    }

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        return new User(rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    };
}
