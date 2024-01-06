package com.samrice.readingroomapi.services;

import com.samrice.readingroomapi.domain.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;
import com.samrice.readingroomapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws RrAuthException {
        if (email != null) email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws RrAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null) email = email.toLowerCase();

        if (!pattern.matcher(email).matches()) {
            throw new RrAuthException("Invalid email format");
        }

        Integer count = userRepository.getCountByEmail(email);
        if (count > 0) {
            throw new RrAuthException("Email already in use");
        }

        Integer userId = userRepository.createUser(firstName, lastName, email, password);
        return userRepository.findById(userId);
    }
}
