package com.training.spring_travelagency.service;

import com.training.spring_travelagency.dto.OrderDTO;
import com.training.spring_travelagency.entity.Order;
import com.training.spring_travelagency.entity.Tour;
import com.training.spring_travelagency.entity.User;
import com.training.spring_travelagency.entity.enums.OrderStatus;
import com.training.spring_travelagency.repository.OrderRepository;
import com.training.spring_travelagency.repository.TourRepository;
import com.training.spring_travelagency.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@Log4j2
public class OrderService {
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository,
                        TourRepository tourRepository,
                        OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.orderRepository = orderRepository;
    }

    public Page<Order> findAllOrdersPageable(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * Adding new order to the DB only with valid userId and tourId through builder
     * @param userId method will trow exception if user with this ID doesn't exist
     * @param tourId method will trow exception if tour with this ID doesn't exist
     */
    @Transactional
    public void orderTour(Long userId, Long tourId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour id: " + tourId));
        Order order = Order.builder()
                .user(user)
                .tour(tour)
                .status(OrderStatus.PROCESSING)
                .price(tour.getPrice())
                .discount(0D)
                .build();
        orderRepository.save(order);
        log.info(user + " ordered " + tour);
    }

    @Transactional
    public void markOrderAsConfirmed(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        log.info("Order confirmed " + order);
    }

    @Transactional
    public void markOrderAsRejected(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        log.info("Order rejected " + order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        order.getUser().getOrders().remove(order);
        order.getTour().getOrders().remove(order);
        orderRepository.deleteById(id);
        log.info("Order deleted " + order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("There is no order with ID: " + id));
    }

    /**
     * Applying discount to specific order with auto updating its price
     * @param id order ID
     * @param orderDTO DTO of order with validation for discount
     */
    @Transactional
    public void setDiscount(Long id, @Valid OrderDTO orderDTO) {
        Order order = getOrderById(id);
        order.setDiscount(orderDTO.getDiscount());
        order.setPrice(order.getTour().getPrice() * ((100 - orderDTO.getDiscount())/100));
        orderRepository.save(order);
        log.info("Discount set for order ID " + id);
    }
}
