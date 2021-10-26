package com.training.spring_travelagency.entity;

import com.training.spring_travelagency.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tour tour;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "price")
    private double price;

    @Column(name = "discount")
    private double discount;

    public boolean isProcessing() {
        return status == OrderStatus.PROCESSING;
    }
    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED;
    }
    public boolean isRejected() {
        return status == OrderStatus.REJECTED;
    }
}
