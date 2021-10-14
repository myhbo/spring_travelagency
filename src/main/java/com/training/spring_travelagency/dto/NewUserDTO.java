package com.training.spring_travelagency.dto;

import lombok.Data;

@Data
public class NewUserDTO {
    private String email;

    private String password;

    private String fullName;

}