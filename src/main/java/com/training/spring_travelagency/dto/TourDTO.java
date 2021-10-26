package com.training.spring_travelagency.dto;

import com.training.spring_travelagency.entity.enums.HotelType;
import com.training.spring_travelagency.entity.enums.TourType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class TourDTO {
    private long id;

    @NotBlank
    @Size(min = 5, max = 100, message = "{validation.tour.name}")
    private String name;

    @Positive(message = "{validation.tour.price}")
    private double price;

    @Positive(message = "{validation.tour.group}")
    private int groupSize;

    @NotNull
    private TourType tourType;

    @NotNull
    private HotelType hotelType;

    private boolean isHot;
}
