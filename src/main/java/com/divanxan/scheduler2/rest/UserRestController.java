package com.divanxan.scheduler2.rest;

import com.divanxan.scheduler2.dto.AuthenticationRequestDto;
import com.divanxan.scheduler2.dto.CarDto;
import com.divanxan.scheduler2.dto.UserDto;
import com.divanxan.scheduler2.dto.WorkDatesDto;
import com.divanxan.scheduler2.model.Car;
import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.model.WorkDates;
import com.divanxan.scheduler2.model.WorkDatesDesired;
import com.divanxan.scheduler2.security.jwt.JwtTokenProvider;
import com.divanxan.scheduler2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping(value = "user/{token}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "token") String token) {
        String userName = jwtTokenProvider.getUsername(token);

        if (userName == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User user = userService.findByUsername(userName);
        List<WorkDates> workDates = userService.findDaysById(user.getId());
        List<Car> cars = userService.findAllCars();

        List<WorkDatesDto> workDatesDto = new ArrayList<>();
        for (WorkDates dates : workDates) {
            CarDto carDto = null;
            for (Car car : cars) {
                if (car.getId().equals(dates.getCarId())) {
                    carDto = CarDto.fromCar(car);
                    break;
                }
            }
            workDatesDto.add(WorkDatesDto.fromWorkDates(dates, carDto));
        }

        List<WorkDatesDesired> workDatesDesireds = userService.findDesiredDaysById(user.getId());
        UserDto result = UserDto.fromUser(user, workDatesDto, workDatesDesireds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "deleteDate/{token}/{id}")
    public ResponseEntity<UserDto> deleteDateById(@PathVariable(name = "token") String token,@PathVariable(name = "id") Long id) {
        userService.deleteDate(id);
        String userName = jwtTokenProvider.getUsername(token);
        User user = userService.findByUsername(userName);
        List<WorkDates> workDates = userService.findDaysById(user.getId());
        List<Car> cars = userService.findAllCars();

        List<WorkDatesDto> workDatesDto = new ArrayList<>();
        for (WorkDates dates : workDates) {
            CarDto carDto = null;
            for (Car car : cars) {
                if (car.getId().equals(dates.getCarId())) {
                    carDto = CarDto.fromCar(car);
                    break;
                }
            }
            workDatesDto.add(WorkDatesDto.fromWorkDates(dates, carDto));
        }

        List<WorkDatesDesired> workDatesDesireds = userService.findDesiredDaysById(user.getId());

        UserDto result = UserDto.fromUser(user, workDatesDto, workDatesDesireds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "addDate/{token}/{date}")
    public ResponseEntity<String> addDate(@PathVariable(name = "token") String token, @PathVariable(name = "date") String date) {
        String userName = jwtTokenProvider.getUsername(token);
        User user = userService.findByUsername(userName);
        WorkDatesDesired workDates   = new WorkDatesDesired();
        workDates.setUserId(user.getId());
        workDates.setDate(LocalDate.parse(date));
        userService.addDate(workDates);
        return new ResponseEntity<>("true", HttpStatus.OK);
    }
}
