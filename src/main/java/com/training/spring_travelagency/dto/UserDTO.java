package com.training.spring_travelagency.dto;

import com.training.spring_travelagency.entity.enums.Roles;
import lombok.Data;


@Data
public class UserDTO {
    private long id;

    private String email;

    private String password;

    private String fullName;

    private Roles role;

}
