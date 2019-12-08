package com.divanxan.scheduler2.repository;

import com.divanxan.scheduler2.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
