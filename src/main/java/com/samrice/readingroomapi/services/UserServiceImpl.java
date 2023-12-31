package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domain.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;

public class UserServiceImpl implements UserService {


    @Override
    public User validateUser(String email, String password) throws RrAuthException {
        return null;
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws RrAuthException {
        return null;
    }
}
