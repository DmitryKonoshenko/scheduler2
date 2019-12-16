package com.divanxan.scheduler2.dto;

import com.divanxan.scheduler2.model.Duty;
import com.divanxan.scheduler2.model.Status;
import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.model.WorkDatesDesired;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Comparable<UserDto> {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Duty duty;
    private int honest;
    @JsonIgnore
    private int shift = 0;
    private List<WorkDatesDto> workDates;
    private List<WorkDatesDesired> workDatesDesireds;

    public UserDto(){}
    public UserDto(UserDto userDto) {
        this.id = userDto.id;
        this.username = userDto.username;
        this.firstName = userDto.firstName;
        this.lastName = userDto.lastName;
        this.email = userDto.email;
        this.duty = userDto.duty;
        this.honest = userDto.honest;
        this.workDatesDesireds = new ArrayList<>();
        this.workDates = new ArrayList<>();
        if (userDto.workDates != null || !userDto.workDates.isEmpty()) {
            for (WorkDatesDto wd : userDto.getWorkDates()) {
                WorkDatesDto newWd = new WorkDatesDto();
                newWd.setDate(wd.getDate());
                newWd.setCar(wd.getCar());
                newWd.setId(wd.getId());
                this.workDates.add(newWd);
            }
        }
        if (userDto.workDatesDesireds != null || !userDto.workDatesDesireds.isEmpty()) {
            for (WorkDatesDesired wd : userDto.getWorkDatesDesireds()) {
                WorkDatesDesired newWd = new WorkDatesDesired();
                newWd.setDate(wd.getDate());
                newWd.setStatus(Status.ACTIVE);
                newWd.setUserId(wd.getUserId());
                newWd.setId(wd.getId());
                newWd.setUpdated(wd.getUpdated());
                newWd.setCreated(wd.getCreated());
                this.workDatesDesireds.add(newWd);
            }
        }
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setHonest(honest);
        return user;
    }

    public static UserDto fromUser(User user, List<WorkDatesDto> workDates, List<WorkDatesDesired> workDatesDesireds) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setWorkDates(workDates);
        userDto.setWorkDatesDesireds(workDatesDesireds);
        userDto.setDuty(user.getDuty());
        userDto.setHonest(user.getHonest());
        return userDto;
    }

    public List<Integer> getDatesPossible(int lastLastDay) {
        if (workDates == null || workDates.isEmpty()) return null;
        else {
            List<Integer> dates = new ArrayList<>();
            int lastDate = 1;
            for (WorkDatesDto workDate : this.workDates) {
                int nextDate = workDate.getDate().getDayOfMonth();
                addDates(dates, lastDate, nextDate);
                lastDate = nextDate;
            }
            if ((lastLastDay - lastDate) > 3) {
                for (int i = lastDate + 3; i <= lastLastDay; i++) {
                    dates.add(i);
                }
            }
            return dates;
        }
    }

    private void addDates(List<Integer> dates, int lastDate, int nextDate) {
        if ((nextDate - lastDate) == 6) {
            dates.add(lastDate + 3);
        } else if ((nextDate - lastDate) > 6) {
            dates.add(lastDate + 3);
            for (int i = 4; i < (nextDate - lastDate - 2); i++) {
                dates.add(lastDate + i);
            }
        }
    }

    @Override
    public int compareTo(UserDto o) {
        return o.getHonest() - this.honest;
    }
}
