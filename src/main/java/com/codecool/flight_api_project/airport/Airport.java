package com.codecool.flight_api_project.airport;

import com.codecool.flight_api_project.city.City;
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
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(airportIataCode, airport.airportIataCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportIataCode);
    }
}
