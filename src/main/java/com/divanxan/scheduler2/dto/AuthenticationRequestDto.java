package com.divanxan.scheduler2.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String username;
    private String firstName;
    private String secondName;
    private String email;
    private String password;
}
