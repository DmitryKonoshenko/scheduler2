package com.divanxan.scheduler2.dto;

import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.model.WorkDates;
import com.divanxan.scheduler2.model.WorkDatesDesired;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<WorkDates> workDates;
    private List<WorkDatesDesired> workDatesDesireds;

    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

    public static UserDto fromUser(User user, List<WorkDates> workDates, List<WorkDatesDesired> workDatesDesireds) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setWorkDates(workDates);
        userDto.setWorkDatesDesireds(workDatesDesireds);

        return userDto;
    }
}
