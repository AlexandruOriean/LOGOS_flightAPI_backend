package com.codecool.flight_api_project.airplane;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="Airplane")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airplane
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name ="manufacturer")
    private String manufacturer;

    @Column(name ="model")
    private String model;

    @Column(name ="numberOfSeats")
    private Long numberOfSeats;

    @Column(name ="speed")
    private double speed;

//    @ManyToOne
//    @JoinColumn(name = "airline_id")
////    @JsonIgnore
//    private Airline airline;

}