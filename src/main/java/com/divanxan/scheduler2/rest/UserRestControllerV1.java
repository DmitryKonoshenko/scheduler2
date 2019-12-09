package com.divanxan.scheduler2.rest;

import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.security.jwt.JwtTokenProvider;
import com.divanxan.scheduler2.service.UserService;
import com.divanxan.scheduler2.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users/")
public class UserRestControllerV1 {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserRestControllerV1(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "{token}")
    public ResponseEntity<String> getUserById(@PathVariable(name = "token") String token){
        String userName = jwtTokenProvider.getUsername(token);

        if(userName == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity<>(userName, HttpStatus.OK);
    }
}
