package com.divanxan.scheduler2.dto;

import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.model.WorkDates;
import com.divanxan.scheduler2.model.WorkDatesDesired;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<WorkDates> workDates = new HashSet<>();
    private Set<WorkDatesDesired> workDatesDesireds = new HashSet<>();

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setWorkDates(workDates);
        user.setWorkDatesDesireds(workDatesDesireds);
        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setWorkDates(user.getWorkDates());
        userDto.setWorkDatesDesireds(user.getWorkDatesDesireds());

        return userDto;
    }
}
