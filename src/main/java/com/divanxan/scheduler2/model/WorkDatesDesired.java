package com.divanxan.scheduler2.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "workdatesdesired")
@Data
public class WorkDatesDesired extends BaseEntity {

    @Column(name = "datework")
    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;
}
