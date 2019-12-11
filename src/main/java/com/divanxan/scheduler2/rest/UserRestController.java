package com.divanxan.scheduler2.rest;

import com.divanxan.scheduler2.dto.AuthenticationRequestDto;
import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.security.jwt.JwtTokenProvider;
import com.divanxan.scheduler2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/users/")
public class UserRestController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserRestController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "{token}")
    public ResponseEntity<String> getUserById(@PathVariable(name = "token") String token) {
        String userName = jwtTokenProvider.getUsername(token);

        if (userName == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthenticationRequestDto requestDto) {
        User user = new User();

        user.setUsername(requestDto.getUsername());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getSecondName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        user = userService.register(user);

        return ResponseEntity.ok("true");
    }
}
