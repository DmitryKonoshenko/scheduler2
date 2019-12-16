package com.divanxan.scheduler2.service;

import com.divanxan.scheduler2.dto.CarDto;
import com.divanxan.scheduler2.dto.UserDto;
import com.divanxan.scheduler2.dto.WorkDatesDto;
import com.divanxan.scheduler2.model.Car;
import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.model.WorkDates;
import com.divanxan.scheduler2.model.WorkDatesDesired;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();
    List<UserDto> getAllDto();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    List<WorkDates> findDaysById(Long id);
    List<WorkDatesDto> findDaysDtoById(Long id);
    List<Car> findAllCars();
    List<CarDto> findAllCarsDto();
    List<WorkDatesDesired> findDesiredDaysById(Long id);

    void deleteDate(Long id);
    boolean addDate(WorkDatesDesired date);
}
