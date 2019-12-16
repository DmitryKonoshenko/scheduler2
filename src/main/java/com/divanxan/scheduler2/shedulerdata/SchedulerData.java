package com.divanxan.scheduler2.shedulerdata;

import com.divanxan.scheduler2.dto.UserDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SchedulerData implements Comparable<SchedulerData> {

    public SchedulerData(int day, int car) {
        this.day = day;
        this.car = car;
    }

    private int day;
    private int car;
    private UserDto doctor;
    private UserDto driver;
    private UserDto paramedic;

    @Override
    public int compareTo(SchedulerData o) {
        return this.day - o.getDay();
    }

    public LocalDate getDate(){
        return LocalDate.of(2019,12,day);
    }
}
