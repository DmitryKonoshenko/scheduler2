package com.divanxan.scheduler2.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "workdates")
@Data
public class WorkDates extends BaseEntity {

    @Column(name = "datework")
    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "car_id")
    private Long carId;
}
