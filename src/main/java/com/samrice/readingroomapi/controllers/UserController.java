package com.samrice.readingroomapi.controllers;

import com.samrice.readingroomapi.Constants;
import com.samrice.readingroomapi.domains.User;
import com.samrice.readingroomapi.dtos.UserDto;
import com.samrice.readingroomapi.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody Map<String, Object> userMap, HttpServletResponse response) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.registerUser(firstName, lastName, email, password);
        UserDto userDto = new UserDto(user.getEmail(), user.getFirstName(), user.getLastName());
        response.addCookie(this.generateAuthCookie(user));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody Map<String, Object> userMap, HttpServletResponse response) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(email, password);
        UserDto userDto = new UserDto(user.getEmail(), user.getFirstName(), user.getLastName());
        response.addCookie(this.generateAuthCookie(user));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    private Cookie generateAuthCookie(User user) {
        long timestamp = System.currentTimeMillis();
        Date expiration = new Date(timestamp + Constants.TOKEN_VALIDITY_DURATION_MS);
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(expiration)
                .claim("userId", user.getUserId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .compact();
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge((int) Constants.TOKEN_VALIDITY_DURATION_MS / 100);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        return cookie;
    }
}
