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

    @Column(name = "iso")
    private String iso;
}
