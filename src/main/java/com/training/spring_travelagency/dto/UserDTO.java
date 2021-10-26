package com.training.spring_travelagency.dto;

import com.training.spring_travelagency.entity.enums.Roles;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class UserDTO {
    private long id;

    @NotBlank
    @Email(message = "{validation.user.email.invalid}")
    private String email;

    private String password;

    @NotBlank
    @Size(max = 50, message = "{validation.user.full.name}")
    private String fullName;

    private Roles role;
}
