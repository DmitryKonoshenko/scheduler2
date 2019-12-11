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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
