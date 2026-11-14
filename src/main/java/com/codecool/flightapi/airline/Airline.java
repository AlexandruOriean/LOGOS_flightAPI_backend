package com.codecool.flightapi.airline;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Airline")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    //
    //    @Column(name="airline_iata_code")
    //    private String airlineIataCode;

    @Column(name = "iso")
    private String iso;
    //
    //    @OneToMany(
    //            fetch = FetchType.EAGER,
    //                cascade = CascadeType.ALL,
    //                orphanRemoval = true,
    //                mappedBy = "airline")
    //    private List<Airplane> airplaneList =  new ArrayList<>();
    //
    //
    //    @OneToMany(
    //            fetch = FetchType.EAGER,
    //            cascade = CascadeType.ALL,
    //            orphanRemoval = true,
    //            mappedBy = "airline"
    //    )
    //    private List<Flight> flights = new ArrayList<>();
}
