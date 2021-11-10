package com.training.spring_travelagency.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;


@Data
@Builder
public class OrderDTO {
    private long id;

    @Min(value = 0, message = "{validation.set.discount}")
    @Max(value = 100, message = "{validation.set.discount}")
    private double discount;
}