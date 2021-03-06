package com.divanxan.scheduler2.rest;

import com.divanxan.scheduler2.dto.AdminUserDto;
import com.divanxan.scheduler2.dto.CarDto;
import com.divanxan.scheduler2.dto.UserDto;
import com.divanxan.scheduler2.model.User;
import com.divanxan.scheduler2.service.UserService;
import com.divanxan.scheduler2.shedulerdata.SchedulerData;
import com.divanxan.scheduler2.shedulerdata.SchedulerPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/")
public class AdminRestControllerV1 {

    private final UserService userService;

    @Autowired
    public AdminRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AdminUserDto result = AdminUserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "create")
    public ResponseEntity<String> createScheduler() {
        List<List<SchedulerData>> data = SchedulerPreparer.prepareScheduler(2, 31);
        List<UserDto> usersDto = userService.getAllDto();
        List<CarDto> cars = userService.findAllCarsDto();
        usersDto = SchedulerPreparer.createScheduler(data, usersDto, cars);

        userService.philUpScheduler(usersDto);

        return new ResponseEntity<>("true", HttpStatus.OK);
    }
}
