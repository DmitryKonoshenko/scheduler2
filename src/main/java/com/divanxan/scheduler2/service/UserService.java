package com.divanxan.scheduler2.service;

import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.model.WorkDates;
import com.divanxan.scheduler2.model.WorkDatesDesired;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    List<WorkDates> findDaysById(Long id);
    List<WorkDatesDesired> findDesiredDaysById(Long id);

    void deleteDate(Long id);
    boolean addDate(WorkDatesDesired date);
}
