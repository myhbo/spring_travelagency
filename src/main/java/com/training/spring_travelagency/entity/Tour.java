package com.training.spring_travelagency.entity;

import com.training.spring_travelagency.entity.enums.HotelType;
import com.training.spring_travelagency.entity.enums.TourType;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString(exclude = {"orders"})
@EqualsAndHashCode(exclude = {"orders"})
@Table(name = "tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private TourType tourType;

    @Column
    private double price;

    @Column
    private int groupSize;

    @Enumerated(value = EnumType.STRING)
    private HotelType hotelType;

    @Column
    private boolean isHot;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Order> orders;

    public void changeHot() {
        isHot = !isHot();
    }
}