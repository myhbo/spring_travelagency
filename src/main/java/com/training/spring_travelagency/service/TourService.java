package com.training.spring_travelagency.service;

import com.training.spring_travelagency.dto.TourDTO;
import com.training.spring_travelagency.entity.Tour;
import com.training.spring_travelagency.entity.enums.OrderStatus;
import com.training.spring_travelagency.repository.TourRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Log4j2
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public Page<Tour> findAllToursPageable(Pageable pageable) {
        return tourRepository.findAll(pageable);
    }

    public void createTour(TourDTO tourDTO) {
        Tour tour = Tour.builder()
                .name(tourDTO.getName())
                .tourType(tourDTO.getTourType())
                .price(tourDTO.getPrice())
                .groupSize(tourDTO.getGroupSize())
                .hotelType(tourDTO.getHotelType())
                .isHot(false)
                .build();
        tourRepository.save(tour);
        log.info("Added tour " + tour);
    }

    @Transactional
    public void updateTour(long id, TourDTO tourDTO) {
        Tour tour = getTourById(id);
        tour.setName(tourDTO.getName());
        tour.setTourType(tourDTO.getTourType());
        if (tour.getPrice() != tourDTO.getPrice()) {
            double updatedPrice = tourDTO.getPrice();
            tour.setPrice(updatedPrice);
            tour.getOrders().stream()
                    .filter((order) -> order.getStatus() == OrderStatus.PROCESSING)
                    .forEach((order) -> {
                        order.setPrice(updatedPrice);
                    });
        }
        tour.setGroupSize(tourDTO.getGroupSize());
        tour.setHotelType(tourDTO.getHotelType());
        tourRepository.save(tour);
        log.info("Updated tour " + tour);
    }

    @Transactional
    public void toggleHot(Long id) {
        Tour tour = getTourById(id);
        tour.changeHot();
        tourRepository.save(tour);
        log.info("Toggled hot " + tour);
    }

    @Transactional
    public void deleteTour(Long id) {
        Tour tour = getTourById(id);
        tourRepository.delete(tour);
        log.info("Deleted tour " + tour);
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid tour id: " + id));
    }
}
