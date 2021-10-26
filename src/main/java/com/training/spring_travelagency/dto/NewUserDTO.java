package com.training.spring_travelagency.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewUserDTO {
    @NotBlank
    @Email(message = "{validation.user.email.invalid}")
    private String email;

    @NotBlank
    @Size(min = 5, message = "{validation.user.password}")
    private String password;

    @NotBlank
    private String fullName;
}