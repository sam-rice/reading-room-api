package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domains.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;

public interface UserService {

    User validateUser(String email, String password) throws RrAuthException;

    User registerUser(String firstName, String lastName, String email, String password) throws RrAuthException;
}
