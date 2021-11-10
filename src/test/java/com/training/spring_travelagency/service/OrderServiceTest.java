package com.training.spring_travelagency.service;

import com.training.spring_travelagency.dto.OrderDTO;
import com.training.spring_travelagency.entity.Order;
import com.training.spring_travelagency.entity.Tour;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.HotelType;
import com.training.spring_travelagency.entity.enums.OrderStatus;
import com.training.spring_travelagency.entity.enums.Roles;
import com.training.spring_travelagency.entity.enums.TourType;
import com.training.spring_travelagency.repository.OrderRepository;
import com.training.spring_travelagency.repository.TourRepository;
import com.training.spring_travelagency.repository.UserRepository;
import org.h2.engine.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.thymeleaf.templateparser.raw.IRawHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TourRepository tourRepository;

    @Test
    public void setDiscount() {
        User user = createTestUser();
        Tour tour = createTestTour();

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .tour(tour)
                .status(OrderStatus.PROCESSING)
                .price(tour.getPrice())
                .discount(0D)
                .build();

        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        user.setOrders(orderList);
        tour.setOrders(orderList);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.setDiscount(order.getId(), createOrderDto());
        assertEquals(order.getPrice(), 500D, 1);
    }

    private static OrderDTO createOrderDto() {
        return OrderDTO.builder()
                .discount(50D)
                .build();
    }

    private static User createTestUser() {
        return User.builder()
                .id(1L)
                .email("1@1.1")
                .fullName("admin")
                .role(Roles.USER)
                .build();
    }

    private static Tour createTestTour() {
        return Tour.builder()
                .id(1L)
                .name("tour")
                .tourType(TourType.EXCURSION)
                .hotelType(HotelType.HOTEL)
                .groupSize(1)
                .price(1000D)
                .isHot(false)
                .build();
    }
}