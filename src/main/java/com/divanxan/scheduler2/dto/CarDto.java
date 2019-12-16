package com.divanxan.scheduler2.dto;

import com.divanxan.scheduler2.model.Car;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDto {
    private Long id;
    private String number;

    public static CarDto fromCar(Car car) {
        CarDto carDto = new CarDto();
        carDto.setId(car.getId());
        carDto.setNumber(car.getNumber());

        return carDto;
    }
}
