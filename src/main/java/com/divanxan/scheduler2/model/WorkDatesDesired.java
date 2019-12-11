package com.divanxan.scheduler2.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "workdatesdesired")
@Data
public class WorkDatesDesired extends BaseEntity {

    @Column(name = "dateworkdesired")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
