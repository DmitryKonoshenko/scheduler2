package com.divanxan.scheduler2.rest;

import com.divanxan.scheduler2.dto.UserDto;
import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }


    @GetMapping(value = "/getUserName")
    public ResponseEntity<String> getUserName() {
        return new ResponseEntity<>("{\"username\":\""+SecurityContextHolder.getContext().getAuthentication().getName()+"\"}", HttpStatus.OK);
    }

    @GetMapping(value = "users")
    public ResponseEntity<List<UserDto>> getUserById() {
        List<User> users = userService.getAll();

        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDto> usersDto = new ArrayList<>();
        for (User user: users) {
            UserDto result = UserDto.fromUser(user);
            usersDto.add(result);
        }

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
}
