package com.codecool.flightapi.airport;

import com.codecool.flightapi.city.City;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Entity
@Table(name = "Airport")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Airport {

    @Id
    @Column(name = "airport_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airport_iata_code")
    private String airportIataCode;

    @Column(name = "airport_name")
    private String airportName;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport airport)) return false;
        return Objects.equals(airportIataCode, airport.getAirportIataCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(airportIataCode);
    }
}
