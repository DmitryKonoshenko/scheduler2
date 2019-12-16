package com.divanxan.scheduler2.repository;

import com.divanxan.scheduler2.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
