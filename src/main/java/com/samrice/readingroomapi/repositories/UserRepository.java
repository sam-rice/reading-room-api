package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;

public interface UserRepository {

    Integer createUser(String firstName, String lastName, String email, String password) throws RrAuthException;

    User findByEmailAndPassword(String email, String password) throws RrAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
