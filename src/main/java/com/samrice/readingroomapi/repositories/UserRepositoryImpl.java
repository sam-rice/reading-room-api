package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws RrAuthException {
        return null;
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws RrAuthException {
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        return null;
    }

    @Override
    public User findById(Integer userId) {
        return null;
    }
}
