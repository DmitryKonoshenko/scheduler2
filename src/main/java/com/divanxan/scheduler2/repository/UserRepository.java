package com.divanxan.scheduler2.repository;

import com.divanxan.scheduler2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
