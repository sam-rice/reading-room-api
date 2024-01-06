package com.samrice.readingroomapi.repositories;

import com.samrice.readingroomapi.domain.User;
import com.samrice.readingroomapi.exceptions.RrAuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryImplIntegrationTests {

    private UserRepositoryImpl underTest;

    @Autowired
    public UserRepositoryImplIntegrationTests(UserRepositoryImpl underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalled() {
        Integer newUserId = underTest.create("Stanley", "Kubrick", "stan@mail.com", "clockwork");
        assertNotNull(newUserId);
        assertNotEquals(0, newUserId);
        User retrievedUser = underTest.findById(newUserId);
        assertNotNull(retrievedUser);
        assertEquals("Stanley", retrievedUser.getFirstName());
        assertEquals("Kubrick", retrievedUser.getLastName());
        assertEquals("stan@mail.com", retrievedUser.getEmail());
    }

    @Test
    public void testThatInvalidUserRegistryDetailsWillThrowAuthException() {
        assertThrows(RrAuthException.class, () -> {
            underTest.create(null, "Kubrick", "stan@mail.com", "clockwork");
        });
    }

    @Test
    public void testThatUserCanBeFoundByEmailAndPassword() {
        User authenticatedUser = underTest.findByEmailAndPassword("jimmy@mail.com", "guitar");
        assertEquals(2, authenticatedUser.getUserId());
        assertEquals("Jimmy", authenticatedUser.getFirstName());
        assertEquals("Page", authenticatedUser.getLastName());
        assertEquals("$2a$10$DcNMEVuyNGLVB5A7ZQrLve49b4eaZpo6abU3Gkpj87k34V/MzgED6", authenticatedUser.getPassword());
    }

    @Test
    public void testThatInvalidEmailOrPasswordWillThrowAuthException() {
        assertThrows(RrAuthException.class, () -> {
            underTest.findByEmailAndPassword("invalidemail", "guitar");
        });
        assertThrows(RrAuthException.class, () -> {
            underTest.findByEmailAndPassword("jimmy@mail.com", "invalidpassword");
        });
    }

    @Test
    public void testThatPreviouslyUsedEmailsCanBeCounted() {
        Integer result = underTest.getCountByEmail("jimmy@mail.com");
        assertEquals(result, 1);
    }

    @Test
    public void testThatUserCanBeFoundById() {
        User user = new User(2, "Jimmy", "Page", "jimmy@mail.com", "$2a$10$DcNMEVuyNGLVB5A7ZQrLve49b4eaZpo6abU3Gkpj87k34V/MzgED6");
        User result = underTest.findById(user.getUserId());
        assertThat(result.equals(user));
    }
}
