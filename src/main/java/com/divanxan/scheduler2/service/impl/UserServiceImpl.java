package com.divanxan.scheduler2.service.impl;

import com.divanxan.scheduler2.model.*;
import com.divanxan.scheduler2.repository.RoleRepository;
import com.divanxan.scheduler2.repository.UserRepository;
import com.divanxan.scheduler2.repository.WorkDatesDesiredRepository;
import com.divanxan.scheduler2.repository.WorkDatesRepository;
import com.divanxan.scheduler2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WorkDatesRepository datesRepository;
    private final WorkDatesDesiredRepository datesDesiredRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder
            , WorkDatesRepository datesRepository, WorkDatesDesiredRepository datesDesiredRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.datesRepository = datesRepository;
        this.datesDesiredRepository = datesDesiredRepository;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    @Override
    public List<WorkDates> findDaysById(Long id) {
        List<WorkDates> dates = datesRepository.findAll();
        List<WorkDates> result = new ArrayList<>();
        for (WorkDates date : dates) {
            if (date.getUserId().equals(id)) {
                result.add(date);
            }
        }

        return result;
    }

    @Override
    public List<WorkDatesDesired> findDesiredDaysById(Long id) {
        List<WorkDatesDesired> dates = datesDesiredRepository.findAll();
        List<WorkDatesDesired> result = new ArrayList<>();
        for (WorkDatesDesired date : dates) {
            if (date.getUserId().equals(id)) {
                result.add(date);
            }
        }

        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted");
    }

    @Override
    public void deleteDate(Long id){
        datesDesiredRepository.deleteById(id);
    }

    @Override
    public boolean addDate(WorkDatesDesired date) {
        date.setCreated(LocalDateTime.now());
        date.setUpdated(LocalDateTime.now());
        date.setStatus(Status.ACTIVE);
        datesDesiredRepository.save(date);
        return true;
    }
}
