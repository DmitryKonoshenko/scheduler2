package com.divanxan.scheduler2.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cars")
@Data
public class Car extends BaseEntity {

    @Column(name = "number")
    private String number;

}
