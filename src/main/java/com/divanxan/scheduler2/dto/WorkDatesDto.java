package com.divanxan.scheduler2.dto;

import com.divanxan.scheduler2.model.Car;
import com.divanxan.scheduler2.model.WorkDates;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkDatesDto {

    private Long id;
    private CarDto car;
    private LocalDate date;

    public static WorkDatesDto fromWorkDates(WorkDates workDates, CarDto carDto) {
        WorkDatesDto workDatesDto = new WorkDatesDto();
        workDatesDto.setId(workDates.getId());
        workDatesDto.setCar(carDto);
        workDatesDto.setDate(workDates.getDate());

        return workDatesDto;
    }
}
