package com.training.spring_travelagency.dto;

import lombok.Data;

import javax.validation.constraints.*;


@Data
public class OrderDTO {
    private long id;

    @Min(value = 0, message = "{validation.set.discount}")
    @Max(value = 100)
    private double discount;
}