package com.codecool.flightapi.cart;

import com.codecool.flightapi.flight.Flight;
import com.codecool.flightapi.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_Id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public Cart() {}

    public Cart(Long id, User userId, Flight flight) {
        this.id = id;
        this.userId = userId;
        this.flight = flight;
    }

    public Long getId() {
        return id;
    }

    public User getUserId() {
        return userId;
    }

    public Flight getFlight() {
        return flight;
    }
}
